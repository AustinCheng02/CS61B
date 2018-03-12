import org.junit.Test;
import static org.junit.Assert.*;

/** A test that verifies the correctness of catenate and remove methods.
 *  @author Daniel Kim
 */

public class ArraysTest {
    /**  A test that verifies the correctness of catenate and remove methods.
     * catenateTest method tests the catenate method in Arrays.java.
     * removeTest method tests the remove method in Arrays.java.
     */
    @Test
    public void catenateTest() {
        int[] Array1 = {1, 3, 6, 3, 4, 6};
        int[] Array2 = {0, 5, 6, 5, 2, 6};
        int[] Array3 = {};
        int[] expected1 = {0, 5, 6, 5, 2, 6, 1, 3, 6, 3, 4, 6};
        assertArrayEquals(expected1, Arrays.catenate(Array2, Array1));
        assertArrayEquals(Array1, Arrays.catenate(Array1, Array3));
    }

    /** a test that verifies the remove function which deleted elements from a list.
     *
     */
    @Test
    public void removeTest() {
        int[] Array1 = {1, 3, 6, 3, 4, 6};
        int[] Array2 = {0, 5, 6, 5, 2, 6};
        int[] Array3 = {0, 5, 6, 5, 2, 6, 1, 3, 6, 3, 4, 6};
        int[] EArray1 = {1, 4, 6};
        int[] EArray2 = {};
        int[] EArray3 = {0, 5, 6, 5, 2, 6, 1, 3, 6, 3, 4, 6};
        assertArrayEquals(EArray1, Arrays.remove(Array1, 1, 3));
        assertArrayEquals(EArray2, Arrays.remove(Array2, 0, 6));
        assertArrayEquals(EArray3, Arrays.remove(Array3, 11, 0));

    }

    /** a test that verifies the naturalruns. creates a double array for a list
     * in increasing order.
     */
    @Test
    public void naturalTest() {
        int[] A = {1, 3, 7, 5, 4, 6, 9, 10},
                B = {};
        int[][] D = new int[0][];
        int[][] C = new int[][] {{1,3,7},{5},{4,6,9,10}};

        assertEquals(Utils.toString(Arrays.naturalRuns(A)), Utils.toString(C));
        assertEquals(Utils.toString(Arrays.naturalRuns(B)), Utils.toString(D));

    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
