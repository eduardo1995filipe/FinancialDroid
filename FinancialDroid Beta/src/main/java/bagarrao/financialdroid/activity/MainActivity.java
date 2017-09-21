package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import bagarrao.financialdroid.R;

/**
 * @author Eduardo Bagarrao
 */
public class MainActivity extends AppCompatActivity {

    public static final String ACTIVITY_TITLE = "FinancialDroid";

    private Intent expensesIntent;
    private Intent addExpenseIntent;
    private Intent infoIntent;
    private Intent analyticsIntent;
    private Intent archiveIntent;

    //    functional
    private Button expensesButton;
    private Button addExpenseButton;
    private Button infoButton;
    private Button archiveButton;
    private Button analyticsButton;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(ACTIVITY_TITLE);
        MobileAds.initialize(this, "ca-app-pub-8899468184876323/4720328233");
        this.adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);

        setSupportActionBar(toolbar);
        init();
        setListeners();
    }

    /**
     * initializes all the elements
     */
    public void init() {


        this.expensesIntent = new Intent(this, ExpensesActivity.class);
        this.addExpenseIntent = new Intent(this, AddExpenseActivity.class);
        this.infoIntent = new Intent(this, InfoActivity.class);
        this.analyticsIntent = new Intent(this, AnalyticsActivity.class);
        this.archiveIntent = new Intent(this, ArchiveActivity.class);


        this.expensesButton = (Button) findViewById(R.id.expensesButton);
        this.addExpenseButton = (Button) findViewById(R.id.addExpenseButton);
        this.infoButton = (Button) findViewById(R.id.infoButton);
        this.analyticsButton = (Button) findViewById(R.id.analyticsButton);
        this.archiveButton = (Button) findViewById(R.id.archiveButton);
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

        analyticsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(analyticsIntent);
                    }
                }
        );

        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(archiveIntent);
            }
        });
    }
}
