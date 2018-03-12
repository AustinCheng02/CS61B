import java.util.Observable;
import java.util.ArrayDeque;
/**
 *  @author Josh Hug
 */

public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = m.xyTo1D(sourceX, sourceY);
        t = m.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        // Add more variables here!
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        distTo[s] = 0;
        marked[s] = true;
        announce();
        q.add(s);
        while (!q.isEmpty()) {
            int v = q.remove();
            for (int nbor : maze.adj(v)) {
                if (!marked[nbor]) {
                    edgeTo[nbor] = v;
                    distTo[nbor] = distTo[v] + 1;
                    marked[nbor] = true;
                    announce();
                    q.add(nbor);
                }
            }
        }
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
    }


    @Override
    public void solve() {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        distTo[s] = 0;
        marked[s] = true;
        announce();
        q.add(s);
        while (!q.isEmpty()) {
            int v = q.remove();
            marked[v] = true;
            announce();
            if (v == t) {
                return;
            }
            for (int nbor : maze.adj(v)) {
                if (!marked[nbor]) {
                    edgeTo[nbor] = v;
                    distTo[nbor] = distTo[v] + 1;
                    q.add(nbor);
                }
            }
        }
        // bfs();
    }
}

