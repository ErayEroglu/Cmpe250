import java.util.HashMap;
import java.util.LinkedList;

public class Graph {

    private HashMap<Vertex, LinkedList<Edge>> map = new HashMap<>();

    public Graph(HashMap<Vertex, LinkedList<Edge>> map) {
        this.map = map;
    }

    public HashMap<Vertex, LinkedList<Edge>> getMap() {
        return map;
    }

    public void setMap(HashMap<Vertex, LinkedList<Edge>> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Graph [map=" + map + "]";
    }

    public void addVertex(Vertex v) {
        map.put(v, new LinkedList<>());
    }

    public void addEdge(Vertex start, Vertex end, int capacity) {
        Edge e = new Edge(start, end, capacity);
        map.get(start).add(e);
    }

    public Vertex vertexFinder(String s) {
        for (Vertex v : map.keySet()) {
            if (v.getName().equals(s)) {
                return v;
            }
        }
        return null;
    }

    public int edgeFinder(Vertex start, Vertex end) {
        LinkedList<Edge> lst = map.get(start);
        int counter = 0;
        for (Edge edge : lst) {
            if (edge.getEnd().getName().equals(end.getName())) {
                return counter;
            }
            counter++;
        }
        return -1;
    }
    
}
