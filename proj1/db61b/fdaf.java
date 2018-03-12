import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;


public class Solution {
    private static String[] _storage = new String[10];
    private static String[] _input;
    private static HashMap[] _map = new HashMap[10];
    private static int _count = 0;

    public static void main(String args[] ) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        Scanner scanner = new Scanner(System.in);
        String argument = scanner.next();
        String list = scanner.next();
        String[] command = {argument, list};
        scanner.close();
        Commands(command);
    }
    static void Commands(String[] in) {
        _input = in;
        _map[_count] = new HashMap<String, String>();
        switch (_input[0]) {
            case "add":
                _storage[_count] = in[1];
                addCommand();
            case "get":
                getCommand();
            default:
                System.out.println("unrecognizable command");
        }
    }

    static void addCommand() {
        String inside = _input[1].substring(1, _input[1].length() - 1).trim();
        System.out.println(inside);
        String[] categories = inside.split(",");
        System.out.println(categories[0]);
        System.out.println(categories[1]);
        for (String category : categories) {
            String[] keys = category.split(":", -1);
            _map[_count].put(keys[0], keys[1]);
        }
        _count += 1;
    }

    static void getCommand() {
        _input[1].substring(1, _input[1].length() - 1).trim();
        String[] categories = _input[1].split(",");
        for (int i = 0; i < _count; i++) {
            int same = 0;
            for (String category : categories) {
                String[] keys = category.split(":", -1);
                String value = (String) _map[i].get(keys[0]);
                if ((keys[1].substring(0,1) == "[") || (keys[1].substring(0,1) == "{")) {
                    if (contains(keys[1].substring(1, keys[1].length() - 1), value)) {
                        same += 1;
                    }
                } else if (value == keys[1]){
                    same += 1;
                } else {
                    break;
                }
            }
            if (same == categories.length) {
                System.out.println(_storage[i]);
            }
        }
    }

    static boolean contains(String a, String b) {
        if (a.length() > b.length()) {
            return false;
        } else if (a.length() == b.length()) {
            return a==b;
        } else if (a == b.substring(0, a.length())) {
            return true;
        } else {
            return contains(a, b.substring(1));
        }
    }
}