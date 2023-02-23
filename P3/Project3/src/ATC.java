import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class ATC {

    private String code;
    private PriorityQueue<Flight> ready;
    private ArrayList<Flight> waiting;
    private ArrayList<Flight> transition;

    public ATC(String code, PriorityQueue<Flight> ready, ArrayList<Flight> waiting, ArrayList<Flight> transition) {
        this.code = code;
        this.ready = ready;
        this.waiting = waiting;
        this.transition = transition;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public PriorityQueue<Flight> getReady() {
        return ready;
    }

    public void setReady(PriorityQueue<Flight> ready) {
        this.ready = ready;
    }

    public ArrayList<Flight> getWaiting() {
        return waiting;
    }

    public void setWaiting(ArrayList<Flight> waiting) {
        this.waiting = waiting;
    }

    public ArrayList<Flight> getTransition() {
        return transition;
    }

    public void setTransition(ArrayList<Flight> transition) {
        this.transition = transition;
    }

    @Override
    public String toString() {
        return "ATC [code=" + code + ", ready=" + ready + ", waiting=" + waiting + ", transition=" + transition + "]";
    }

    private int hash(String code) {
        int sum = 0;
        char[] arr = code.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            sum = (int) (sum + ((int) arr[i] * Math.pow(31, i)));

        }
        return sum % 1000;
    }

    public int findIndex(String code) {
        return hash(code);
    }

    public String codeGenerater(String code, int num) {
        String name = code;

        if (num < 10) {
            name = name + "00" + String.valueOf(num);
        } else if (num < 100) {
            name = name + "0" + String.valueOf(num);
        } else {
            name = name + String.valueOf(num);
        }
        return name;
    }

    public int findDurationATC() {
        int num = Integer.MAX_VALUE;
        if (ready.isEmpty() && waiting.isEmpty()) {
            return -1;
        } else if ((!ready.isEmpty()) && (!waiting.isEmpty())) {
            for (Flight f : waiting) {
                if (f.getOperations().get(0) < num) {
                    num = f.getOperations().get(0);
                }
            }
            return Math.min(num, ready.peek().getOperations().get(0));
        } else if ((ready.isEmpty()) && (!waiting.isEmpty())) {
            for (Flight f : waiting) {
                if (f.getOperations().get(0) < num) {
                    num = f.getOperations().get(0);
                }
            }
            return num;
        } else {
            return ready.peek().getOperations().get(0);
        }
    }

    private void waitATC(int duration) {
        ArrayList<Integer> removedIndex = new ArrayList<>(); 
        for (int i = 0; i < waiting.size(); i++) {
           
            waiting.get(i).run(duration);
            waiting.get(i).setRuntime(waiting.get(i).getRuntime() + duration);
            if (waiting.get(i).isFinished()) {
                
                waiting.get(i).setMoment(waiting.get(i).getMoment() + waiting.get(i).getRuntime());
                waiting.get(i).setRuntime(0);
                waiting.get(i).getOperations().remove(0);
                waiting.get(i).setCount(waiting.get(i).getCount() + 1);
                transition.add(waiting.get(i));
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

    public void proces(int duration) {
        if ((ready.isEmpty()) && (waiting.isEmpty()) && (transition.isEmpty())) {
            return;
        }
        if (!waiting.isEmpty()) {
            waitATC(duration);
        }
        if (!ready.isEmpty()) {
            ready.peek().run(duration);
            ready.peek().setRuntime(ready.peek().getRuntime() + duration);
            if (ready.peek().isFinished()) {

                // ready.peek().setMoment(ready.peek().getMoment() + ready.peek().getRuntime());
                ready.peek().setMoment(ready.peek().getControlCenter().getTime());
                ready.peek().setRuntime(0);
                ready.peek().getOperations().remove(0);
                ready.peek().setAge(true);
                ready.peek().setCount(ready.peek().getCount() + 1);
                
                if ((ready.peek().getCount() == 11) || (ready.peek().getCount() == 21)) {
                    ready.peek().getControlCenter().getBoundary().add(ready.poll());  // time to send it back to ACC
                } else {
                    waiting.add(ready.poll());
                }
            }
        }
    }

}
