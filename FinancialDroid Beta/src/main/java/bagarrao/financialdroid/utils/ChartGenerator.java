package bagarrao.financialdroid.utils;

import android.content.Context;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.database.DataManager;
import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.expense.ExpenseType;

/**
 * Created by eduar on 30/01/2018.
 */

public class ChartGenerator {

    public static PieChart generatePieChart(Context context, int month, int year){
        PieChart pieChart = new PieChart(context);
        PieDataSet dataSet = new PieDataSet(generateEntries(month, year),"Expense Types");
        dataSet.setValueTextSize(dataSet.getValueTextSize() + 5);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        return pieChart;
    }

    public static PieChart generatePieChart(Context context, int year){
        PieChart pieChart = new PieChart(context);
        PieDataSet dataSet = new PieDataSet(generateEntries(year),"Expense Types");
        dataSet.setValueTextSize(dataSet.getValueTextSize() + 5);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        return pieChart;
    }

    public static List<PieEntry> generateEntries(int month, int year){
        List<Expenditure> list = DataManager.getInstance().selectAll();
        List<PieEntry> entryList = new LinkedList<>();
        list = Filter.filterExpensesByYear(list,year);
        list = Filter.filterExpensesByMonth(list,month);
        int i = 0;
        for(ExpenseType expenseType : ExpenseType.values()) {
            List<Expenditure> newList = list;
            //label
            newList = Filter.filterExpensesByType(newList,expenseType);
            //sum
            float sum = 0;
            for (Expenditure e : newList) {
               sum += e.getValue();
            }
            if(sum > 0)
            entryList.add(new PieEntry(sum, expenseType.toString(),i++));
        }
        return entryList;
    }

    public static List<PieEntry> generateEntries(int year){
        List<Expenditure> list = DataManager.getInstance().selectAll();
        List<PieEntry> entryList = new LinkedList<>();
        list = Filter.filterExpensesByYear(list,year);
        int i = 0;
        for(ExpenseType expenseType : ExpenseType.values()) {
            List<Expenditure> newList = list;
            //label
            newList = Filter.filterExpensesByType(newList,expenseType);
            //sum
            float sum = 0;
            for (Expenditure e : newList) {
                sum += e.getValue();
            }
            if(sum > 0)
                entryList.add(new PieEntry(sum, expenseType.toString(),i++));
        }
        return entryList;
    }
}
