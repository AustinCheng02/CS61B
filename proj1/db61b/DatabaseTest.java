
package db61b;

import static org.junit.Assert.*;
import org.junit.Test;

/** Tests for DataBase
 * @author Daniel Kim
 * */

public class DatabaseTest {
    @Test
    public void testGet() {
    /* Run the unit tests in this file. */
        Database d = new Database();
        Table t = new Table(new String[] {"one", "three", "two"});
        String[] r = new String[] {"one", "three", "two"};
        t.add(r);
        d.put("right", t);
        assertEquals(t, d.get("right"));
    }
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(DatabaseTest.class));
    }
}
