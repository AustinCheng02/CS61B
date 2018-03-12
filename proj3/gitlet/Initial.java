package gitlet;

import java.io.File;
import java.io.Serializable;

import java.util.HashMap;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/** class Initial that stores all the commits and the stage.
 *  @author Daniel Kim */
public class Initial implements Serializable {
    /** arraylist of all commits. */
    private ArrayList<Commit> allCommits;
    /** current branch.*/
    private String curBranch;
    /** headers to branches. */
    private HashMap<String, Commit> branchHead;
    /** branch and its stage. */
    private HashMap<String, Staged> stageMap;
    /** id to its commits. */
    private HashMap<String, Commit> idtoCommit;

    /** initializes the class. */
    private Initial() {
        branchHead = new HashMap<>();
        allCommits = new ArrayList<>();
        idtoCommit = new HashMap<>();
        stageMap = new HashMap<>();
    }

    /** Returns the version control. */
    public static Initial init() {
        Initial start = new Initial();
        String branch = "master";
        String log = "initial commit";
        Commit initi = new Commit(log, "Wed Dec 31 16:00:00 1969 -0800");
        start.allCommits.add(initi);
        Staged startStage = new Staged(initi);
        start.stageMap.put(branch, startStage);
        start.curBranch = branch;
        start.branchHead.put(branch, initi);
        initi.upId(Utils.sha1(Utils.serialize(initi)));
        start.idtoCommit.put(initi.getId(), initi);

        return start;
    }

    /** @param fileName gets added to the stage. */
    public void add(String fileName) {
        Staged curr = stageMap.get(curBranch);
        curr.add(fileName);
    }

    /** @param msg creates a commit with the msg. */
    public void commit(String msg) {
        Staged stage = stageMap.get(curBranch);
        for (String mark : stage.getFilesMarkedForRemoval()) {
            stage.stageRemove(mark);
        }

        if (stage.getStagedFiles().isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        Commit head = branchHead.get(curBranch);
        Commit newHead = new Commit(msg, stageMap.get(curBranch));
        Staged newStage = new Staged(newHead);
        allCommits.add(newHead);
        stageMap.put(curBranch, newStage);
        newHead.upId(Utils.sha1(Utils.serialize(newHead)));
        idtoCommit.put(newHead.getId(), newHead);
        branchHead.put(curBranch, newHead);
    }

    /** @param file gets removed. */
    public void rm(String file) {
        stageMap.get(curBranch).remove(file);


        if (branchHead.get(curBranch).contains(file)) {
            Utils.restrictedDelete(file);
            stageMap.get(curBranch).upRemove(file);
        }
    }

    /** prints the log. */
    public void log() {
        Commit curr = branchHead.get(curBranch);
        curr.log();
        curr = curr.getPreviousCommit();
        while (curr != null) {
            System.out.println();
            curr.log();
            curr = curr.getPreviousCommit();
        }
    }

    /** print the global log. */
    public void logGlobal() {

        if (allCommits.size() > 1) {
            allCommits.get(0).log();
        }

        for (int i = 1; i < allCommits.size(); i++) {
            System.out.println();
            allCommits.get(i).log();
        }
    }

    /** @param msg its commit gets printed. */
    public void find(String msg) {
        int count = 0;
        for (Commit commit : allCommits) {
            if (commit.msg().equals(msg)) {
                count += 1;
                System.out.println(commit.getId());
            }
        }
        if (count == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    /** the status gets printed. */
    public void status() {
        System.out.println("=== Branches ===");
        for (String branch : branchHead.keySet()) {
            if (branch.equals(curBranch)) {
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println();
        stageMap.get(curBranch).status();
    }


    /** @param file gets checkedout. */
    public void checkoutfile(String file) {
        if (!branchHead.get(curBranch).contains(file)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            branchHead.get(curBranch).restore(file);
        }
    }

    /** @param file gets check.
     *              @param id to its id.*/
    public void checkoutid(String id, String file) {
        if (!idtoCommit.containsKey(id)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit checkout = idtoCommit.get(id);
        if (!checkout.contains(file)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        checkout.restore(file);
    }

    /** @param branch gets checkedout. */
    public void checkoutB(String branch) {
        if (!branchHead.containsKey(branch)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        } else if (curBranch.equals(branch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        } else {
            Staged cur = stageMap.get(curBranch);
            curBranch = branch;
            branchHead.get(curBranch).restoreAll();
            Commit newCur = branchHead.get(curBranch);
            for (String file : cur.getStagedFiles().keySet()) {
                if (!newCur.getAddedFiles().containsKey(file)) {
                    Utils.restrictedDelete(file);
                }
            }
        }
    }

    /** @param branch gets added. */
    public void addNewBranch(String branch) {
        if (branchHead.containsKey(branch)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Commit head = branchHead.get(curBranch);
        Staged stage = new Staged(head);
        stageMap.put(branch, stage);
        branchHead.put(branch, head);
    }

    /** @param branch gets removed. */
    public void removeBranch(String branch) {
        if (!branchHead.containsKey(branch)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        if (curBranch.equals(branch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        branchHead.remove(branch);
    }

    /** @param id its commit gets reset. */
    public void reset(String id) {
        if (!idtoCommit.containsKey(id)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        Commit reset = idtoCommit.get(id);
        reset.restoreAll();
        branchHead.put(curBranch, reset);
        stageMap.put(curBranch, new Staged(reset));
    }

    /** @param otherBranch and
     *                     @param thisBranch returns the split. */
    public Commit split(Commit otherBranch, Commit thisBranch) {
        Commit thislen = thisBranch;
        Commit otherlen = otherBranch;
        int thisLength = 0;
        while (thislen != null) {
            thisLength += 1;
            thislen = thislen.getPreviousCommit();
        }
        int otherLength = 0;
        while (otherlen != null) {
            otherLength += 1;
            otherlen = otherlen.getPreviousCommit();
        }
        int diff = thisLength - otherLength;
        if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                thisBranch = thisBranch.getPreviousCommit();
            }
        } else {
            diff = diff * -1;
            for (int i = 0; i < diff; i++) {
                otherBranch = otherBranch.getPreviousCommit();
            }
        }

        while (!thisBranch.equals(otherBranch)) {
            thisBranch = thisBranch.getPreviousCommit();
            otherBranch = otherBranch.getPreviousCommit();
        }
        return thisBranch;
    }

    /** Checks for the @param branchName and.
     *  @param thisBranch and
     *  @param otherBranch for the branch. */
    public void chec(String branchName, Commit thisBranch, Commit otherBranch) {
        Staged current = stageMap.get(curBranch);
        for (String fileName : otherBranch.getAddedFiles().keySet()) {
            File file = new File(fileName);
            if (file.exists()
                    && (!current.getFilesNewStaged().contains(fileName)
                    && !current.getFilesMarkedForRemoval()
                    .contains(fileName))) {
                System.out.println("There is an untracked file in the way;"
                        + " delete it or add it first.");
                System.exit(0);
            }
        }
        if (!current.getFilesNewStaged().isEmpty()
                || !current.getFilesMarkedForRemoval().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (!branchHead.containsKey(branchName)) {
            System.out.println(" A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchName.equals(curBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
    }

    /** merges with the given @param branchName branch. */
    public void merge(String branchName) {
        Commit otherBranch = branchHead.get(branchName);
        Commit thisBranch = branchHead.get(curBranch);
        chec(branchName, thisBranch, otherBranch);
        Commit splitPoint = split(thisBranch, otherBranch);
        otherBranch = branchHead.get(branchName);
        thisBranch = branchHead.get(curBranch);
        if (splitPoint.equals(otherBranch)) {
            System.out.println("Given branch is an"
                    + " ancestor of the current branch.");
            return;
        }
        if (splitPoint.equals(thisBranch)) {
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        boolean merge = false;
        for (String fileName : otherBranch.getAddedFiles().keySet()) {
            File write = new File(fileName);
            File replace = new File(".gitlet"
                    + File.separator
                    + otherBranch.getAddedFiles().get(fileName));
            if (!thisBranch.contains(fileName)) {
                Utils.restrictedDelete(write);
                Utils.writeContents(write, Utils.readContentsAsString(replace));
            } else if (!thisBranch.modified(splitPoint, fileName)) {
                Utils.restrictedDelete(write);
                Utils.writeContents(write, Utils.readContentsAsString(replace));
            } else {
                merge = true;
                Utils.restrictedDelete(write);
                try {
                    if (write.isDirectory()) {
                        throw new IllegalArgumentException(
                                "cannot overwrite directory");
                    }
                    File read = new File(".gitlet"
                            + File.separator
                            + thisBranch.getAddedFiles().get(fileName));
                    BufferedOutputStream str =
                            new BufferedOutputStream(
                                    Files.newOutputStream(write.toPath()));
                    str.write("<<<<<<< HEAD\n".getBytes());
                    str.write(Utils.readContents(read));
                    str.write("=======\n".getBytes());
                    str.write(Utils.readContents(replace));
                    str.write(">>>>>>>\n".getBytes());
                    str.close();
                } catch (IOException | ClassCastException excp) {
                    throw new IllegalArgumentException(excp.getMessage());
                }
            }
        }
        if (merge) {
            System.out.println("Encountered a merge conflict.");
        } else {
            commit("Merged " + branchName + " into " + curBranch);
        }
    }


}

