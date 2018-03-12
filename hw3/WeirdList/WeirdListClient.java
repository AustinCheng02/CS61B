/** Functions to increment and sum the elements of a WeirdList. */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        return L.map(x -> x + n); // REPLACE THIS LINE WITH THE RIGHT ANSWER.
    }

    /** Return the sum of the elements in L. */
    static int sum(WeirdList L) {
        sumElements sum = new sumElements();
        L.map(sum);
        return sum.getSum();
    }

    private static class sumElements implements IntUnaryFunction {
        private int _sum;

        public sumElements() {
            _sum = 0;
        }
        public int apply(int x) {
            _sum += x;
            return _sum;
        }

        public int getSum() {
            return _sum;
        }
    }

    /* As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     */
}
