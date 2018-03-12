/* NOTE: The file ArrayUtil.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Daniel Kim
 */
class Arrays {
    /* C. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        if (A.length == 0) {
            return B;
        } else if (B.length == 0) {
            return A;
        }
        int[] C = new int[A.length + B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);
        return C;
    }

    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        int[] B = new int[A.length - len];
        if (start == 0) {
            System.arraycopy(A, len, B, 0, A.length - len);
            return B;
        }
        System.arraycopy(A, 0, B, 0, start);
        System.arraycopy(A, start + len, B, start, A.length - start - len);

        return B;
    }

    /* E. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        if (A.length == 0) {
            return new int[0][];
        }
        int l = 1;
        int [][] r;
        int t = 0;
        for (int i = 1; i < A.length; i++) {
            if (A[i] < A[i - 1]) {
                l++;
            }
        }
        r = new int[l][];
        l = 0;
        for (int i = 1; i < A.length; i++) {
            if (A[i] < A[i - 1]) {
                r[l++] = Utils.subarray(A, t, i - t);
                t = i;
            }
        }
        if (l != r.length) {
            r[l] = Utils.subarray(A, t, A.length - t);
        }
        return r;

    }
}
