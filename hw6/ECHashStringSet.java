import java.util.LinkedList;
// The current contents of this file are merely to allow things to compile 
// out of the box. It bears scant relation to a proper solution (for one thing,
// a hash table should not be a SortedStringSet.)
/** A set of String values.
 *  @author Daniel Kim
 */
class ECHashStringSet extends BSTStringSet {
    private static double min = 0.2;
    private static double max = 5;
    private int n;
    private LinkedList<String>[] a;
    // Initializes an empty list
    public ECHashStringSet() {
        a = new LinkedList[(int)(1/min)];
        n = 0;
    }

    @Override
    public boolean contains(String s) {
        if (s == null) {
            return false;
        }
        int pos = (s.hashCode() & 0x7fffffff) % a.length;
        if (a[pos] == null) {
            return false;
        }
        return a[pos].contains(s);
    }

    @Override
    public void put(String s) {
        if (s == null) {
            return;
        }
        if (!contains(s)) {
            n++;
            if ((n / a.length) > max) {
                resize();
            }
            int pos = (s.hashCode() & 0x7fffffff) % a.length;
            if (a[pos] == null) {
                a[pos] = new LinkedList<String>();
            }
            a[pos].add(s);
        }
    }

    public int size() {
        return n;
    }

    public void resize(){
        a = new LinkedList[a.length * 2];
        n = 0;
        for (LinkedList x : a) {
            if (x != null) {
                for (Object y : x) {
                    put((String) y);
                }
            }
        }

    }
}