package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.database.DataManager;
//import bagarrao.financialdroid.database.DataSource;
import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.utils.DateParser;
import bagarrao.financialdroid.utils.Filter;
import bagarrao.financialdroid.utils.Pair;
import bagarrao.financialdroid.utils.PieChartHelper;

/**
 * @authos Eduardo
 */
public class MonthAnalyticsActivity extends AppCompatActivity {

    private DataManager dataManager = DataManager.getInstance();

//    private DataSource archiveDataSource;

    private ScrollView scrollView;
    private LinearLayout mainLayout;

    private List<LinearLayout> pieChartLayoutList;
    private List<Expenditure> totalExpenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        loadCharts();
        setContentView(scrollView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCharts();
    }

    /**
     *
     */
    public void init() {
        this.scrollView = new ScrollView(this);
        this.mainLayout = new LinearLayout(this);
        this.pieChartLayoutList = new LinkedList<>();
        this.totalExpenseList = new ArrayList<>();
        mainLayout.setOrientation(LinearLayout.VERTICAL);
    }

    /**
     *
     */
    public void readDB() {
        totalExpenseList.clear();
        //TODO: retirar o mes atual
        totalExpenseList = Filter.filterExpensesByYear(dataManager.selectAllOlder(), DateParser.getYear(new Date())); //critico
        }

    /**
     *
     */
    public void loadCharts() {
        scrollView.removeAllViews();
        mainLayout.removeAllViews();
        pieChartLayoutList.clear();

        readDB();
        Date date = new Date();
        for(int i = 0;i < DateParser.getMonth(date); i++){
            List<Expenditure> list = Filter.filterExpensesByMonth(totalExpenseList,i);
            if(!list.isEmpty())
                pieChartLayoutList.add(getChartLayout(i, DateParser.getYear(date),list));
        }
        int i = 0;
        scrollView.addView(mainLayout,0);
        TextView text = new TextView(this);
        text.setText("Here you can check your expenses along the months of "
                + DateParser.getYear(date) + "!!\n\n");
        mainLayout.addView(text,i++);
        for(LinearLayout l : pieChartLayoutList){
            mainLayout.addView(l,i++);
            Log.d("MonthAnalyticsActivity","Added layout --> " + i);
        }
    }

    /**
     *
     * @param monthNum
     * @param yearNum
     * @param list
     * @return
     */
    public LinearLayout getChartLayout(int monthNum,int yearNum, List<Expenditure> list) {
        LinearLayout layout = new LinearLayout(this);
        layout.setWeightSum(1);
        layout.setOrientation(LinearLayout.VERTICAL);

        Pair<List<Entry>, List<String>> pair_2 = PieChartHelper.setExpensesAmount(getApplicationContext(),monthNum,yearNum); //critico
        PieChart pieChart = PieChartHelper.generatePieChart(getApplicationContext(), pair_2.getKey(), pair_2.getValue());
        PieChart.LayoutParams  params = new PieChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
        pieChart.setLayoutParams(params);

        int index = 0;
        TextView textView = new TextView(this);
        textView.setText(new DateFormatSymbols().getMonths()[monthNum]);
        layout.addView(textView, index++);
        layout.addView(pieChart, index++);
        Log.d("PieChartHelper","Chart is null --> " + (pieChart == null));
        return layout;
    }
}