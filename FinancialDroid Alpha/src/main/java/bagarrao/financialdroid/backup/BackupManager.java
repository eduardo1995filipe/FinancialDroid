package bagarrao.financialdroid.backup;

import android.content.Context;

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
    private LinkedList<Backup> backupList;

    /**
     *
     */
    private BackupManager() {
        this.backupList = new LinkedList<>();
    }

    /**
     *
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
    public List<Backup> getBackups() {
        return backupList;
    }

    /**
     * @return
     */
    public boolean deleteBackup(Backup toRemove) {
        for (Backup backup : backupList) {
            if (backup.getBackupName().equals(toRemove.getBackupName())) {
                backupList.remove(backup);
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
        saveBackup(backup);
    }

    /**
     * @param toSave
     */
    private void saveBackup(Backup toSave) {
        backupList.add(toSave);
    }

    public void resetBackups() {
        backupList.clear();
    }
}
