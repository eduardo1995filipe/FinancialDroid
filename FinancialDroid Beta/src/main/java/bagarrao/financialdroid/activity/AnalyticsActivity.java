package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.Filter;

public class AnalyticsActivity extends AppCompatActivity {

    private AdView adView;

    private PieChart pieChart;
    private PieDataSet dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        MobileAds.initialize(this, "ca-app-pub-8899468184876323/9793116541");

        this.adView = (AdView) findViewById(R.id.analyticsAdBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);

        this.pieChart = (PieChart) findViewById(R.id.lastMonthPieChart);

        setAmountsByType();

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Feeding");
        labels.add("Transports");
        labels.add("School");
        labels.add("Clothing");
        labels.add("others");

        PieData data = new PieData(labels, dataset);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        pieChart.setDescription("");
        pieChart.setData(data);

        Legend legend = pieChart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);


        pieChart.animateY(2500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAmountsByType();
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    public float getExpensesAmountByType(ExpenseType type){
        ExpenseDataSource dataSource = new ExpenseDataSource(this);
        dataSource.open();
        List<Expense> expenses = Filter.getExpensesByType(dataSource.getAllExpenses(),type);
        double amount = 0;
        for(Expense e : expenses)
            amount += e.getValue();
        return (float)amount;
    }

    public void setAmountsByType(){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(getExpensesAmountByType(ExpenseType.FEEDING), 0));
        entries.add(new Entry(getExpensesAmountByType(ExpenseType.TRANSPORTS), 1));
        entries.add(new Entry(getExpensesAmountByType(ExpenseType.SCHOOL), 2));
        entries.add(new Entry(getExpensesAmountByType(ExpenseType.CLOTHING), 3));
        entries.add(new Entry(getExpensesAmountByType(ExpenseType.OTHERS), 4));

        dataset = new PieDataSet(entries, "Expense Types");
        dataset.setValueTextSize(dataset.getValueTextSize() + 5);
    }
}
