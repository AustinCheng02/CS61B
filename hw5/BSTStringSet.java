import java.util.List;

/**
 * Implementation of a BST based String Set.
 * @author Daniel Kim
 */
public class BSTStringSet implements StringSet {
    /** Creates a new empty set. */
    public BSTStringSet() {
        root = null;
    }

    @Override
    public void put(String s) {
        root = put(s, root);
    }

    private Node put(String s, Node r) {
        if (r == null) {
            return new Node(s);
        }
        int compare = s.compareTo(r.s);
        if (compare > 0) {
            r.right = put(s, r.right);
        } else if (compare < 0) {
            r.left = put(s, r.left);
        }
        return r;
    }

    @Override
    public boolean contains(String s) {
        return contains(s, root);
    }

    private boolean contains(String s, Node r) {
        if (r == null) {
            return false;
        }
        int compare = s.compareTo(root.s);
        if (compare > 0) {
            return contains(s, r.right);
        } else if (compare < 0) {
            return contains(s, r.left);
        } else {
            return true;
        }
    }
    @Override
    public List<String> asList() {
        return null; // FIXME
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        public Node(String sp) {
            s = sp;
        }
    }

    /** Root node of the tree. */
    private Node root;
}
