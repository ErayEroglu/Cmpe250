public class Node {

    String IP;
    Node left;
    Node right;

    public Node(String IP, Node left, Node right) {
        this.IP = IP;
        this.left = left;
        this.right = right;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String data) {
        this.IP = data;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

}