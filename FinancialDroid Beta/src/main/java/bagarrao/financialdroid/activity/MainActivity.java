package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import bagarrao.financialdroid.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Eduardo Bagarrao
 */
public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_TITLE = "FINANCIALDROID";
    private static final Class[] ACTIVITY_ARRAY = {ExpensesActivity.class, AddExpenseActivity.class, InfoActivity.class,
            AnalyticsActivity.class, ArchiveActivity.class, SettingsActivity.class};
    private static final int[] IMAGE_ID_ARRAY = {R.id.expenseViewerImage,R.id.addExpenseImage,R.id.infoImage,
            R.id.analyticsImage,R.id.archiveImage,R.id.settingsImage};
    private static final int NUM_OPTIONS = 6;

    private Intent[] optionsIntent;
    private ImageView[] optionImages;

    @BindView(R.id.appBarMainMenu)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.adView)
    AdView adView;

    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        init();
        setup();
    }

    /**
     * initializes all the elements
     */
    public void init() {
        this.optionImages = new ImageView[NUM_OPTIONS];
        this.optionsIntent = new Intent[NUM_OPTIONS];

        for(int i = 0;i < NUM_OPTIONS;i++){

            final int index = i;

            optionsIntent[i] = new Intent(this, ACTIVITY_ARRAY[i]);
            optionImages[i] = (ImageView) findViewById(IMAGE_ID_ARRAY[i]);
            optionImages[i].setOnClickListener(v -> startActivity(optionsIntent[index]));
        }
        this.adRequest = new AdRequest.Builder().build();
    }

    /**
     * setup of all class objects
     */
    public void setup(){
        MobileAds.initialize(this, "ca-app-pub-8899468184876323/4720328233");
        adView.loadAd(adRequest);

//        toolbar.setTitle(ACTIVITY_TITLE);
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.BLACK);

        appBarLayout.setBackgroundColor(Color.WHITE);
    }

}
