package bagarrao.financialdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * @author Eduardo Bagarrao
 */
public class MainActivity extends AppCompatActivity {

    public static final String ACTIVITY_TITLE = "FinancialDroid";

    //    functional
    private Intent expensesIntent;
    private Intent addExpenseIntent;
    private Intent infoIntent;
    //    only on alpha/beta phase
    private Intent backupIntent;
    //    not on alpha phase
    private Intent analyticsIntent;
    private Intent archiveIntent;

    //    functional
    private Button expensesButton;  //to check your expenses
    private Button addExpenseButton;    //to register a new Expense
    private Button infoButton;  //to show info about the app
    //    only on alpha/beta phase
    private Button backupButton;    //to show backup lobby
//    not on alpha phase
//    private Button archiveButton;
//    private Button analyticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(ACTIVITY_TITLE);
        setSupportActionBar(toolbar);
        init();
        setListeners();
    }

    /**
     * initializes all the elements
     */
    public void init() {

//        functional
        this.expensesIntent = new Intent(this, ExpensesActivity.class);
        this.addExpenseIntent = new Intent(this, AddExpenseActivity.class);
        this.infoIntent = new Intent(this, InfoActivity.class);
        //    only on alpha/beta phase
        this.backupIntent = new Intent(this, BackupActivity.class);
//        not on alpha phase
//        this.analyticsIntent = new Intent(this,AnalyticsActivity.class);
//        this.archiveIntent = new Intent(this,ArchiveActivity.class);

//        functional
        this.expensesButton = (Button) findViewById(R.id.expensesButton);
        this.addExpenseButton = (Button) findViewById(R.id.addExpenseButton);
        this.infoButton = (Button) findViewById(R.id.infoButton);
        //    only on alpha/beta phase
        this.backupButton = (Button) findViewById(R.id.backupButton);
//        not on alpha phase
//        this.analyticsButton = (Button) findViewById(R.id.analyticsButton);
//        this.archiveButton = (Button) findViewById(R.id.archiveButton);
    }

    /**
     * sets the listeners of the views
     */
    public void setListeners() {
        expensesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(expensesIntent);
                    }
                }
        );

        addExpenseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(addExpenseIntent);
                    }
                }
        );

        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String filePath = backupToCSV();
//                Toast.makeText(getApplicationContext(), "Backup is successfully done! Please enjoy :D\n" +
//                        "File directory: " + filePath, Toast.LENGTH_LONG).show();
                startActivity(backupIntent);
            }
        });


        infoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(infoIntent);
                    }
                }
        );

//        analyticsButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(analyticsIntent);
//                    }
//                }
//        );

//        archiveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(archiveIntent);
//            }
//        });
    }

//    /**
//     * backups all expenses to a CSV file
//     */
//    public String backupToCSV() {
//        String filePath = "";
//        Date date = new Date();
//        BufferedWriter bw;
//        ExpenseDataSource dataSource;
//        try {
//            File file = new File(this.getApplicationContext().getFilesDir(), "backup.csv");
//            if (!file.exists())
//                file.createNewFile();
//            else {
//                for (int i = 0; ; i++) {
//                    file = new File(this.getApplicationContext().getFilesDir(), "backup(" + i + ").csv");
//                    if (!file.exists())
//                        break;
//                }
//            }
//            bw = new BufferedWriter(new FileWriter(file));
//            dataSource = new ExpenseDataSource(this.getApplicationContext());
//            dataSource.open();
//            List<Expense> list = dataSource.getAllExpenses();
//            for (Expense e : list) {
//                bw.write(e.toString() + "\n");
//            }
//            filePath = file.getAbsolutePath();
//            dataSource.close();
//            bw.flush();
//            bw.close();
//        } catch (IOException io) {
//            io.getStackTrace();
//        }
//        return filePath;
//    }
}
