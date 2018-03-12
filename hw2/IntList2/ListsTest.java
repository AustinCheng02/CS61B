import org.junit.Test;
import static org.junit.Assert.*;

/** Tests natural runs
 *
 *  @author Daniel Kim
 */

public class ListsTest {
    /** Tests naturalruns from Lists.java
     */
    @Test
    public void testList() {
        IntList2 list1 = IntList2.list(IntList.list(1, 3, 4), IntList.list(4, 5, 6), IntList.list(4, 5), IntList.list(2));
        IntList2 list2 = IntList2.list(IntList.list(5), IntList.list(4), IntList.list(3), IntList.list(2));
        IntList2 list3 = IntList2.list(IntList.list(4), IntList.list(4), IntList.list(4), IntList.list(4));

        IntList list10 = IntList.list(1,3,4,4,5,6,4,5,2);
        IntList list20 = IntList.list(5,4,3,2);
        IntList list30 = IntList.list(4,4,4,4);
        assertEquals(list1.toString(), Lists.naturalRuns(list10).toString());
        assertEquals(list2.toString(), Lists.naturalRuns(list20).toString());
        assertEquals(list3.toString(), Lists.naturalRuns(list30).toString());
    }
    // It might initially seem daunting to try to set up
    // Intlist2 expected.
    //
    // There is an easy way to get the IntList2 that you want in just
    // few lines of code! Make note of the IntList2.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
