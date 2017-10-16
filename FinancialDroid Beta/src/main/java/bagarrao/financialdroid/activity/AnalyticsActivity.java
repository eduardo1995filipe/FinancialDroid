package bagarrao.financialdroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.DateForCompare;
import bagarrao.financialdroid.utils.Filter;
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

        this.yearAnalyticsIntent = new Intent(this,MonthAnalyticsActivity.class);
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
                startActivity(yearAnalyticsIntent);
            }
        });
    }

//    /**
//     * return the total value of the ExpenseType given in the parameter
//     * @param type type of the
//     * @return total value of the expenses of that ExpenseType
//     */
//    private float getExpensesAmountByType(ExpenseType type){
//        this.dataSource = new ExpenseDataSource(this);
//        dataSource.open();
//        List<Expense> expenses = Filter.getExpensesByType(dataSource.getAllExpenses(),type);
//        double amount = 0;
//        for(Expense e : expenses)
//            amount += e.getValue();
//        return (float)amount;
//    }

//    /**
//     * set the expenses amount for the entries of the chart
//     */
//    private void setExpensesAmount() {
//        if (entries.size() != 0)
//            entries.clear();
//        if (labels.size() != 0)
//            labels.clear();
//        int i = 0;
//        for (ExpenseType type : ExpenseType.values()) {
//            float amount = getExpensesAmountByType(type);
//            if (amount > 0) {
//                labels.add(type.toString());
//                entries.add(new Entry(amount, i));
//                i++;
//            } else
//                continue;
//        }
//    }
}
