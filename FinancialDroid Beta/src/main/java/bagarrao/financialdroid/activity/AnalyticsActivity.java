package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.DataManager;
import bagarrao.financialdroid.utils.ChartGenerator;
import bagarrao.financialdroid.utils.DateParser;
import bagarrao.financialdroid.utils.ExpenseChart;
import bagarrao.financialdroid.utils.Pair;
import bagarrao.financialdroid.utils.PieChartHelper;
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
//    private Pair<List<Entry>, List<String>> pair;
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
        pieChart.animateY(2500);
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
        this.date = new Date();
//        List<PieEntry> entries = new ArrayList<PieEntry>();
//        entries.add(new PieEntry(1,"Teste1",2));
//        entries.add(new PieEntry(2,"Teste2",3));
//        entries.add(new PieEntry(3,"Teste3",4));
//        entries.add(new PieEntry(4,"Teste4",5));
//        entries.add(new PieEntry(5,"Teste5",6));
//        entries.add(new PieEntry(6,"Teste6",1));
        pieChart = ChartGenerator.generatePieChart(
                getApplicationContext(),
                DateParser.getMonth(date),
                DateParser.getYear(date));
        Log.d("AnalyticsActivity","current month: " +
                DateParser.getMonth(date) + ", year: " +
                DateParser.getYear(date));

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setWordWrapEnabled(true);

        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawSliceText(true);
        layout.removeAllViews();
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        pieChart.invalidate();
        pieChart.setLayoutParams(param);
        layout.addView(pieChart,0);
    }
}
