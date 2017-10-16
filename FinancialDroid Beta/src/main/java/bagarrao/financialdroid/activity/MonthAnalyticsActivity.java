package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import bagarrao.financialdroid.utils.Pair;
import bagarrao.financialdroid.utils.PieChartHelper;

public class MonthAnalyticsActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private LinearLayout mainLayout;

    private ExpenseDataSource expenseDataSource;
    private ArchiveDataSource archiveDataSource;

    private List<LinearLayout> pieChartLayoutList;
    private HashMap<Integer, List<Expense>> expensesByMonthMap;

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
        expenseDataSource.close();
        archiveDataSource.close();
    }

    public void init() {
        this.scrollView = new ScrollView(this);
        this.mainLayout = new LinearLayout(this);

        this.expenseDataSource = new ExpenseDataSource(this);
        this.archiveDataSource = new ArchiveDataSource(this);

        this.pieChartLayoutList = new LinkedList<>();

        this.expensesByMonthMap = new HashMap<>();

    /*              SETUP               */

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 12; i++) {
            expensesByMonthMap.put(0, new LinkedList<Expense>());
        }

    }
    public void readDB() {
        expensesByMonthMap.clear();

        List<Expense> expenseViewerList = expenseDataSource.getAllExpenses();
        List<Expense> archiveList = archiveDataSource.getAllExpenses();
        List<Expense> totalExpenseList = new ArrayList<>();

        for (Expense e : expenseViewerList)
            totalExpenseList.add(e);
        for (Expense e : archiveList)
            if (new DateForCompare(e.getDate()).getYear() == new DateForCompare(new Date()).getYear())
                totalExpenseList.add(e);
            else
                continue;

        for (Expense e : totalExpenseList) {
            DateForCompare dateForCompare = new DateForCompare(e.getDate());
            expensesByMonthMap.get(dateForCompare.getMonth()).add(e);
        }
    }

    public void loadCharts() {
        scrollView.removeAllViews();
        mainLayout.removeAllViews();

        if(!expenseDataSource.isOpen())
            expenseDataSource.open();
        if(!archiveDataSource.isOpen())
            archiveDataSource.open();
        readDB();

        Iterator it = expensesByMonthMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            pieChartLayoutList.add(getChartLayout((Integer) pair.getKey(), getChartByPair(pair)));
            it.remove();
        }
        int i = 0;
        for(LinearLayout l : pieChartLayoutList){
            mainLayout.addView(l,i++);
        }
        scrollView.addView(mainLayout,0);
    }

    public PieChart getChartByPair(Map.Entry<Integer, List<Expense>> pair) {
        DateForCompare dateForCompare = new DateForCompare(new Date());
        Pair<List<Entry>, List<String>> pair_2 = PieChartHelper.setExpensesAmount(getApplicationContext(),pair.getKey(),dateForCompare.getYear());
        PieChart pieChart = PieChartHelper.generatePieChart(this, R.id.lastMonthPieChart, pair_2.getKey(), pair_2.getValue());
        return pieChart;
    }

    public LinearLayout getChartLayout(int monthNum, PieChart chart) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int index = 0;
        TextView textView = new TextView(this);
        textView.setText(new DateFormatSymbols().getMonths()[monthNum - 1]);
        layout.addView(textView, index++);
        layout.addView(chart, index++);
        return layout;
    }
}
