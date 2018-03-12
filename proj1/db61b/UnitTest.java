package db61b;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;


/** Tests Integrative.
 *  Tables and selection method
 *  read table
 *  @author Daniel Kim
 *  */


public class UnitTest {

    @Test
    public void testTable() {
        Table table = Table.readTable("enrolled");
        assertEquals(3, table.columns());
        String[] titles = new String[] {"SID", "CCN", "Grade"};
        for (int i = 0; i < 3; i++) {
            assertEquals(titles[i], table.getTitle(i));
            assertEquals(i, table.findColumn(titles[i]));
        }
        assertEquals(-1, table.findColumn("non_title"));
        assertEquals(19, table.size());
        Table empty = new Table(titles);
        assertEquals(0, empty.size());
    }

    @Test
    public void selectTest() {
        Table students = Table.readTable("students");
        List<String> columnTitles = new ArrayList<String>();
        columnTitles.add("SID");
        columnTitles.add("Firstname");
        columnTitles.add("Lastname");
        List<Column> columns = new ArrayList<Column>();
        for (String name : columnTitles) {
            columns.add(new Column(name, students));
        }
        String[] first = new String[columnTitles.size()];
        List<Condition> conditions = new ArrayList<Condition>();

        for (int i = 0; i < columnTitles.size(); i++) {
            first[i] = students.select(columnTitles, conditions).get(0, i);

        }
        assertEquals("101 Jason Knowles", String.join(" ", first));
        conditions.add(new Condition(columns.get(0), ">=", "103"));

        String[] second = new String[columnTitles.size()];
        for (int i = 0; i < columnTitles.size(); i++) {
            second[i] = students.select(columnTitles, conditions).get(2, i);
        }
        assertEquals("105 Shana Brown", String.join(" ", second));
        conditions.add(new Condition(columns.get(1), "<", columns.get(2)));

        String[] third = new String[columnTitles.size()];
        for (int i = 0; i < columnTitles.size(); i++) {
            third[i] = students.select(columnTitles, conditions).get(0, i);
        }
        students.select(columnTitles, conditions).print();
        assertEquals("103 Jonathan Xavier", String.join(" ", third));
        Table enrolled = Table.readTable("enrolled");
        columns = new ArrayList<Column>();
        columnTitles = new ArrayList<String>();
        columnTitles.add("Firstname");
        columnTitles.add("Grade");
        for (String name : columnTitles) {
            columns.add(new Column(name, students, enrolled));
        }
        conditions = new ArrayList<Condition>();
        Table table = students.select(enrolled, columnTitles, conditions);
        table.print();
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(UnitTest.class));
    }
}
