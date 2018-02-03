package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.DataManager;
import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.chart.ChartGenerator;
import bagarrao.financialdroid.utils.DateParser;
import bagarrao.financialdroid.utils.Filter;

public class YearAnalyticsActivity extends AppCompatActivity {

    private DataManager dataManager = DataManager.getInstance();

    private LinearLayout mainLayout;
    private List<Expenditure> totalExpenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_analytics);
        init();
        loadCharts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCharts();
    }

    public void init() {
        this.mainLayout = (LinearLayout) findViewById(R.id.yearAnalyticsMainLayout);
        this.totalExpenseList = dataManager.selectAll();
    }

    public void readDB() {
        totalExpenseList = dataManager.selectAll();
    }

    public void loadCharts() {
        mainLayout.removeAllViews();
        readDB();
        HashSet<Integer> set = new HashSet<>();
        for (Expenditure e : totalExpenseList) {
            Log.d("YearAnalyticsActivity","Added expenditure --> " + e.toString());
            set.add(DateParser.getYear(e.getDate()));
        }
        int layoutCount = 0;

        for (Integer year : set) {
            Log.d("YearAnalyticsActivity","Integer used --> " + year);
            List<Expenditure> list = Filter.filterExpensesByYear(totalExpenseList, year);
            if(!list.isEmpty()){
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        600,
                        1);
//                param.height = 500;
//                param.width = 500;
                param.gravity = Gravity.CENTER;
                layout.setLayoutParams(param);
                layout.setWeightSum(1);
                PieChart chart = ChartGenerator.generatePieChart(getApplicationContext(),year);
                chart.setMinimumWidth(550);
                chart.setMinimumHeight(550);
                chart.setPadding(10,10,10,10);
                TextView monthTextView = new TextView(this);
                monthTextView.setPadding(15,0,0,0);
                layout.setPadding(0,20,0,0);
                monthTextView.setText(Integer.toString(year));
                layout.addView(monthTextView,0);
                layout.addView(chart,1);
                mainLayout.addView(layout,layoutCount++);
            }
        }
    }
}

