package bagarrao.financialdroid.activity;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import bagarrao.financialdroid.R;

/**
 * @author Eduardo Bagarrao
 */
public class MainActivity extends AppCompatActivity {

    public static final String ACTIVITY_TITLE = "FINANCIALDROID";

    private Intent expensesIntent;
    private Intent addExpenseIntent;
    private Intent infoIntent;
    private Intent analyticsIntent;
    private Intent archiveIntent;
    private Intent settingsIntent;

    private ImageView expensesView;
    private ImageView addExpenseView;
    private ImageView infoView;
    private ImageView archiveView;
    private ImageView analyticsView;
    private ImageView settingsView;

    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(ACTIVITY_TITLE);
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.BLACK);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarMainMenu);
        appBarLayout.setBackgroundColor(Color.WHITE);


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
        this.settingsIntent =  new Intent(this, SettingsActivity.class);

        this.expensesView = (ImageView) findViewById(R.id.expenseViewerImage);
        this.addExpenseView = (ImageView) findViewById(R.id.addExpenseImage);
        this.analyticsView = (ImageView) findViewById(R.id.analyticsImage);
        this.archiveView = (ImageView) findViewById(R.id.archiveImage);
        this.infoView = (ImageView) findViewById(R.id.infoImage);
        this.settingsView = (ImageView) findViewById(R.id.settingsImage);
    }

    /**
     * sets the listeners of the views
     */
    public void setListeners() {
        expensesView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(expensesIntent);
                    }
                }
        );

        addExpenseView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(addExpenseIntent);
                    }
                }
        );

        infoView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(infoIntent);
                    }
                }
        );

        analyticsView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(analyticsIntent);
                    }
                }
        );

        archiveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(archiveIntent);
            }
        });

        settingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(settingsIntent);
            }
        });
    }
}
