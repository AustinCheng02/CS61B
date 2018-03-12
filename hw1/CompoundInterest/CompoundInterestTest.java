import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.
        assertEquals(0, 0); */
        assertEquals(1, CompoundInterest.numYears(2016));
        assertEquals(2,CompoundInterest.numYears(2017));
        assertEquals(0,CompoundInterest.numYears(2015));
    }

    @Test
    public void testFutureValue() {

        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10,12,2017),tolerance);
        assertEquals(11, CompoundInterest.futureValue(10,10,2016), tolerance);
    }

    @Test
    public void testFutureValueReal() {

        double tolerance = 0.01;
        assertEquals(11.8026496, CompoundInterest.futureValueReal(10,12,2017,3),tolerance);
        assertEquals(10.67,CompoundInterest.futureValueReal(10,10,2016,3),tolerance);
    }


    @Test
    public void testTotalSavings() {

        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000,2017,10),tolerance);
        assertEquals(13497.6, CompoundInterest.totalSavings(4000,2017,12),tolerance);
    }

    @Test
    public void testTotalSavingsReal() {

        double tolerance = 0.01;
        assertEquals(13066.65984, CompoundInterest.totalSavingsReal(4000,2017,12,3),tolerance);
        assertEquals(15980.48, CompoundInterest.totalSavingsReal(5000, 2017,12,5), tolerance);

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
