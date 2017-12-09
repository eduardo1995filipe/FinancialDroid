package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.ArchiveDataSource;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.utils.DateForCompare;
import bagarrao.financialdroid.utils.Filter;
import bagarrao.financialdroid.utils.Pair;
import bagarrao.financialdroid.utils.PieChartHelper;

/**
 * @author Eduardo Bagarrao
 */
public class YearAnalyticsActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private LinearLayout mainLayout;

    private ArchiveDataSource archiveDataSource;
    private ExpenseDataSource expenseDataSource;

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
        expenseDataSource.close();
    }

    public void init() {
        this.scrollView = new ScrollView(this);
        this.mainLayout = new LinearLayout(this);
        this.expenseDataSource = new ExpenseDataSource(this);
        this.archiveDataSource = new ArchiveDataSource(this);
        this.pieChartLayoutList = new LinkedList<>();
        this.totalExpenseList = new ArrayList<>();
        mainLayout.setOrientation(LinearLayout.VERTICAL);
    }

    public void readDB() {
        List<Expense> archiveList = archiveDataSource.getAllExpenses();
        List<Expense> expenseList = expenseDataSource.getAllExpenses();
        List<Expense> newTotalExpenseList = new ArrayList<>();

        for (Expense e : archiveList)
            newTotalExpenseList.add(e);
        for (Expense e : expenseList)
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
        if(!expenseDataSource.isOpen())
            expenseDataSource.open();
        readDB();

        HashSet<Integer> set = new HashSet<>();
        //ver todos os anos que existem
        for(Expense e : totalExpenseList){
            set.add(new DateForCompare(e.getDate()).getYear());
        }

        for(Integer year : set){
            List<Expense> list = Filter.filterExpensesByYear(totalExpenseList,year);
            pieChartLayoutList.add(getChartLayout(year,list)); //to add
        }

        int i = 0;
        scrollView.addView(mainLayout,0);
        TextView text = new TextView(this);
        text.setText("Here you can check your expenses along all the years!!\n\n");
        mainLayout.addView(text,i++);

        for(LinearLayout l : pieChartLayoutList){
            mainLayout.addView(l,i++);
            Log.d("YearAnalyticsActivity","Added layout --> " + i);
        }
    }

    public LinearLayout getChartLayout(int yearNum, List<Expense> list) {

        LinearLayout layout = new LinearLayout(this);
        layout.setWeightSum(1);
        layout.setOrientation(LinearLayout.VERTICAL);

        //piechart

        //ERRO PROVAVEL VIR DA LINHA DEBAIXO OU A DUAS LINHAS ABAIXO
        Pair<List<Entry>, List<String>> pair_2 = PieChartHelper.setExpensesAmount(getApplicationContext(),yearNum); //critico
        PieChart pieChart = PieChartHelper.generatePieChart(getApplicationContext(), pair_2.getKey(), pair_2.getValue());
        PieChart.LayoutParams  params = new PieChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
        pieChart.setLayoutParams(params);

        //end piechart

        int index = 0;
        TextView textView = new TextView(this);
        textView.setText("" + yearNum);
        layout.addView(textView, index++);
        layout.addView(pieChart, index++);
        Log.d("PieChartHelper","Chart is null --> " + (pieChart == null));
        return layout;
    }}
