import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BinarySearchTree myBST = new BinarySearchTree();
        AVLTree myAVL = new AVLTree();
        PrintStream out;
        ArrayList<String> inputs = readFile(args[0]);
        myBST.setRoot(new Node(inputs.get(0), null, null));
        myAVL.setRoot(new AVLNode(inputs.get(0), null, null, 1));
        for (int j = 1; j < 3; j++) {
            try {
                if (j == 1) {
                    out = new PrintStream(new FileOutputStream(args[1] + "_bst.txt", false));
                    System.setOut(out);
                } else {
                    out = new PrintStream(new FileOutputStream(args[1] + "_avl.txt", false));
                    System.setOut(out);
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            for (int i = 1; i < inputs.size(); i++) {
                String command = inputs.get(i);
                String[] action = command.split(" ");
                String info = action[1];
                switch (action[0]) {
                    case "ADDNODE":
                        if (j == 1)
                            myBST.add(info);
                        else
                            myAVL.add(info);

                        break;
                    case "DELETE":
                        if (j == 1)
                            myBST.delete(info);
                        else
                            myAVL.delete(info);
                        break;
                    case "SEND":
                        String destination = action[2];
                        if (j == 1)
                            myBST.sendMessage(info, destination);
                        else
                            myAVL.sendMessage(info, destination);

                }

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