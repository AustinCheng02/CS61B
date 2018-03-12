import java.util.*;

public class Graph {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    // Initialize a graph with the given number of vertices and no edges.
    @SuppressWarnings("unchecked")
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    // Add to the graph a directed edge from vertex v1 to vertex v2,
    // with the given edge information. If the edge already exists,
    // replaces the current edge with a new edge with edgeInfo.
    public void addEdge(int v1, int v2, int edgeWeight) {
        if (!isAdjacent(v1, v2)) {
            LinkedList<Edge> v1Neighbors = adjLists[v1];
            v1Neighbors.add(new Edge(v1, v2, edgeWeight));
        } else {
            LinkedList<Edge> v1Neighbors = adjLists[v1];
            for (Edge e : v1Neighbors) {
                if (e.to() == v2) {
                    e.edgeWeight = edgeWeight;
                }
            }
        }
    }

    // Add to the graph an undirected edge from vertex v1 to vertex v2,
    // with the given edge information. If the edge already exists,
    // replaces the current edge with a new edge with edgeInfo.
    public void addUndirectedEdge(int v1, int v2, int edgeWeight) {
        addEdge(v1, v2, edgeWeight);
        addEdge(v2, v1, edgeWeight);
    }

    // Return true if there is an edge from vertex "from" to vertex "to";
    // return false otherwise.
    public boolean isAdjacent(int from, int to) {
        for (Edge e : adjLists[from]) {
            if (e.to() == to) {
                return true;
            }
        }
        return false;
    }

    // Returns a list of all the neighboring  vertices 'u'
    // such that the edge (VERTEX, 'u') exists in this graph.
    public List<Integer> neighbors(int vertex) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        for (Edge e : adjLists[vertex]) {
            neighbors.add(e.to());
        }
        return neighbors;
    }

    // Runs Dijkstra's algorithm starting from vertex 'startVertex' and returns
    // an integer array consisting of the shortest distances from 'startVertex'
    // to all other vertices.
    public int[] dijkstras(int startVertex) {
        PriorityQueue<Vertex> q = new PriorityQueue<>(11, new comp());
        Vertex[] lis = new Vertex[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            lis[i] = new Vertex(i, Integer.MAX_VALUE, null);
        }
        lis[startVertex].distUp(0);
        q.add(lis[startVertex]);

        while (!q.isEmpty()) {
            Vertex v = q.poll();
            for (int nbor : neighbors(v.index())) {
                if (v.back() != lis[nbor]) {
                    if (v.dist() + getEdge(v.index, nbor).info() < lis[nbor].dist()) {
                        lis[nbor].distUp(v.dist() + getEdge(v.index, nbor).info());
                        lis[nbor].backUp(v);
                        q.remove(lis[nbor]);
                        q.add(lis[nbor]);
                    }
                }
            }
        }

        int[] ret = new int[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            ret[i] = lis[i].dist();
        }
        // TODO: Your code here!
        return ret;
    }

    static class comp implements Comparator<Vertex> {
        public int compare(Vertex v0, Vertex v1) {
            return Integer.compare(v0.dist(), v1.dist());
        }
    }

    // Returns the Edge object corresponding to the listed vertices, v1 and v2.
    // You may find this helpful to implement!
    private Edge getEdge(int v1, int v2) {
        LinkedList<Edge> v1Neighbors = adjLists[v1];
        for (Edge e : v1Neighbors) {
            if (e.to() == v2) {
                return e;
            }
        }
        return null;
    }

    private class Edge {

        private int from;
        private int to;
        private int edgeWeight;

        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.edgeWeight = weight;
        }

        public int to() {
            return to;
        }

        public int info() {
            return edgeWeight;
        }

        public String toString() {
            return "(" + from + "," + to + ",dist=" + edgeWeight + ")";
        }

    }

    public class Vertex {

        private int index;
        private int dist;
        private Vertex back;

        public Vertex(int index, int dist, Vertex back) {
            this.index = index;
            this.dist = dist;
            this.back = back;
        }

        public void distUp(int updte) {dist = updte;}
        public void backUp(Vertex updte) {back = updte;}

        public Vertex back() {return back;}
        public int index() {return index;}
        public int dist() {return dist;}
    }

    public static void main(String[] args) {
        // Put some tests here!
        Graph g1 = new Graph(5);
        g1.addEdge(0, 1, 1);
        g1.addEdge(0, 2, 1);
        g1.addEdge(0, 4, 1);
        g1.addEdge(1, 2, 1);
        g1.addEdge(2, 0, 1);
        g1.addEdge(2, 3, 1);
        g1.addEdge(4, 3, 1);
        int[] v0 = g1.dijkstras(0);
        for (int i = 0; i < g1.vertexCount; i++) {
            System.out.print(v0[i]);
        }
        System.out.println();
        int[] v1 = g1.dijkstras(1);
        for (int i = 0; i < g1.vertexCount; i++) {
            System.out.print(v1[i]);
        }


        Graph g2 = new Graph(5);
        g2.addEdge(0, 1, 1);
        g2.addEdge(0, 2, 1);
        g2.addEdge(0, 4, 1);
        g2.addEdge(1, 2, 1);
        g2.addEdge(2, 3, 1);
        g2.addEdge(4, 3, 1);
        int[] v3 = g1.dijkstras(0);
        System.out.println();
        for (int i = 0; i < g1.vertexCount; i++) {
            System.out.print(v3[i]);
        }
        System.out.println();
        int[] v4 = g1.dijkstras(1);
        for (int i = 0; i < g1.vertexCount; i++) {
            System.out.print(v4[i]);
        }
    }
}
