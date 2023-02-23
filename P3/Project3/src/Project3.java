import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Project3 {

    public static void main(String[] args) {
        String[][] text = readFile(args[0]);
        PrintStream out;
        try {
            out = new PrintStream(new FileOutputStream(args[1], false));
            System.setOut(out);
        } catch (FileNotFoundException e) {
            // do nothing
        }
        ArrayList<ACC> accList = new ArrayList<>();
        ArrayList<Flight> flightList = new ArrayList<>();

        for (int i = 0; i < Integer.valueOf(text[0][0]); i++) { // creates ACCs
            Hashtable<Integer, String> table = new Hashtable<Integer, String>(1000, 1);
            PriorityQueue<Flight> ready = new PriorityQueue<>(11, new FlightComparator(true));
            PriorityQueue<Flight> running = new PriorityQueue<>(11, new FlightComparator(true));
            ArrayList<Flight> waiting = new ArrayList<>();
            ArrayList<ATC> airports = new ArrayList<>();
            ArrayList<Flight> boundary = new ArrayList<>();

            accList.add(new ACC(text[i + 1][0], table, ready, 0, waiting, running, airports, boundary)); // keeps ACCs in a list

            ArrayList<ATC> tempATC = new ArrayList<>(); // creates a temporary ACT list to creates ACTs of current ACC
            for (int j = 1; j < text[i + 1].length; j++) { // creates ATCs and sets them into hashtable
                PriorityQueue<Flight> readyATC = new PriorityQueue<>(11, new FlightComparator(false));
                ArrayList<Flight> waitingATC = new ArrayList<>();
                ArrayList<Flight> transition = new ArrayList<>();
                tempATC.add(new ATC(text[i + 1][j], readyATC, waitingATC,transition));
                accList.get(i).addToACC(tempATC.get(j - 1));
            }
        }
        for (int i = Integer.valueOf(text[0][0]) + 1; i < text.length; i++) {
            if (text[i] == null)
                break;
            ArrayList<Integer> lst = new ArrayList<>();
            for (int j = 0; j < 21; j++) {
                lst.add(Integer.valueOf(text[i][5 + j]));
            }
            ACC belongedACC = findACC(text[i][2], accList);
            flightList // creates flights and keep them in a list
                    .add(new Flight(text[i][1], Integer.valueOf(text[i][0]), belongedACC, text[i][3], text[i][4],
                            lst, true, 1, Integer.valueOf(text[i][0]), 0));
        }

        for (int i = 0; i < flightList.size(); i++) { // adding fligths to ACCs
            flightList.get(i).getControlCenter().getReady().add(flightList.get(i));
        }

        for (ACC acc : accList) {  // calls all ACCs and do the operations
            int num = numberOfFlights(acc, flightList);
            acc.operate(num);
            System.out.println(acc.getName() + " " + acc.getTime() + " " + printingTable(acc.getTable()));
        }

    }

    private static String printingTable(Hashtable<Integer,String> ht) {
        String str = "";
        for (String s : ht.values()) {
            str = str + " " + s;
        }
        return str.strip();
    }

    private static int numberOfFlights(ACC acc, ArrayList<Flight> lst) {
        int num = 0;
        for (Flight f : lst) {
            if (f.getControlCenter().getName().equals(acc.getName())) {
                num++;
            }
        }
        return num;
    }

    private static ACC findACC(String str, ArrayList<ACC> lst) {
        for (ACC acc : lst) {
            if (acc.getName().equals(str)) {
                return acc;
            }
        }
        return null;
    }

    private static String[][] readFile(String text) {
        int index = 0;
        String[][] data = new String[40][];

        try {
            File myFile = new File(text);
            Scanner reader = new Scanner(myFile);

            while (reader.hasNextLine()) {
                String info = reader.nextLine();
                if (index > data.length - 1)
                    data = resize(data);
                data[index] = info.split(" ");
                index++;
            }
            reader.close();
            return data;

        } catch (FileNotFoundException e) {
            System.out.println("An error occured");
            e.printStackTrace();
            return null;
        }

    }

    private static String[][] resize(String[][] arr) {
        String[][] temp = new String[2 * arr.length][];
        for (int i = 0; i < arr.length; i++) {
            temp[i] = arr[i];
        }
        return temp;
    }
}
