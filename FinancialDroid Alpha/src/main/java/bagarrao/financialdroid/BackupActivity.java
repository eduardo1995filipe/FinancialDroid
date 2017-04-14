package bagarrao.financialdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import bagarrao.financialdroid.backup.BackupManager;

/**
 * @author Eduardo Bagarrao
 */
public class BackupActivity extends AppCompatActivity {

    public static boolean autoBackupEnabled;
    private BackupManager bm = BackupManager.getInstance();

    private Switch autoBackupSwitch;
    private ListView backupListView;
    private TextView infoTextview;
    private Button backupButton;
    private Button deleteBackupButton;
    private Button resetButton;

    private List<File> backupFilesList;
    private List<String> backupFilesListString;
    private File backupFileSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
    }

    /**
     * initializes all the elements
     */
    public void init() {
        this.autoBackupSwitch = (Switch) findViewById(R.id.autoBackupSwitch);
        this.backupListView = (ListView) findViewById(R.id.backupListListview);
        this.infoTextview = (TextView) findViewById(R.id.autoBackupTextView);
        this.backupButton = (Button) findViewById(R.id.newBackupFileButton);
        this.deleteBackupButton = (Button) findViewById(R.id.deleteBackupButton);
        this.resetButton = (Button) findViewById(R.id.resetBackupFilesButton);

        this.backupFileSelected = null;

        this.bm.setContext(this);
        this.autoBackupEnabled = autoBackupSwitch.isEnabled();
        this.backupFilesList = bm.getAllBackupFiles();
    }

    /**
     * sets the listeners of the views
     */
    public void setListeners() {

        autoBackupSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoBackupEnabled = autoBackupSwitch.isEnabled();
            }
        });

        backupListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (File f : backupFilesList) {
                    if (f.getName().equals(backupFilesListString.get(position)))
                        backupFileSelected = f;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                backupFileSelected = backupFilesList.get(0);
            }
        });

        deleteBackupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean removeSuccessfull = false;
                try {
                    removeSuccessfull = bm.deleteBackup(backupFileSelected);
                } catch (NullPointerException e) {
                    Log.e("BackupActivity", "No backup is selected");
                    e.getStackTrace();
                }
                if (!removeSuccessfull) {
                    Log.e("BackupActivity", "Problem to remove backup file");
                }
//                notificar as alteracoes
            }
        });

        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bm.createBackup();
//                adicionar o backup ao listview
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bm.resetBackups();
//                eliminat todos os backups e notificar a listview
            }
        });
    }

}
