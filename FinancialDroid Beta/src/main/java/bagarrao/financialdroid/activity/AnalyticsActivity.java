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

/**
 * @author Eduardo Bagarrao
 */
public class AnalyticsActivity extends AppCompatActivity {

    private AdView adView;
    private AdRequest adRequest;

    private PieChart pieChart;
    private PieData data;
    private PieDataSet dataSet;
    private  Legend legend;

    private ArrayList<String> labels;
    private ArrayList<Entry> entries;

    private ExpenseDataSource dataSource;

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
        setExpensesAmount();
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
        this.pieChart = (PieChart) findViewById(R.id.lastMonthPieChart);

        this.entries = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    /**
     * setups all the  class objects
     */
    private void setup(){
        this.adView.loadAd(adRequest);

        setExpensesAmount();

//        this.labels.add("Feeding");
//        this.labels.add("Transports");
//        this.labels.add("School");
//        this.labels.add("Clothing");
//        this.labels.add("others");

        this.dataSet = new PieDataSet(entries, "Expense Types");
        this.dataSet.setValueTextSize(dataSet.getValueTextSize() + 5);

        this.data = new PieData(labels, dataSet);
        this.dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        this.pieChart.setDescription("");
        this.pieChart.setData(data);

        this.legend = pieChart.getLegend();
        this.legend.setWordWrapEnabled(true);
        this.legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        this.pieChart.animateY(2500);

    }

    /**
     * return the total value of the ExpenseType given in the parameter
     * @param type type of the
     * @return total value of the expenses of that ExpenseType
     */
    private float getExpensesAmountByType(ExpenseType type){
        this.dataSource = new ExpenseDataSource(this);
        dataSource.open();
        List<Expense> expenses = Filter.getExpensesByType(dataSource.getAllExpenses(),type);
        double amount = 0;
        for(Expense e : expenses)
            amount += e.getValue();
        return (float)amount;
    }

    /**
     * set the expenses amount for the entries of the chart
     */
    private void setExpensesAmount(){
        if(entries.size() != 0)
            entries.clear();
        if(labels.size() != 0)
            labels.clear();
        int i = 0;
		for(ExpenseType type : ExpenseType.values()){
			float amount = getExpensesAmountByType(type);
			if(amount > 0) {
                labels.add(type.toString());
                entries.add(new Entry(amount, i));
                i++;
            }
			else
				continue;
		}
	}
}
