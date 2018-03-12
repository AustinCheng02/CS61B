package gitlet;


import java.io.File;


/** A simplified version control system.
 * @author Daniel Kim. */
public class Main {

    /** gets inputs from the command. @param args are the inputs. */
    public static void main(String...  args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        Initial command = loadFile();
        switch (args[0]) {
        case "init":
            command = init();
            break;
        case "add":
            command.add(args[1]);
            break;
        case "commit":
            if (args.length == 1) {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            } else if (args[1].isEmpty()) {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            }
            command.commit(args[1]);
            break;
        case "rm":
            command.rm(args[1]);
            break;
        case "log":
            command.log();
            break;
        case "global-log":
            command.logGlobal();
            break;
        case "find":
            command.find(args[1]);
            break;
        case "status":
            command.status();
            break;
        case "checkout":
            checkout(command, args);
            break;
        case "branch":
            command.addNewBranch(args[1]);
            break;
        case "rm-branch":
            command.removeBranch(args[1]);
            break;
        case "reset":
            command.reset(args[1]);
            break;
        case "merge":
            command.merge(args[1]);
            break;
        default:
            System.out.println("No command with that name exists.");
            System.exit(0);
            break;
        }
        saveFile(command);
    }

    /** @param com is the command.
     *             @param args are the inputs.*/

    private static void checkout(Initial com, String... args) {
        if ((args.length == 3) && (args[1].equals("--"))) {
            com.checkoutfile(args[2]);
        } else if ((args.length == 4) && args[2].equals("--")) {
            com.checkoutid(args[1], args[3]);
        } else if (args.length == 2) {
            com.checkoutB(args[1]);
        } else {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    /** Returns the initial command. */
    private static Initial init() {
        File directory = new File(".gitlet");
        if (directory.exists()) {
            System.out.println("A gitlet version control system"
                    + " already exists in the current directory");
            System.exit(0);
        }
        directory.mkdir();

        return Initial.init();
    }

    /** Returns the saved command. */
    private static Initial loadFile() {
        Initial command = null;

        File load = new File(".gitlet" + File.separator + "gitletMain.ser");
        if (load.exists()) {
            command = Utils.readObject(load, Initial.class);
        }
        return command;
    }

    /** Saves the @param command to directory. */
    public static void saveFile(Initial command) {
        if (command == null) {
            return;
        }
        File save = new File(".gitlet" + File.separator + "gitletMain.ser");
        Utils.writeObject(save, command);
    }
}
