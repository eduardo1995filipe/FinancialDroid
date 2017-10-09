package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Calendar;
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

public class MonthAnalyticsActivity extends AppCompatActivity {

    private ExpenseDataSource expenseDataSource;
    private ArchiveDataSource archiveDataSource;

    private ScrollView scrollView;
    private LinearLayout linearLayout;

    private List<Expense> expenseList;
    private List<PieChart> pieChartList;

    private HashMap<Integer,List<Expense>> expensesByMonthMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        readDB();
        setup();
        setContentView(R.layout.activity_month_analytics);
    }

    @Override
    protected void onResume() {
        super.onResume();
        expenseDataSource.open();
        archiveDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        expenseDataSource.close();
        archiveDataSource.close();
    }

    public void init(){
        this.scrollView = new ScrollView(this);
        this.linearLayout  = new LinearLayout(this);

        this.expenseDataSource = new ExpenseDataSource(this);
        this.archiveDataSource = new ArchiveDataSource(this);

        this.expenseList = new LinkedList<>();
        this.pieChartList = new LinkedList<>();

        this.expensesByMonthMap = new HashMap<>();
    }

    public void setup(){
        for(int i = 0; i < 12 ; i++){
            expensesByMonthMap.put(0,new LinkedList<Expense>());
        }
    }

    public void readDB(){
        List<Expense> expenseViewerList = expenseDataSource.getAllExpenses();
        List<Expense> archiveList = archiveDataSource.getAllExpenses();
        for(Expense e : expenseViewerList)
            expenseList.add(e);
        for(Expense e : archiveList)
            if(new DateForCompare(e.getDate()).getYear() == new DateForCompare(new Date()).getYear())
                archiveList.add(e);
            else
                continue;
    }

    public void distributeExpenses(){
        for(Expense e : expenseList){
            DateForCompare dateForCompare = new DateForCompare(e.getDate());
            expensesByMonthMap.get(dateForCompare.getMonth()).add(e);
        }
    }

    public void loadCharts(){
        Iterator it = expensesByMonthMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //code here
            List<Expense> list = (List<Expense>) pair.getValue();
            for(Expense e: list){
                PieChart pieChart = new PieChart(this);
                //TODO: fazer aqui os graficos
                pieChartList.add(pieChart);
            }
            //end code here
            it.remove();
        }
    }

    public void setLayout(){
        setContentView(scrollView);
    }
}
