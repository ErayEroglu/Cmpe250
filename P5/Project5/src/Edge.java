public class Edge {

    private Vertex start;
    private Vertex end;
    private int capacity;

    public Edge(Vertex start, Vertex end, int capacity) {
        this.start = start;
        this.end = end;
        this.capacity = capacity;
    }

    public Vertex getStart() {
        return start;
    }

    public void setStart(Vertex start) {
        this.start = start;
    }

    public Vertex getEnd() {
        return end;
    }

    public void setEnd(Vertex end) {
        this.end = end;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Edge [start=" + start + ", end=" + end + ", capacity=" + capacity + "]";
    }

}
