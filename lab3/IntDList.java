
public class IntDList {

    protected DNode _front, _back;

    public IntDList() {
        _front = _back = null;
    }

    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /** Returns the first item in the IntDList. */
    public int getFront() {
        return _front._val;
    }

    /** Returns the last item in the IntDList. */
    public int getBack() {
        return _back._val;
    }

    /** Return value #I in this list, where item 0 is the first, 1 is the
     *  second, ...., -1 is the last, -2 the second to last.... */
    public int get(int i) {
        DNode location;
    	if (i >= 0){
    		location = _front;
    	}
    	else {
    		location = _back;
    	}
    	while (i > 0){
    		location = location._next;
    		i -= 1;
    	}
    	while (i < -1){
    		location = location._prev;
    		i += 1;
    	}
        return location._val;   // Your code here
    }

    /** The length of this list. */
    public int size() {
    	int count = 0;
    	DNode current = _front;
    	while (current != null){
    		current = current._next;
    		count +=1;
    	}
        return count;   // Your code here
    }

    /** Adds D to the front of the IntDList. */
    public void insertFront(int d) {
    	if (_front == null){
    	    _front = new DNode(_front, d, _back);
    	    _back = _front;
        }
        else {
    	    DNode save = _front;
    	    _front = new DNode(null, d, save);
    	    save._prev = _front;
        }
        // Your code here 
    }

    /** Adds D to the back of the IntDList. */
    public void insertBack(int d) {
        if (_back == null){
            insertFront(d);
        }
        else {
            DNode save = _back;
            _back = new DNode(save, d, null);
            save._next = _back;
        }
        // Your code here 
    }

    /** Removes the last item in the IntDList and returns it.
     * This is an extra challenge problem. */
    public int deleteBack() {
        return 0;   // Your code here

    }

    /** Returns a string representation of the IntDList in the form
     *  [] (empty list) or [1, 2], etc. 
     * This is an extra challenge problem. */
    public String toString() {
        return null;   // Your code here
    }

    /* DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information! */
    protected static class DNode {
        protected DNode _prev;
        protected DNode _next;
        protected int _val;

        private DNode(int val) {
            this(null, val, null);
        }

        private DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
