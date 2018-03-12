package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;

/** a class that keeps track of staged files.
 *  @author Daniel Kim
 */
public class Staged implements Serializable {

    /** the commit of the stage. */
    private Commit represent;
    /** newly staged files. */
    private ArrayList<String> newlyStaged;
    /** marked by remove. */
    private ArrayList<String> markedRemove;
    /** files that are removed. */
    private ArrayList<String> removed;
    /** staged files. */
    private HashMap<String, String> stagedFiles;

    /** Initializes with @param latestCommit commit. */
    public Staged(Commit latestCommit) {
        represent = latestCommit;
        newlyStaged = new ArrayList<>();
        markedRemove = new ArrayList<>();
        stagedFiles = new HashMap<>();
        removed = new ArrayList<>();
        if (latestCommit.getAddedFiles() != null) {
            stagedFiles.putAll(latestCommit.getAddedFiles());
        }
    }


    /** Returns the latest commit. */
    public Commit getLatestCommit() {
        return represent;
    }

    /** Returns newly staged files. */
    public ArrayList<String> getFilesNewStaged() {
        return newlyStaged;
    }

    /** Returns marked files. */
    public ArrayList<String> getFilesMarkedForRemoval() {
        return markedRemove;
    }

    /** Returns the staged files. */
    public HashMap<String, String> getStagedFiles() {
        return this.stagedFiles;
    }

    /** @param file gets removed. */
    public void upRemove(String file) {
        removed.add(file);
    }

    /** @param file gets removed from stage. */
    public void stageRemove(String file) {
        stagedFiles.remove(file);
    }

    /** @param file gets added to stage. */
    public void add(String file) {
        File name = new File(file);
        if (!name.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        String comparison = Utils.sha1(Utils.readContentsAsString(name));
        removed.remove(file);
        markedRemove.remove(file);
        if (represent.contains(file)) {
            if (!represent.getIdfile(file).equals(comparison)) {
                stagedFiles.put(file, comparison);
                newlyStaged.add(file);
            } else {
                if (removed.contains(file)) {
                    represent.restore(file);
                }
            }
        } else {
            stagedFiles.put(file, comparison);
            newlyStaged.add(file);
        }
    }

    /** @param fileName gets removed. */
    public void remove(String fileName) {
        if (stagedFiles.containsKey(fileName)) {
            stagedFiles.remove(fileName);
            newlyStaged.remove(fileName);
            markedRemove.add(fileName);
        } else {
            System.out.println("No reason to remove the file.");
        }
    }

    /** prints the status. */
    public void status() {
        System.out.println("=== Staged Files ===");
        for (String file : newlyStaged) {
            System.out.println(file);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String file : removed) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();

    }
}
