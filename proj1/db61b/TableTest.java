package db61b;

import static org.junit.Assert.*;
import org.junit.Test;

/** Tests for Table
 * @author Daniel Kim
 * */
public class TableTest {

    @Test
    public void testColumnSize() {
    /* Run the Column size tests in this file. */
        Table t = new Table(new String[] {"My", "name", "Daniel"});
        assertEquals(3, t.columns());
        Table td = new Table(new String[] {"Create", "a", "new", "test"});
        assertEquals(4, td.columns());
    }

    @Test
    public void testColumnGet() {
    /* Run the get Column tests in this file. */
        Table t = new Table(new String[] {"laha", "hoho", "hehe"});
        assertEquals("hehe", t.getTitle(2));
    }

    @Test
    public void testFindColumn() {
    /* Run the Find Column tests in this file. */
        Table t = new Table(new String[] {"Cool", "Test", "yolo"});
        assertEquals(2, t.findColumn("yolo"));
        assertEquals(-1, t.findColumn("naha"));
    }



    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(TableTest.class));
    }
}
