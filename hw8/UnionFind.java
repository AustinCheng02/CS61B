import java.util.Arrays;

/** A partition of a set of contiguous integers that allows (a) finding whether
 *  two integers are in the same partition set and (b) replacing two partitions
 *  with their union.  At any given time, for a structure partitioning
 *  the integers 1-N, each partition is represented by a unique member of that
 *  partition, called its representative.
 *  @author Daniel Kim
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */

    private int[] structure;
    public UnionFind(int N) {
        structure = new int[N];

        for (int i = 1; i <= N; i++) {
            structure[i] = i;
        }
    }

    /** Return the representative of the partition currently containing V.
     *  Assumes V is contained in one of the partitions.  */
    public int find(int v) {
        return structure[v];
    }

    /** Return true iff U and V are in the same partition. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single partition, returning its representative. */
    public int union(int u, int v) {
        int uFind = find(u);
        int vFind = find(v);

        for (int i = 1; i <= structure.length; i++) {
            if (find(i) == uFind) {
                structure[i] = vFind;
            }
        }
        return vFind;
    }

}
