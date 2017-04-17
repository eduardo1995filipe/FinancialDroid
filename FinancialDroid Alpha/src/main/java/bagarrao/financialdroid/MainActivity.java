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
    //    not on alpha phase
    private Intent analyticsIntent;
    private Intent archiveIntent;

    //    functional
    private Button expensesButton;  //to check your expenses
    private Button addExpenseButton;    //to register a new Expense
    private Button infoButton;  //to show info about the app
//    not on alpha phase
//    private Button archiveButton; //TODO archive functionality
//    private Button analyticsButton; //TODO analytics functionality

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
//        not on alpha phase
//        this.analyticsIntent = new Intent(this,AnalyticsActivity.class);
//        this.archiveIntent = new Intent(this,ArchiveActivity.class);

//        functional
        this.expensesButton = (Button) findViewById(R.id.expensesButton);
        this.addExpenseButton = (Button) findViewById(R.id.addExpenseButton);
        this.infoButton = (Button) findViewById(R.id.infoButton);
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
}
