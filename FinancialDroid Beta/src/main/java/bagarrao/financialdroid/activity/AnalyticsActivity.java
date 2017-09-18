package bagarrao.financialdroid.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.DateForCompare;
import bagarrao.financialdroid.utils.Filter;

public class AnalyticsActivity extends AppCompatActivity {

    private static final String TAG = "AnalyticsActivity";

    private ExpenseDataSource dataSource;

    private float[] yData = new float[ExpenseType.values().length];
    private String[] xData = new String[ExpenseType.values().length];
    private PieChart pieChart;

    private List<Expense> lastMonthExpenses;

    private double getAmount(List<Expense> list){
        return 10;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        dataSource = new ExpenseDataSource(this.getApplicationContext());
        dataSource.open();
        readDB();
        xData[0] = ExpenseType.FEEDING.toString();
        xData[1] = ExpenseType.TRANSPORTS.toString();
        xData[2] = ExpenseType.SCHOOL.toString();
        xData[3] = ExpenseType.CLOTHING.toString();
        xData[4] = ExpenseType.OTHERS.toString();

        yData[0] = (float) getAmount(Filter.getExpensesByType(lastMonthExpenses,ExpenseType.FEEDING));
        yData[1] = (float) getAmount(Filter.getExpensesByType(lastMonthExpenses,ExpenseType.TRANSPORTS));
        yData[2] = (float) getAmount(Filter.getExpensesByType(lastMonthExpenses,ExpenseType.SCHOOL));
        yData[3] = (float) getAmount(Filter.getExpensesByType(lastMonthExpenses,ExpenseType.CLOTHING));
        yData[4] = (float) getAmount(Filter.getExpensesByType(lastMonthExpenses,ExpenseType.OTHERS));

        //PIE CHART///////////////////////////////////////////////////////////////////////////////

        Log.d(TAG, "onCreate: starting to create chart");

        pieChart = (PieChart) findViewById(R.id.lastMonthPieChart);

        Description description = new Description();
        description.setText("Sales by employee (In Thousands $) ");
        pieChart.setDescription(description);
        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Last month expenses");
        pieChart.setCenterTextSize(10);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!

        addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                int pos1 = e.toString().indexOf("(sum):   ");
                String sales = e.toString().substring(pos1 + 7);

                for(int i = 0; i < yData.length; i++){
                    if(yData[i] == Float.parseFloat(sales)){
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1 + 1];
                Toast.makeText(AnalyticsActivity.this, "Employee " + employee + "\n" + "Sales: $" + sales + "K", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });


        //END PIE CHART///////////////////////////////////////////////////////////////////////////////

    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        readDB();
        super.onResume();
    }

    private void addDataSet() {
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Employee Sales");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public void readDB() {
        this.lastMonthExpenses = dataSource.getAllExpenses();
    }

}
