package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;


/** Public class Commit.
 * @author Daniel Kim
 */

public class Commit implements Serializable {

    /** unique id for commit.
     */
    private String id;
    /** msg for the commit.
     */
    private String msg;
    /** time for the commit.
     */
    private String commitTime;
    /** files for the commit.
     */
    private HashMap<String, String> addedFiles;
    /** previous commit for the commit.
     */
    private Commit parent;

    /** Initializes the class.
     *
     * @param log msg
     * @param currentStage files that are staged
     */
    public Commit(String log, Staged currentStage) {
        msg = log;
        String pattern = "EEE MMM dd kk:mm:ss yyyy Z";
        SimpleDateFormat simple = new SimpleDateFormat(pattern);
        commitTime = simple.format(new Date());
        if (currentStage != null) {
            addedFiles = currentStage.getStagedFiles();
            parent = currentStage.getLatestCommit();
        }
        saveFiles();
    }

    /** Initializes the commit class.
     *
     * @param log msg
     * @param time time committed.
     */
    public Commit(String log, String time) {
        commitTime = time;
        addedFiles = new HashMap<>();
        parent = null;
        msg = log;
        saveFiles();
    }

    /** get the id for the file.
     *
     * @param file the file
     * @return the ID
     */
    public String getIdfile(String file) {
        return addedFiles.get(file);
    }

    /** update the id of the commit to newId.
     *
     * @param newId the update.
     */
    public void upId(String newId) {
        id = newId;
    }

    /** returns the id.
     *
     * @return the commit id.
     */
    public String getId() {
        return id;
    }

    /** Returns the msg of the commit.
     *
     * @return msg
     */
    public String msg() {
        return msg;
    }

    /** Returns all files added.
     *
     * @return all files.
     */
    public HashMap<String, String> getAddedFiles() {
        return addedFiles;
    }

    /** Returns the parent commit. */
    public Commit getPreviousCommit() {
        return parent;
    }

    /** Returns whether @param fileName is contained. */
    public boolean contains(String fileName) {
        if (addedFiles == null) {
            return false;
        }
        return addedFiles.containsKey(fileName);
    }

    /** Returns the file that has @param fileName in this. */
    public File getFile(String fileName) {
        String path = addedFiles.get(fileName) + fileName;
        File backup = new File(path);
        return backup;
    }

    /** Restore the @param fileName to this version. */
    public void restore(String fileName) {
        File writein = new File(fileName);
        String backed = addedFiles.get(fileName);
        File backup = new File(".gitlet" + File.separator + backed);
        Utils.writeContents(writein, Utils.readContents(backup));
    }

    /** Restore every added files. */
    public void restoreAll() {
        Collection<String> fileNames = addedFiles.keySet();
        for (String fileName : fileNames) {
            restore(fileName);
        }
    }

    /** Returns whether the @param fileName is
     *  modified in the @param other commit. */
    public boolean modified(Commit other, String fileName) {
        File originFile = other.getFile(fileName);
        File thisFile = this.getFile(fileName);
        return thisFile.lastModified() > originFile.lastModified();
    }

    /** backups the files. */
    private void saveFiles() {
        if (addedFiles == null) {
            return;
        }
        for (String fileName : addedFiles.keySet()) {
            File work = new File(fileName);
            String path = ".gitlet" + File.separator + addedFiles.get(fileName);
            File backupFile = new File(path);
            Utils.writeContents(backupFile, Utils.readContents(work));
        }
    }

    /** prints the log. */
    public void log() {
        System.out.println("===");
        System.out.println("commit " + id);
        System.out.println("Date: " + commitTime);
        System.out.println(msg);
    }

}
