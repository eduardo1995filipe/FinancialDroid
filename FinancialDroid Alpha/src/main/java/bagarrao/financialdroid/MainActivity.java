package bagarrao.financialdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

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
