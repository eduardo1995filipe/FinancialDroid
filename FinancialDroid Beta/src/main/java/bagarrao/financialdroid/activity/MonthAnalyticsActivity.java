package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.DataManager;
import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.chart.ChartGenerator;
import bagarrao.financialdroid.utils.DateParser;
import bagarrao.financialdroid.utils.Filter;

/**
 * @authos Eduardo
 */
public class MonthAnalyticsActivity extends AppCompatActivity {

    private DataManager dataManager = DataManager.getInstance();

    private LinearLayout mainLayout;
    private List<Expenditure> totalExpenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_analytics);
        init();
        loadCharts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCharts();
    }

    public void init() {
        this.mainLayout = (LinearLayout) findViewById(R.id.monthAnalyticsMainLayout);
        this.totalExpenseList = dataManager.selectAll();
    }

    public void readDB() {
        totalExpenseList = dataManager.selectAll();
        totalExpenseList = Filter.filterExpensesByYear(dataManager.selectAllOlder(), DateParser.getYear(new Date()));
    }

    public void loadCharts() {
        mainLayout.removeAllViews();
        readDB();
        Date date = new Date();
        int layoutCount = 0;
        TextView text = new TextView(this);
        text.setText("Here you can check your expenses along the months of "
                + DateParser.getYear(date) + "!!\n\n");
        mainLayout.addView(text,layoutCount++);
        for(int i = 0;i <= DateParser.getMonth(date); i++){
            Log.d("MonthAnalyticsActivity","Current i --> " + i);
            List<Expenditure> list = Filter.filterExpensesByMonth(totalExpenseList,i);
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
                PieChart chart = ChartGenerator.generatePieChart(getApplicationContext(),i,DateParser.getYear(date));
                chart.setMinimumWidth(550);
                chart.setMinimumHeight(550);
                chart.setPadding(10,10,10,10);
                TextView monthTextView = new TextView(this);
                monthTextView.setText(new DateFormatSymbols().getMonths()[i]);
                layout.addView(monthTextView,0);
                layout.addView(chart,1);
                mainLayout.addView(layout,layoutCount++);
            }
        }
    }
}