import org.junit.Test;
import static org.junit.Assert.*;

/** Tests accumulatevertical and accumulate.
 *  @author Daniel Kim
 */

public class MatrixUtilsTest {
    /**
     */
    @Test
    public void accumVertTest() {
        double[][] array1 = new double[][] {{1000000,1000000,1000000,1000000},
                                            {1000000,75990,30003,1000000},
                                            {1000000,30002,103046,1000000},
                                            {1000000,29515,38273,1000000},
                                            {1000000,73403,35399,1000000},
                                            {1000000,1000000,1000000,1000000}};
        double[][] array2 = new double[][] {{1000000,1000000,1000000,1000000},
                                            {2000000,1075990,1030003,2000000},
                                            {2075990,1060005,1133049,2030003},
                                            {2060005,1089520,1098278,2133049},
                                            {2089520,1162923,1124919,2098278},
                                            {2162923,2124919,2124919,2124919}};

        double[][] array3 = MatrixUtils.accumulateVertical(array1);
        for (int i = 0; i < 6; i++){
            assertArrayEquals(array2[i],array3[i],1);
        }

    }
    @Test
    public void orientedVertTest() {
        double[][] array0 = new double[][] {{1000000,1000000,1000000,1000000},
                {1000000,75990,30003,1000000},
                {1000000,30002,103046,1000000},
                {1000000,29515,38273,1000000},
                {1000000,73403,35399,1000000},
                {1000000,1000000,1000000,1000000}};
        double[][] array1 = new double[][] {{1000000,1000000,1000000,1000000,1000000,1000000},
                {1000000,73403,29515,30002,75990,1000000},
                {1000000,35399,38273,103046,30003,1000000},
                {1000000,1000000,1000000,1000000,1000000,1000000}};

        double[][] array3 = MatrixUtils.accumulate(array0, MatrixUtils.Orientation.HORIZONTAL);
        double[][] array4 = MatrixUtils.accumulateVertical(array1);
        System.out.println(MatrixUtils.matrixToString(array3));
        for (int i = 0; i < 4; i++){
            assertArrayEquals(array3[i],array4[i],1);
        }

    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
