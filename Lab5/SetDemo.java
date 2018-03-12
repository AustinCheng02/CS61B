import java.util.HashSet;
import java.util.Set;

//@author: Daniel Kim
public class SetDemo {

    public static void main(String[] args) {
        Set<String> bears = new HashSet<String>();
        bears.add("papa");
        bears.add("bear");
        bears.add("mama");
        bears.add("bear");
        bears.add("baby");
        bears.add("bear");
        System.out.println(bears);
    }
}