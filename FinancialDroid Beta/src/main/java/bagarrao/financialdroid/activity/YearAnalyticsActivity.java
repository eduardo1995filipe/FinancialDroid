package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.database.DataManager;
import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.utils.DateParser;
import bagarrao.financialdroid.utils.Filter;
import bagarrao.financialdroid.utils.Pair;
import bagarrao.financialdroid.utils.PieChartHelper;

/**
 * @author Eduardo
 */
public class YearAnalyticsActivity extends AppCompatActivity {

    private DataManager dataManager = DataManager.getInstance();

//    private boolean isLocal;

    private ScrollView scrollView;
    private LinearLayout mainLayout;
//
//    private DataSource archiveDataSource;
//    private DataSource expenseDataSource;

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

    @Override
    protected void onPause() {
        super.onPause();
//        if(isLocal && expenseDataSource != null && archiveDataSource != null){
//            archiveDataSource.close();
//            expenseDataSource.close();
//        }
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
        totalExpenseList = dataManager.selectAll();
    }

    /**
     *
     */
    public void loadCharts() {
        scrollView.removeAllViews();
        mainLayout.removeAllViews();
        pieChartLayoutList.clear();
//
//        if(archiveDataSource != null && isLocal)
//            archiveDataSource.open();
//        if(archiveDataSource != null && isLocal)
//            expenseDataSource.open();
        readDB();

        HashSet<Integer> set = new HashSet<>();

        for(Expenditure e : totalExpenseList){
            set.add(DateParser.getYear(e.getDate()));
        }

        for(Integer year : set){
            List<Expenditure> list = Filter.filterExpensesByYear(totalExpenseList,year);
            pieChartLayoutList.add(getChartLayout(year,list));
        }

        int i = 0;
        scrollView.addView(mainLayout,0);
        TextView text = new TextView(this);
        text.setText("Here you can check your expenses along all the years!!\n\n");
        mainLayout.addView(text,i++);

        for(LinearLayout l : pieChartLayoutList){
            mainLayout.addView(l,i++);
        }
    }

    /**
     *
     * @param yearNum
     * @param list
     * @return
     */
    public LinearLayout getChartLayout(int yearNum, List<Expenditure> list) {

        LinearLayout layout = new LinearLayout(this);
        layout.setWeightSum(1);
        layout.setOrientation(LinearLayout.VERTICAL);

        Pair<List<Entry>, List<String>> pair_2 = PieChartHelper.setExpensesAmount(getApplicationContext(),yearNum); //critico
        PieChart pieChart = PieChartHelper.generatePieChart(getApplicationContext(), pair_2.getKey(), pair_2.getValue());
        PieChart.LayoutParams  params = new PieChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
        pieChart.setLayoutParams(params);

        int index = 0;
        TextView textView = new TextView(this);
        textView.setText("" + yearNum);
        layout.addView(textView, index++);
        layout.addView(pieChart, index++);
        return layout;
    }}
