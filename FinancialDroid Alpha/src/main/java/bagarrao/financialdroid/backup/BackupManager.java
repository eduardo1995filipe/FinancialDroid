package bagarrao.financialdroid.backup;

import android.content.Context;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Eduardo Bagarrao
 */
public class BackupManager {

    public static final String FILE_NAME = "backup";
    public static final String FILE_EXTENSION = ".csv";
    private static final BackupManager INSTANCE = new BackupManager();
    private Context context;
    private boolean autoBackupEnabled;
//    private LinkedList<Backup> backupList;

    /**
     *
     */
    private BackupManager() {
//        this.backupList = new LinkedList<>();
    }

    /**
     * @return
     */
    public static BackupManager getInstance() {
        return (INSTANCE == null) ? new BackupManager() : INSTANCE;
    }

    /**
     * @return
     */
    public boolean isAutoBackupEnabled() {
        return autoBackupEnabled;
    }

    /**
     *
     * @param autoBackupEnabled
     */
    public void setAutoBackupEnabled(boolean autoBackupEnabled) {
        this.autoBackupEnabled = autoBackupEnabled;
    }

    /**
     *
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * @return
     */
    public List<File> getAllBackupFiles() {
        LinkedList<File> backupFilesList = new LinkedList<>();
        File file = new File(FILE_NAME + FILE_EXTENSION);
        if (file.exists())
            backupFilesList.add(file);
        for (int i = 1; ; i++) {
            File nextFile = new File(FILE_NAME + "(" + i + ")" + FILE_EXTENSION);
            if (nextFile.exists())
                backupFilesList.add(nextFile);
            else
                break;
        }
        return backupFilesList;
    }

    /**
     * @return
     */
    public boolean deleteBackup(File toRemove) {
        List<File> list = getAllBackupFiles();
        for (File f : list) {
            if (f.getName().equals(toRemove.getName())) {
                list.remove(f);
                return true;
            }
        }
        return false;
    }

    /**
     * @return
     */
    public void createBackup() {
        Backup backup = new Backup(context);
        backup.go();
    }

    /**
     *
     */
    public void resetBackups() {
        List<File> list = getAllBackupFiles();
        for (File f : list) {
            f.delete();
        }
    }
}
