package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.Filter;

public class AnalyticsActivity extends AppCompatActivity {

    private static final String ACTIVITY_TITLE = "Analytics";

    private PieChart pieChart;
    private PieDataSet dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(ACTIVITY_TITLE);
        setSupportActionBar(toolbar);

        this.pieChart = (PieChart) findViewById(R.id.lastMonthPieChart);

        setAmountsByType();

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("FEEDING");
        labels.add("TRANSPORTS");
        labels.add("SCHOOL");
        labels.add("CLOTHING");
        labels.add("OTHERS");

        PieData data = new PieData(labels, dataset);
        dataset.setColors(ColorTemplate.PASTEL_COLORS);
        pieChart.setDescription("");
        pieChart.setData(data);

        Legend legend = pieChart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);


        pieChart.animateY(5000);


//        pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image




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
