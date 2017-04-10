package bagarrao.financialdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import bagarrao.financialdroid.database.ExpenseDataSource;

public class MainActivity extends AppCompatActivity {

    //functional
    private Intent expensesIntent;
    private Intent addExpenseIntent;
    //not on alpha phase
    private Intent analyticsIntent;
    private Intent archiveIntent;

    //functional
    private Button expensesButton;
    private Button addExpenseButton;
    private Button backupButton; //to extract expenses into a CSV file
    //not on alpha phase
//    private Button archiveButton;
//    private Button analyticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListeners();
    }

    /**
     * initializes all the elements
     */
    public void init() {
        setContentView(R.layout.activity_main);
//        In the future try to make the toolbar programatically
//        easier to make it functional
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Main Menu");
        setSupportActionBar(toolbar);
//functional
        this.expensesIntent = new Intent(this, ExpensesActivity.class);
        this.addExpenseIntent = new Intent(this, AddExpenseActivity.class);
        //not on alpha phase
//        this.analyticsIntent = new Intent(this,AnalyticsActivity.class);
//        this.archiveIntent = new Intent(this,ArchiveActivity.class);

//functional
        this.expensesButton = (Button) findViewById(R.id.expensesButton);
        this.addExpenseButton = (Button) findViewById(R.id.addExpenseButton);
        this.backupButton = (Button) findViewById(R.id.backupButton);
        //not on alpha phase
//        this.analyticsButton = (Button) findViewById(R.id.analyticsButton);
//        this.archiveButton = (Button) findViewById(R.id.archiveButton);
    }

    /**
     * sets the listeners of the buttons
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
                String filePath = backupToCSV();
                Toast.makeText(getApplicationContext(), "Backup is successfully done! Please enjoy :D\n" +
                        "File directory: " + filePath, Toast.LENGTH_LONG).show();
            }
        });

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

    /**
     * backups all expenses to a CSV file
     */
    public String backupToCSV() {
        String filePath = "";
        Date date = new Date();
        try {
            File file = new File(this.getApplicationContext().getFilesDir(), "backup.csv");
            if (!file.exists())
                file.createNewFile();
            else {
                for (int i = 0; ; i++) {
                    file = new File(this.getApplicationContext().getFilesDir(), "backup(" + i + ").csv");
                    if (!file.exists())
                        break;
                }
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            ExpenseDataSource dataSource = new ExpenseDataSource(this.getApplicationContext());
            dataSource.open();
            List<Expense> list = dataSource.getAllExpenses();
            for (Expense e : list) {
                bw.write(e.toString() + "\n");
            }
            filePath = file.getAbsolutePath();
            dataSource.close();
            bw.flush();
            bw.close();
        } catch (IOException io) {
            io.getStackTrace();
        }
        return filePath;
    }

}
