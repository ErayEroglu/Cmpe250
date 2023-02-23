import java.util.ArrayList;
import java.util.Collections;

public class AVLTree {

    private AVLNode root;

    public AVLNode getRoot() {
        return root;
    }

    public void setRoot(AVLNode root) {
        this.root = root;
    }

    public AVLTree() {
        root = null;
    }

    private int height(AVLNode node) {
        return node != null ? node.getHeight() : 0;
    }

    private void heightUpdater(AVLNode node) {
        int leftHeight = height(node.getLeft());
        int rightHeight = height(node.getRight());
        node.setHeight(Math.max(leftHeight, rightHeight) + 1);
    }

    private int balanceChecker(AVLNode node) {
        if (node == null)
            return 0;
        return height(node.getLeft()) - height(node.getRight());
    }

    private AVLNode rightRotation(AVLNode node) {
        AVLNode leftChild = node.getLeft();
        AVLNode temp = leftChild.getRight();

        leftChild.setRight(node);
        node.setLeft(temp);

        heightUpdater(node);
        heightUpdater(leftChild);

        return leftChild;
    }

    private AVLNode leftRotation(AVLNode node) {
        AVLNode rightChild = node.getRight();
        AVLNode temp = rightChild.getLeft();

        rightChild.setLeft(node);
        node.setRight(temp);

        heightUpdater(node);
        heightUpdater(rightChild);

        return rightChild;
    }

    private AVLNode rebalance(AVLNode node) {
        int balanceFactor = balanceChecker(node);
        // if it is left heavy
        if (balanceFactor > 1) {
            if (balanceChecker(node.getLeft()) >= 0) { // right rotation required
                System.out.println("Rebalancing: right rotation");
                return rightRotation(node);
            } else { // first left then right rotation
                System.out.println("Rebalancing: left-right rotation");
                node.setLeft(leftRotation(node.getLeft()));
                return rightRotation(node);
            }
        } else if (balanceFactor < -1) {
            if (balanceChecker(node.getRight()) <= 0) { // left rotation required
                System.out.println("Rebalancing: left rotation");
                return leftRotation(node);
            } else { // first right then left
                System.out.println("Rebalancing: right-left rotation");
                node.setRight(rightRotation(node.getRight()));
                return leftRotation(node);
            }
        }
        return node;

    }

    private String findMin(AVLNode root) { // helper method to find min val in tree
        String temp = root.getIP();
        while (root.getLeft() != null) {
            temp = root.getLeft().getIP();
            root = root.getLeft();
        }
        return temp;
    }

    public ArrayList<String> AVLNodeFinder(String target) { // helper to find specific node
        ArrayList<String> path = new ArrayList<>();
        AVLNodeFinderRec(root, target, path);
        path.add(target);
        return path;
    }

    private AVLNode AVLNodeFinderRec(AVLNode root, String target, ArrayList<String> path) {
        if (root == null) {
            return null;
        } else {
            int result = target.compareTo(root.getIP());
            if (result < 0) {
                path.add(root.getIP());
                root.setLeft(AVLNodeFinderRec(root.getLeft(), target, path));
            } else if (result > 0) {
                path.add(root.getIP());
                root.setRight(AVLNodeFinderRec(root.getRight(), target, path));
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

    private AVLNode addRec(AVLNode root, String data) { // similar to bst, balancing is required
        if (root == null) {
            root = new AVLNode(data, null, null, 1);
            return root;
        }
        System.out.println(root.getIP() + ": New node being added with IP:" + data);
        int result = data.compareTo(root.getIP());
        if (result < 0) {
            root.setLeft(addRec(root.getLeft(), data));
        } else if (result > 0) {
            root.setRight(addRec(root.getRight(), data));
        }
        heightUpdater(root);
        return rebalance(root);
    }

    public void delete(String data) {
        if (data.equals(root.getIP())) {
            return;
        }
        AVLNode temp = new AVLNode(null, null, null, 0);
        root = deleteRec(root, data, temp, true);
    }

    private AVLNode deleteRec(AVLNode root, String data, AVLNode temp, boolean flag) {
        // similar to bst, requires balancing
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
            // return root;
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
        if (root == null) {
            return null;
        }

        heightUpdater(root);
        return rebalance(root);

    }

    public ArrayList<String> sendMessage(String sender, String reciever) {
        ArrayList<String> pathToSender = AVLNodeFinder(sender);
        ArrayList<String> pathToReciever = AVLNodeFinder(reciever);
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