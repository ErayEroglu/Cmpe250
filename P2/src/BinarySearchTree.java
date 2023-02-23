import java.util.ArrayList;
import java.util.Collections;

public class BinarySearchTree {

    private Node root;

    public BinarySearchTree() {
        root = null;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public void printTree() {
        printTreeRec(root);
    }

    private void printTreeRec(Node root) {
        if (root != null) {
            printTreeRec(root.getLeft());
            System.out.print(root.getIP() + " ");
            printTreeRec(root.getRight());
        }

    }

    private String findMin(Node root) { // helper method to find min val in tree
        String temp = root.getIP();
        while (root.getLeft() != null) {
            temp = root.getLeft().getIP();
            root = root.getLeft();
        }
        return temp;
    }

    public ArrayList<String> nodeFinder(String target) { // helper to find specific node
        ArrayList<String> path = new ArrayList<>();
        nodeFinderRec(root, target, path);
        path.add(target);
        return path;
    }

    private Node nodeFinderRec(Node root, String target, ArrayList<String> path) {
        if (root == null) {
            return null;
        } else {
            int result = target.compareTo(root.getIP());
            if (result < 0) {
                path.add(root.getIP());
                root.setLeft(nodeFinderRec(root.getLeft(), target, path));
            } else if (result > 0) {
                path.add(root.getIP());
                root.setRight(nodeFinderRec(root.getRight(), target, path));
            }
            return root;
        }
    }

    private ArrayList<String> slicer(ArrayList<String> lst, int first, int last) { // arraylist slicer
        ArrayList<String> newlist = new ArrayList<>();
        for (int i = first; i < last + 1; i++) {
            newlist.add(lst.get(i));
        }
        return newlist;
    }

    public void add(String data) {
        root = addRec(root, data);
    }

    private Node addRec(Node root, String data) {
        if (root == null) {
            root = new Node(data, null, null);
            return root;
        }
        System.out.println(root.getIP() + ": New node being added with IP:" + data);
        int result = data.compareTo(root.getIP());

        if (result < 0) {
            root.setLeft(addRec(root.getLeft(), data));
        } else if (result > 0) {
            root.setRight(addRec(root.getRight(), data));
        }
        return root;
    }

    public void delete(String data) {
        Node temp = new Node(null, null, null);
        root = deleteRec(root, data, temp, true);
    }

    private Node deleteRec(Node root, String data, Node temp, boolean flag) {
        if (root == null) { // tree is empty
            return null;
        }
        int result = data.compareTo(root.getIP());
        if (result < 0) {
            temp = root;
            root.setLeft(deleteRec(root.getLeft(), data, temp, flag));
        } else if (result > 0) {
            temp = root;
            root.setRight(deleteRec(root.getRight(), data, temp, flag));
        } else if (root.getRight() != null && root.getLeft() != null) { // node with two children
            root.setIP(findMin(root.getRight()));
            String replaced = root.getIP();
            System.out.println(temp.getIP() + ": Non Leaf Node Deleted; removed: " + data + " replaced: " + replaced);
            root.setRight(deleteRec(root.getRight(), root.getIP(), temp, false));
            return root;
        } else { // node with one or no children
            if (root.getLeft() == null && root.getRight() == null) { // leaf node
                if (flag) {
                    System.out.println(temp.getIP() + ": Leaf Node Deleted: " + root.getIP());
                }
                root = null;
            } else { // one child
                if (flag) {
                    System.out.println(temp.getIP() + ": Node with single child Deleted: " + root.getIP());
                }
                root = (root.getLeft() != null) ? root.getLeft() : root.getRight();
            }
        }
        return root;

    }

    public ArrayList<String> sendMessage(String sender, String reciever) {
        ArrayList<String> pathToSender = nodeFinder(sender);
        ArrayList<String> pathToReciever = nodeFinder(reciever);
        ArrayList<String> path = new ArrayList<String>();
        int intersection = 0;
        for (int i = Math.min(pathToReciever.size(), pathToSender.size()) - 1; i >= 0; i--) {
            if (pathToReciever.get(i) == pathToSender.get(i)) {
                intersection = i;
                break;
            }
        }
        Collections.reverse(pathToSender);
        path = slicer(pathToSender, 0, pathToSender.size() - intersection - 1);
        path.addAll(slicer(pathToReciever, intersection + 1, pathToReciever.size() - 1));
        System.out.println(sender + ": Sending message to: " + reciever);
        boolean flag;
        try {
            if (path.get(2).equals(sender)) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            flag = false;
        }
        for (int i = 0; i < path.size(); i++) {
            if (flag) {
                i = 1;
                flag = false;
                continue;
            }
            if (path.get(i + 1).equals(reciever)) {
                System.out.println(reciever + ": Received message from: " + sender);
                break;
            }
            System.out.println(path.get(i + 1) + ": Transmission from: " + path.get(i) + " receiver: " + reciever
                    + " sender:" + sender);
        }
        return path;
    }

}