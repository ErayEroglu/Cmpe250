import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Project1 {

    public static void main(String[] args) {
        FactoryImpl myFactory = new FactoryImpl(null, null, 0);
        ArrayList<String> commands = readFile(args[0]);
        PrintStream out;
        try {
            out = new PrintStream(new FileOutputStream(args[1], false));
            System.setOut(out);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        for (int i = 0; i < commands.size(); i++) {
            String info = commands.get(i);
            String[] order = info.split(" ");
            if (order[0].equals("P")) {
                System.out.println(myFactory.printList());
            } else if (order[0].equals("AF")) {
                int id = Integer.valueOf(order[1]);
                int val = Integer.valueOf(order[2]);
                Product newP = new Product(id, val);
                myFactory.addFirst(newP);
            } else if (order[0].equals("AL")) {
                int id = Integer.valueOf(order[1]);
                int val = Integer.valueOf(order[2]);
                Product newP = new Product(id, val);
                myFactory.addLast(newP);
            } else if (order[0].equals("RF")) {
                try {
                    Product p = myFactory.removeFirst();
                    System.out.println(p.toString());
                } catch (NoSuchElementException exception) {
                    System.out.println("Factory is empty.");
                }
            } else if (order[0].equals("RL")) {
                try {
                    Product p = myFactory.removeLast();
                    System.out.println(p.toString());
                } catch (NoSuchElementException exception) {
                    System.out.println("Factory is empty.");
                }
            } else if (order[0].equals("F")) {
                try {
                    int id = Integer.valueOf(order[1]);
                    Product p = myFactory.find(id);
                    System.out.println(p.toString());
                } catch (NoSuchElementException exception) {
                    System.out.println("Product not found.");
                }
            } else if (order[0].equals("U")) {
                try {
                    int id = Integer.valueOf(order[1]);
                    int val = Integer.valueOf(order[2]);
                    Product p = myFactory.update(id, val);
                    System.out.println(p.toString());
                } catch (NoSuchElementException exception) {
                    System.out.println("Product not found.");
                }
            } else if (order[0].equals("G")) {
                try {
                    int index = Integer.valueOf(order[1]);
                    System.out.println(myFactory.get(index));
                } catch (IndexOutOfBoundsException exception) {
                    System.out.println("Index out of bounds.");
                }
            } else if (order[0].equals("A")) {
                try {
                    int index = Integer.valueOf(order[1]);
                    int id = Integer.valueOf(order[2]);
                    int val = Integer.valueOf(order[3]);
                    Product newP = new Product(id, val);
                    myFactory.add(index, newP);
                } catch (IndexOutOfBoundsException exception) {
                    System.out.println("Index out of bounds.");
                }
            } else if (order[0].equals("RI")) {
                try {
                    int index = Integer.valueOf(order[1]);
                    Product p = myFactory.removeIndex(index);
                    System.out.println(p.toString());
                } catch (IndexOutOfBoundsException exception) {
                    System.out.println("Index out of bounds.");
                }
            } else if (order[0].equals("RP")) {
                try {
                    int val = Integer.valueOf(order[1]);
                    Product p = myFactory.removeProduct(val);
                    System.out.println(p.toString());
                } catch (NoSuchElementException exception) {
                    System.out.println("Product not found.");
                }
            } else if (order[0].equals("FD")) {
                int num = myFactory.filterDuplicates();
                System.out.println(num);
            } else if (order[0].equals("R")) {
                myFactory.reverse();
                System.out.println(myFactory.printList());
            }
        }
    }

    private static ArrayList<String> readFile(String text) {
        ArrayList<String> data = new ArrayList<String>();
        try {
            File myFile = new File(text);
            Scanner reader = new Scanner(myFile);
            while (reader.hasNextLine()) {
                String info = reader.nextLine();
                data.add(info);
            }
            reader.close();
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("An error occured");
            e.printStackTrace();
            return null;
        }

    }

}