package bagarrao.financialdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * @author Eduardo Bagarrao
 */
public class BackupActivity extends AppCompatActivity {

    private Switch autoBackupSwitch;
    private ListView backupListView;
    private TextView infoTextview;
    private Button backupButton;
    private Button deleteBackupButton;
    private Button resetButton;

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

    }

    /**
     * sets the listeners of the views
     */
    public void setListeners() {

        autoBackupSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        backupListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deleteBackupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
