package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.ArchiveDataSource;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.utils.DateForCompare;
import bagarrao.financialdroid.utils.Filter;
import bagarrao.financialdroid.utils.Pair;
import bagarrao.financialdroid.utils.PieChartHelper;

public class MonthAnalyticsActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private LinearLayout mainLayout;

    private ArchiveDataSource archiveDataSource;

    private List<LinearLayout> pieChartLayoutList;
    private List<Expense> totalExpenseList;

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
        archiveDataSource.close();
    }

    public void init() {
        this.scrollView = new ScrollView(this);
        this.mainLayout = new LinearLayout(this);
//        this.expenseDataSource = new ExpenseDataSource(this);
        this.archiveDataSource = new ArchiveDataSource(this);
        this.pieChartLayoutList = new LinkedList<>();
        this.totalExpenseList = new ArrayList<>();
        mainLayout.setOrientation(LinearLayout.VERTICAL);
    }

    public void readDB() {
        List<Expense> archiveList = archiveDataSource.getAllExpenses();
        List<Expense> newTotalExpenseList = new ArrayList<>();

        List<Expense> filteredList = Filter.filterExpensesByYear(archiveList,new DateForCompare(new Date()).getYear());
        for (Expense e : filteredList)
                newTotalExpenseList.add(e);
        totalExpenseList = newTotalExpenseList;
    }

    public void loadCharts() {
        scrollView.removeAllViews();
        mainLayout.removeAllViews();
        pieChartLayoutList.clear();
        totalExpenseList.clear();

        if(!archiveDataSource.isOpen())
            archiveDataSource.open();
        readDB();
        DateForCompare dfc = new DateForCompare(new Date());
        for(int i = 0;i < dfc.getMonth(); i++){
            List<Expense> list = Filter.filterExpensesByMonth(totalExpenseList,i);
            pieChartLayoutList.add(getChartLayout(i, new DateForCompare(new Date()).getYear(),list));
        }
        int i = 0;
        scrollView.addView(mainLayout,0);
        for(LinearLayout l : pieChartLayoutList){
            mainLayout.addView(l,i++);
            Log.d("MonthAnalyticsActivity","Added layout --> " + i);
        }
    }

    public LinearLayout getChartLayout(int monthNum,int yearNum, List<Expense> list) {

        LinearLayout layout = new LinearLayout(this);
        layout.setWeightSum(1);
        layout.setOrientation(LinearLayout.VERTICAL);

        //piechart

        Pair<List<Entry>, List<String>> pair_2 = PieChartHelper.setExpensesAmount(getApplicationContext(),monthNum,yearNum); //critico
        PieChart pieChart = PieChartHelper.generatePieChart(getApplicationContext(), pair_2.getKey(), pair_2.getValue());
        PieChart.LayoutParams  params = new PieChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
        pieChart.setLayoutParams(params);

        //end piechart

        int index = 0;
        TextView textView = new TextView(this);
        textView.setText(new DateFormatSymbols().getMonths()[monthNum]);
        layout.addView(textView, index++);
        layout.addView(pieChart, index++);
        Log.d("PieChartHelper","Chart is null --> " + (pieChart == null));
        return layout;
    }
}