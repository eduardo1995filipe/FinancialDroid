package bagarrao.financialdroid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.Expense.Expense;
import bagarrao.financialdroid.Expense.ExpenseType;
import bagarrao.financialdroid.backup.BackupManager;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.utils.DateForCompare;

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
    private Button restoreButton;

    //    private List<File> backupFilesList;
    private List<String> backupFilesListString;
    private ArrayAdapter<String> backupFilesAdapter;
    private File backupFileSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        init();
        setListeners();
    }

    /**
     * initializes all the elements
     */
    public void init() {
        this.autoBackupSwitch = (Switch) findViewById(R.id.autoBackupSwitch);
        this.backupListView = (ListView) findViewById(R.id.backupListListview);
        this.infoTextview = (TextView) findViewById(R.id.backupInfoTextView);
        this.backupButton = (Button) findViewById(R.id.newBackupFileButton);
        this.deleteBackupButton = (Button) findViewById(R.id.deleteBackupButton);
        this.resetButton = (Button) findViewById(R.id.resetBackupFilesButton);
        this.restoreButton = (Button) findViewById(R.id.restoreBackupButton);
        this.backupFilesListString = new LinkedList<>();
        this.backupFilesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, backupFilesListString);
        backupListView.setAdapter(backupFilesAdapter);

        this.infoTextview.setText("");

        this.backupListView.setBackgroundColor(Color.LTGRAY);

        refreshBackups();

        this.backupFileSelected = null;

        this.bm.setContext(this);
        this.autoBackupEnabled = autoBackupSwitch.isEnabled();
//        this.backupFilesList = bm.getAllBackupFiles();

        deleteBackupButton.setVisibility(Button.INVISIBLE);
        restoreButton.setVisibility(Button.INVISIBLE);

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

        backupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<File> list = bm.getAllBackupFiles();
                String string = backupFilesListString.get(position).trim().toUpperCase();
                for (File f : list) {
                    String fileName = f.getName().trim().toUpperCase();
                    if (string.equals(fileName)) {
                        backupFileSelected = f;
                        infoTextview.setText("File name: " + f.getName() + " \n Backup date: " + "info exemplo \n info exemplo \n info exemplo");
                        deleteBackupButton.setVisibility(Button.VISIBLE);
                        restoreButton.setVisibility(Button.VISIBLE);
                    }
                }
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
                } else {
                    backupFileSelected = null;
                    infoTextview.setText("");
                    deleteBackupButton.setVisibility(Button.INVISIBLE);
                    restoreButton.setVisibility(Button.INVISIBLE);
                    refreshBackups();
                }
            }
        });

        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bm.createBackup();
                refreshBackups();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bm.resetBackups();
                refreshBackups();
                backupFileSelected = null;
                infoTextview.setText("");
            }
        });


        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backupFileSelected != null) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(backupFileSelected));
                        ExpenseDataSource dataSource = new ExpenseDataSource(getApplicationContext());
                        dataSource.open();
                        dataSource.deleteAllExpenses();
                        String text = "";
                        while ((text = br.readLine()) != null) {
                            String[] expenseString = text.split(";");
                            Expense e = new Expense(Double.parseDouble(expenseString[0]),
                                    ExpenseType.valueOf(expenseString[1]), expenseString[2],
                                    DateForCompare.DATE_FORMATTED.parse(expenseString[3]));
                            dataSource.createExpense(e);
                        }
                        dataSource.close();
                        br.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(BackupActivity.this, "No backup file selected to restore!", Toast.LENGTH_SHORT).show();
                bm.deleteBackup(backupFileSelected);
                backupFileSelected = null;
                refreshBackups();
            }
        });
    }

    public void refreshBackups() {
        List<File> list = bm.getAllBackupFiles();
        if (list == null)
            list = new LinkedList<>();
        backupFilesListString.clear();
        for (File f : list) {
            backupFilesListString.add(f.getName());
        }
        backupFilesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        refreshBackups();
        super.onResume();
    }

}
