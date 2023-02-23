public class AVLNode {

    private String IP;
    private AVLNode left;
    private AVLNode right;
    private int height;

    public AVLNode(String IP, AVLNode left, AVLNode right, int height) {
        this.IP = IP;
        this.left = left;
        this.right = right;
        this.height = height;
    }
    
    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public AVLNode getLeft() {
        return left;
    }

    public void setLeft(AVLNode avlNode) {
        this.left = avlNode;
    }

    public AVLNode getRight() {
        return right;
    }

    public void setRight(AVLNode avlNode) {
        this.right = avlNode;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
