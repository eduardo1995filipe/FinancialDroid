package bagarrao.financialdroid.backup;

import android.content.Context;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
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
    private HashMap<String, Date> fileDateMap; // TODO list of date of all backup files


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
     * @param autoBackupEnabled
     */
    public void setAutoBackupEnabled(boolean autoBackupEnabled) {
        this.autoBackupEnabled = autoBackupEnabled;
    }

    /**
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
        File file = new File(context.getFilesDir(), FILE_NAME + FILE_EXTENSION);
        if (file.exists()) {
            backupFilesList.add(file);
        } else
            return backupFilesList;

        for (int i = 1, index = 0; ; i++) {
            File nextFile = new File(context.getFilesDir(), FILE_NAME + "(" + i + ")" + FILE_EXTENSION);
            if (nextFile.exists())
                backupFilesList.add(nextFile);
            else if (index > 10)
                break;
            else
                index++;
        }
        return backupFilesList;
    }

    /**
     * @return
     */
    public boolean deleteBackup(File toRemove) {
        File file = new File(context.getFilesDir(), toRemove.getName());
        if (file.exists()) {
            file.delete();
            return true;
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
