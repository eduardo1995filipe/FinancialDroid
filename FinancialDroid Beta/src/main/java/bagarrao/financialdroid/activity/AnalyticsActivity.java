package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Date;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.DataManager;
import bagarrao.financialdroid.chart.ChartGenerator;
import bagarrao.financialdroid.utils.DateParser;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Eduardo Bagarrao
 */
public class AnalyticsActivity extends AppCompatActivity {

    private DataManager manager = DataManager.getInstance();
    @BindView(R.id.monthAnalyticsButton)
    Button monthAnalyticsButton;
    @BindView(R.id.yearAnalyticsButton)
    Button yearAnalyticsButton;
    @BindView(R.id.analyticsAdBanner)
    AdView adView;
    @BindView(R.id.chartLayout)
    RelativeLayout layout;
    private AdRequest adRequest;
    private Date date;
    private PieChart pieChart;
    private Intent monthAnalyticsIntent;
    private Intent yearAnalyticsIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        ButterKnife.bind(this);
        init();
        adView.loadAd(adRequest);
        loadChart();
        monthAnalyticsButton.setOnClickListener(v -> startActivity(monthAnalyticsIntent));
        yearAnalyticsButton.setOnClickListener(v -> startActivity(yearAnalyticsIntent));
    }

    /**
     * inits all the class objects
     */
    private void init(){

        MobileAds.initialize(this, "ca-app-pub-8899468184876323/9793116541");
        this.adRequest = new AdRequest.Builder().build();
        this.monthAnalyticsIntent = new Intent(this,MonthAnalyticsActivity.class);
        this.yearAnalyticsIntent = new Intent(this,YearAnalyticsActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadChart();
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    public void loadChart(){
        layout.removeAllViews();
        this.date = new Date();
        pieChart = ChartGenerator.generatePieChart(
                getApplicationContext(),
                DateParser.getMonth(date),
                DateParser.getYear(date));
        Log.d("AnalyticsActivity","current month: " +
                DateParser.getMonth(date) + ", year: " +
                DateParser.getYear(date));
        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        pieChart.setLayoutParams(param);
        layout.addView(pieChart);
    }
}

