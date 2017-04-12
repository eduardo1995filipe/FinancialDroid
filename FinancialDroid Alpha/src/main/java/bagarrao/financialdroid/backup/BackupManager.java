package bagarrao.financialdroid.backup;

import android.content.Context;

import java.io.File;
import java.util.HashMap;

/**
 * @author Eduardo Bagarrao
 */
public class BackupManager {

    public static final String FILE_NAME = "backup";
    public static final String FILE_EXTENSION = ".csv";

    private Context context;
    private HashMap<String, File> backupFilesMap;

    /**
     * @return
     */
    public HashMap<String, File> getBackupFilesMap() {
        return backupFilesMap;
    }

    public void refreshBackupFiles() {

    }
}
