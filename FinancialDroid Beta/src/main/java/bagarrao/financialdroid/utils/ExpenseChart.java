package bagarrao.financialdroid.utils;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

import bagarrao.financialdroid.database.DataManager;

/**
 * Created by eduar on 29/01/2018.
 */

public class ExpenseChart extends PieChart{

    private DataManager manager = DataManager.getInstance();

    private int year;
    private int month;
    private PieDataSet dataSet;
    private PieData data;
    private List<PieEntry> entryList;

    public ExpenseChart(Context context, int month, int year) {
        super(context);
        this.year = year;
        this.month = month;
        this.entryList = getEntries();
        setupChart();
    }

    public ExpenseChart(Context context, int year) {
        super(context);
        this.year = year;
        this.month = -1;
        this.entryList = getEntries();
        setupChart();
    }

    public List<PieEntry> getEntries(){
        List<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(1,2));
        entries.add(new PieEntry(2,3));
        entries.add(new PieEntry(3,4));
        entries.add(new PieEntry(4,5));
        entries.add(new PieEntry(5,6));
        entries.add(new PieEntry(6,7));
        return entries;
    }

    private void setupChart(){
        this.dataSet = new PieDataSet(entryList,"Expense Types");
        this.data = new PieData(dataSet);
        setData(data);
    }

    @Override
    public void invalidate() {
        this.entryList = getEntries();
        setupChart();
        super.invalidate();
    }
}
