import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.PriorityQueue;

public class ACC {

    private String name;
    private Hashtable<Integer, String> table;
    private PriorityQueue<Flight> ready;
    private int time;
    private ArrayList<Flight> waiting;
    private PriorityQueue<Flight> running;
    private ArrayList<ATC> airports;
    private ArrayList<Flight> boundary;

    public ACC(String name, Hashtable<Integer, String> table, PriorityQueue<Flight> ready, int time,
            ArrayList<Flight> waiting, PriorityQueue<Flight> running, ArrayList<ATC> airports,
            ArrayList<Flight> boundary) {
        this.name = name;
        this.table = table;
        this.ready = ready;
        this.time = time;
        this.waiting = waiting;
        this.running = running;
        this.airports = airports;
        this.boundary = boundary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hashtable<Integer, String> getTable() {
        return table;
    }

    public void setTable(Hashtable<Integer, String> table) {
        this.table = table;
    }

    public PriorityQueue<Flight> getReady() {
        return ready;
    }

    public void setReady(PriorityQueue<Flight> ready) {
        this.ready = ready;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<Flight> getWaiting() {
        return waiting;
    }

    public void setWaiting(ArrayList<Flight> waiting) {
        this.waiting = waiting;
    }

    public PriorityQueue<Flight> getRunning() {
        return running;
    }

    public void setRunning(PriorityQueue<Flight> running) {
        this.running = running;
    }

    public ArrayList<ATC> getAirports() {
        return airports;
    }

    public void setAirports(ArrayList<ATC> airports) {
        this.airports = airports;
    }

    public ArrayList<Flight> getBoundary() {
        return boundary;
    }

    public void setBoundary(ArrayList<Flight> boundary) {
        this.boundary = boundary;
    }

    @Override
    public String toString() {
        return "ACC [name=" + name + ", table=" + table + ", ready=" + ready + ", time=" + time + ", waiting=" + waiting
                + ", running=" + running + ", airports=" + airports + ", boundary=" + boundary + "]";
    }

    public void addToACC(ATC atc) {
        airports.add(atc);
        int index = atc.findIndex(atc.getCode());

        while (table.get(index) != null) { // linear probing
            index++;
        }
        String name = atc.codeGenerater(atc.getCode(), index);
        table.put(index, name);
    }

    private ATC findAirport(String str) {
        for (ATC atc : airports) {
            if (atc.getCode().equals(str))
                return atc;
        }
        return null;
    }

    private int findDurationACC() {
        int num = Integer.MAX_VALUE;
        if (running.isEmpty() && waiting.isEmpty() && (boundary.isEmpty())) {
            if (ready.isEmpty()){
                return -1;
            } return ready.peek().getAdmittion();
        }
        if (!boundary.isEmpty()) {
            for (int i = 0; i < boundary.size(); i++) {
                running.add(boundary.remove(i));
            }
        }
        if ((!running.isEmpty()) && (!waiting.isEmpty())) {
            for (Flight f : waiting) {
                if (f.getOperations().get(0) < num) {
                    num = f.getOperations().get(0);
                }
            }
            return Math.min(num, running.peek().getOperations().get(0));
        }
        if ((running.isEmpty()) && (!waiting.isEmpty())) {
            for (Flight f : waiting) {
                if (f.getOperations().get(0) < num) {
                    num = f.getOperations().get(0);
                }
            }
            return num;
        } else {
            return running.peek().getOperations().get(0);
        }
    }

    private int atcDurations() {
        int num = Integer.MAX_VALUE;
        for (ATC atc : airports) {
            if (!atc.getTransition().isEmpty()) {
                for (int i = 0; i < atc.getTransition().size(); i++) {
                    atc.getReady().add(atc.getTransition().remove(i));
                }
            }
            if ((atc.findDurationATC() >= 0) && (atc.findDurationATC() < num)) {
                num = atc.findDurationATC();
            }
        }
        return num;
    }

    private int findDuration() {
        if (atcDurations() == Integer.MAX_VALUE && findDurationACC() < 0) {
            return ready.peek().getAdmittion(); // eski hali direkt bunu returnliyor
        }
        if (findDurationACC() < 0) {
            if (atcDurations() < 30) {
                return atcDurations();
            } else {
                return 30;
            }
        }
        int num = Math.min(findDurationACC(), atcDurations());
        if ((!ready.isEmpty()) && time + num > ready.peek().getAdmittion()) {
            num = ready.peek().getAdmittion() - time;
        }
        if (running.isEmpty()) {
            return num;
        } else {
            return Math.min(num, 30 - running.peek().getRuntime());
        }

    }

    private void wait(int duration) {
        ArrayList<Integer> removedIndex = new ArrayList<>();
        for (int i = 0; i < waiting.size(); i++) {
            waiting.get(i).run(duration);
            waiting.get(i).setRuntime(duration);
            if (waiting.get(i).isFinished()) { // end of waiting

                waiting.get(i).setMoment(time);
                waiting.get(i).setAge(true);
                waiting.get(i).getOperations().remove(0);
                waiting.get(i).setCount(waiting.get(i).getCount() + 1);
                waiting.get(i).setRuntime(0);
                boundary.add(waiting.get(i));
                removedIndex.add(i);
            }
        }
        if (!removedIndex.isEmpty()) {
            Collections.sort(removedIndex, Collections.reverseOrder());
            for (int i : removedIndex) {
                waiting.remove(i);
            }
        }

    }

    public void operate(int num) {
        ArrayList<String> finishedFlights = new ArrayList<>();
        boolean checker = true;
        while (true) {
            if (finishedFlights.size() == num) {
                break;
            }

            int duration = findDuration();

            if (checker) {
                time = time + duration;
                running.add(ready.poll());
                try {
                    while (time == ready.peek().getAdmittion()) {
                        running.add(ready.poll());
                    }
                } catch (Exception e) {
                    // do nothing
                }
                checker = false;
                continue;
            }

            if ((!ready.isEmpty()) && time == ready.peek().getAdmittion()) {
                running.add(ready.poll());
                try {
                    if (time == ready.peek().getAdmittion()) {
                        continue;
                    }
                } catch (Exception e) {
                    // ready is empty, all flights were admitted
                    // nothing to do
                }
            }

            time = time + duration; // updates time

            for (int i = 0; i < airports.size(); i++) { // works ATCs
                airports.get(i).proces(duration);
            }

            if (!waiting.isEmpty()) {
                wait(duration);
            }

            if (!running.isEmpty()) {
                running.peek().run(duration);
                running.peek().setRuntime(running.peek().getRuntime() + duration);
                if (running.peek().isFinished()) {

                    running.peek().getOperations().remove(0); // finishes current operation and proceeds another
                    running.peek().setAge(true);
                    running.peek().setCount(running.peek().getCount() + 1);
                    running.peek().setRuntime(0);
                    
                    if (running.peek().getCount() == 22) {
                        finishedFlights.add(running.poll().getName());

                    } else if ((running.peek().getCount() == 4) || (running.peek().getCount() == 14)) {

                        running.peek().setMoment(time);
                        if (running.peek().getCount() == 4) { // sends the flight to landing atc
                            ATC atc = findAirport(running.peek().getLanding());
                            atc.getReady().add(running.poll());
                        } else {
                            ATC atc = findAirport(running.peek().getDeparting()); // sends the flight to landing atc
                            atc.getReady().add(running.poll());
                        }
                    } else {
                        running.peek().setMoment(time);
                        waiting.add(running.poll());
                    }
                } else {
                    if (running.peek().getRuntime() >= 30) {
                        running.peek().setAge(false);
                        running.peek().setMoment(time);
                        running.peek().setRuntime(0);
                        Flight temp = running.poll();
                        running.add(temp);
                    } else {
                        running.peek().setAge(true);
                    }
                }
            }

        }

    }

}