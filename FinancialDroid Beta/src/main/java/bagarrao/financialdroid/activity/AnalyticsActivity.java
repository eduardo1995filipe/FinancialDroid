package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Date;
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.utils.DateForCompare;
import bagarrao.financialdroid.utils.Pair;
import bagarrao.financialdroid.utils.PieChartHelper;

/**
 * @author Eduardo Bagarrao
 */
public class AnalyticsActivity extends AppCompatActivity {

    private AdView adView;
    private AdRequest adRequest;

    private PieChart pieChart;

    private Intent monthAnalyticsIntent;
    private Intent yearAnalyticsIntent;

    private Button monthAnalyticsButton;
    private Button yearAnalyticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        init();
        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DateForCompare dateForCompare = new DateForCompare(new Date());
        PieChartHelper.setExpensesAmount(getApplicationContext(), dateForCompare.getMonth(), dateForCompare.getYear());
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    /**
     * inits all the class objects
     */
    private void init(){
        MobileAds.initialize(this, "ca-app-pub-8899468184876323/9793116541");

        this.adView = (AdView) findViewById(R.id.analyticsAdBanner);
        this.adRequest = new AdRequest.Builder().build();

        this.monthAnalyticsButton = (Button) findViewById(R.id.monthAnalyticsButton);
        this.yearAnalyticsButton = (Button) findViewById(R.id.yearAnalyticsButton);

        this.monthAnalyticsIntent = new Intent(this,MonthAnalyticsActivity.class);
        this.yearAnalyticsIntent = new Intent(this,YearAnalyticsActivity.class);
    }

    /**
     * setups all the  class objects
     */
    private void setup(){
        this.adView.loadAd(adRequest);
        DateForCompare dateForCompare = new DateForCompare(new Date());
        Pair<List<Entry>, List<String>> pair = PieChartHelper.setExpensesAmount(getApplicationContext(),dateForCompare.getMonth(),dateForCompare.getYear());
        this.pieChart = PieChartHelper.generatePieChart(this, R.id.lastMonthPieChart, pair.getKey(), pair.getValue());

        this.pieChart.animateY(2500);

        monthAnalyticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(monthAnalyticsIntent);
            }
        });

        yearAnalyticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(yearAnalyticsIntent);
            }
        });
    }
}
