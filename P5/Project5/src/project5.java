import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


public class project5 {
    public static void main(String[] args) throws Exception {
        String text = args[0];
        PrintStream out;
        try {
            out = new PrintStream(new FileOutputStream(args[1], false));
            System.setOut(out);
        } catch (FileNotFoundException e) {
            // do nothing
        }
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        data = readFile(text);

        Graph graph = new Graph(new HashMap<>());
        HashSet<String> names = new HashSet<>();
        int numberOfCities = Integer.parseInt(data.get(0).get(0));

        Vertex kingsLanding = new Vertex("KL");
        Vertex source = new Vertex("source");
        names.add("source");
        names.add("KL");
        graph.addVertex(kingsLanding);
        graph.addVertex(source);

        int index = 0;
        for (String s : data.get(1)) {
            String name = "r" + index;
            int num = Integer.parseInt(s);
            Vertex v = new Vertex(name);
            graph.addVertex(v);
            names.add(name);
            graph.addEdge(source, v, num);
            index++;
        }

        for (int i = 2; i < 8; i++) {
            ArrayList<String> current = data.get(i);
            int num = i - 2;
            String startName = "r" + num;
            Vertex start = graph.vertexFinder(startName);

            for (int j = 1; j < current.size(); j += 2) {
                String name = current.get(j);

                if (!names.contains(name)) {
                    Vertex newV = new Vertex(name);
                    graph.addVertex(newV);
                    names.add(name);
                    Vertex dest = graph.vertexFinder(name);
                    int cap = Integer.parseInt(current.get(j + 1));
                    graph.addEdge(start, dest, cap);
                } else {
                    Vertex dest = graph.vertexFinder(name);
                    int cap = Integer.parseInt(current.get(j + 1));
                    graph.addEdge(start, dest, cap);
                }
            }
        }

        for (int i = 8; i < 8 + numberOfCities; i++) {
            ArrayList<String> current = data.get(i);
            String name = current.get(0);

            if (!names.contains(name)) {
                graph.addVertex(new Vertex(name));
                names.add(name);
            }
            Vertex start = graph.vertexFinder(name);

            for (int j = 1; j < current.size(); j += 2) {
                String destName = current.get(j);

                if (!names.contains(destName)) {
                    graph.addVertex(new Vertex(destName));
                    names.add(destName);
                }

                Vertex dest = graph.vertexFinder(destName);
                int road = Integer.parseInt(current.get(j + 1));
                graph.addEdge(start, dest, road);
            }
        }
        Graph residualGraph = new Graph(new HashMap<>(graph.getMap()));
        System.out.println(fordFulkerson(residualGraph, source, kingsLanding));
        minCut(graph, residualGraph, source);

    }

    private static boolean bfs(Graph graph, Vertex source, Vertex sink, HashMap<Vertex, Edge> parents) {
        HashSet<Vertex> visited = new HashSet<>();
        Queue<Vertex> q = new LinkedList<>();
        // HashMap<Vertex, Vertex> children = new HashMap<>();
        q.add(source);
        visited.add(source);
        parents.put(source, null);

        while (!q.isEmpty()) {
            Vertex current = q.poll();
            ArrayList<Edge> temp = new ArrayList<>();
            for (Edge e : graph.getMap().get(current)) {
                temp.add(e);
            }
            for (int i = 0; i < temp.size(); i++)

            {
                Edge e = temp.get(i);
                Vertex dest = e.getEnd();

                if (!visited.contains(dest) && e.getCapacity() > 0) {

                    if (dest.equals(sink)) {
                        parents.put(dest, e);
                        return true;
                    }
                    visited.add(dest);
                    q.add(dest);
                    parents.put(dest, e);

                }
            }
        }
        return false;

    }

    private static void dfs(Graph graph, Vertex v, Set<Vertex> reachableVertices) {
        reachableVertices.add(v);

        for (Edge e : graph.getMap().get(v)) {
            Vertex otherEnd = e.getEnd();
            if (e.getCapacity() > 0 && !reachableVertices.contains(otherEnd)) {
                dfs(graph, otherEnd, reachableVertices);
            }
        }
    }

    private static int fordFulkerson(Graph residualGraph, Vertex source, Vertex sink) {
        int maxFlow = 0;
        HashMap<Vertex, Edge> parents = new HashMap<>();

        while (bfs(residualGraph, source, sink, parents)) {
            int flow = Integer.MAX_VALUE;
            for (Vertex v = sink; v != source; v = parents.get(v).getStart()) {
                flow = Math.min(flow, parents.get(v).getCapacity());
            }
            for (Vertex v = sink; v != source; v = parents.get(v).getStart()) {

                Edge edge = parents.get(v);
                Vertex start = edge.getStart();
                Vertex end = edge.getEnd();

                int reverseIndex = residualGraph.edgeFinder(end, start);
                int index = residualGraph.edgeFinder(start, end);
                residualGraph.getMap().get(start).get(index).setCapacity(edge.getCapacity() - flow);
                if (reverseIndex == -1) {
                    Edge newE = new Edge(end, start, flow);
                    residualGraph.getMap().get(end).add(newE);
                } else {
                    residualGraph.getMap().get(v).get(reverseIndex).setCapacity(edge.getCapacity() + flow);
                }
            }
            maxFlow += flow;
        }
        return maxFlow;
    }

    public static void minCut(Graph graph, Graph rGraph, Vertex source) {
        Set<Vertex> reachableVertices = new HashSet<>();
        dfs(rGraph, source, reachableVertices);
        Set<Edge> minCut = new HashSet<>();

        for (Vertex v : graph.getMap().keySet()) {
            for (Edge e : graph.getMap().get(v)) {
                Vertex otherEnd = e.getEnd();
                if (reachableVertices.contains(v) && !reachableVertices.contains(otherEnd)) {
                    minCut.add(e);
                }
            }
        }

        for (Edge edge : minCut) {
            if (edge.getStart().getName().equals("source")) {
                System.out.println(edge.getEnd().getName());
            } else {
                System.out.println(edge.getStart().getName() + " " + edge.getEnd().getName());
            }
        }

    }

    private static ArrayList<ArrayList<String>> readFile(String text) throws IOException {
        BufferedReader ceren = new BufferedReader(new FileReader(text));
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        int index = 0;
        String line = "";

        while ((line = ceren.readLine()) != null) {
            ArrayList<String> temp = new ArrayList<>();

            for (String s : line.split(" ")) {
                temp.add(s);
            }
            data.add(index, temp);
            index++;
        }
        ceren.close();
        return data;
    }
}
