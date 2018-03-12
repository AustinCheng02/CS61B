import java.util.Observable;
import java.util.Stack;
/**
 *  @author Josh Hug
 */

public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        Stack<Integer> q = new Stack<>();
        distTo[0] = 0;
        marked[0] = true;
        int[] edge2 = new int[maze.V()];
        edge2[0] = 0;
        announce();
        q.push(0);
        while (!q.isEmpty()) {
            int v = q.pop();
            for (int nbor : maze.adj(v)) {
                if (marked[nbor] && edge2[v] != nbor && edge2[nbor] != v) {
                    edgeTo[nbor] = v;
                    announce();
                    edgeTo[edge2[nbor]] = nbor;
                    announce();
                    edgeTo[v] = edge2[v];
                    announce();
                    while (edge2[v] != edge2[nbor]) {
                        v = edge2[v];
                        edgeTo[v] = edge2[v];
                        announce();
                    }
                    return;
                }
                if (!marked[nbor]) {
                    edge2[nbor] = v;
                    distTo[nbor] = distTo[v] + 1;
                    marked[nbor] = true;
                    announce();
                    q.push(nbor);
                }
            }
        }
    }

    // Helper methods go here
}

