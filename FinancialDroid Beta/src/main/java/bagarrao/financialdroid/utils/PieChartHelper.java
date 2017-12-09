package bagarrao.financialdroid.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bagarrao.financialdroid.database.ArchiveDataSource;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;


/**
 * Created by eduar on 14/10/2017.
 */

public class PieChartHelper {

    public static PieChart generatePieChart(Activity activity, int id, List<Entry> entries, List<String> labels){
        PieChart pieChart = (PieChart) activity.findViewById(id);
        PieDataSet dataSet = new PieDataSet(entries, "Expense Types");
        dataSet.setValueTextSize(dataSet.getValueTextSize() + 5);

        PieData data = new PieData(labels, dataSet);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieChart.setDescription("");
        pieChart.setData(data);

        Legend legend = pieChart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        pieChart.animateY(2500);

        return pieChart;
    }

    public static PieChart generatePieChart(Context context, List<Entry> entries, List<String> labels){
        PieChart pieChart = new PieChart(context);
        PieDataSet dataSet = new PieDataSet(entries, "Expense Types");
        dataSet.setValueTextSize(dataSet.getValueTextSize() + 5);

        PieData data = new PieData(labels, dataSet);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieChart.setDescription("");
        pieChart.setData(data);

        Legend legend = pieChart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        pieChart.animateY(2500);

        return pieChart;
    }

    /**
     * return the total value of the ExpenseType given in the parameter
     * @param type type of the
     * @return total value of the expenses of that ExpenseType
     */
    public static float getExpensesAmountByType(ExpenseType type, Context context, int month, int year){
        ExpenseDataSource expenseDataSource = new ExpenseDataSource(context);
        ArchiveDataSource archiveDataSource = new ArchiveDataSource(context);
        expenseDataSource.open();
        archiveDataSource.open();

        List<Expense> totalExpenseList = new ArrayList<>();
        for(Expense e : expenseDataSource.getAllExpenses()){
            totalExpenseList.add(e);
        }
        for(Expense e : archiveDataSource.getAllExpenses()){
            totalExpenseList.add(e);
        }

        List<Expense> expenses = Filter.getExpensesByType(totalExpenseList,type);
        List<Expense> expensesFiltered = filterListByDate(expenses,month,year);
        double amount = 0;
        for(Expense e : expensesFiltered)
            amount += e.getValue();
        return (float)amount;
    }

    private static List<Expense> filterListByDate(List<Expense> toFilter, int month, int year){
        List<Expense> filteredList = new LinkedList<>();
            for(Expense e : toFilter){
                DateForCompare expenseDate = new DateForCompare(e.getDate());
                if(expenseDate.getMonth() == month && expenseDate.getYear() == year)
                    filteredList.add(e);
            }
        return filteredList;
    }


    public static Pair<List<Entry>, List<String>> setExpensesAmount(Context context, int month, int year){

        final List<Entry> entries = new LinkedList<>();
        final List<String> labels = new LinkedList<>();

        int i = 0;
        ExpenseType[] array = ExpenseType.values();
        for (ExpenseType type : array) {
            float amount = getExpensesAmountByType(type, context, month, year);
            if (amount > 0) {
                labels.add(type.toString());
                entries.add(new Entry(amount, i));
                i++;
            } else
                continue;
        }
        return new Pair<>(entries,labels);
    }

    public static Pair<List<Entry>,List<String>> setExpensesAmount(Context context, int yearNum) {

        final List<Entry> entries = new LinkedList<>();
        final List<String> labels = new LinkedList<>();

        int i = 0;
        ExpenseType[] array = ExpenseType.values();
        for(ExpenseType type : array) {
            float yearAmount = 0;

            yearAmount = getExpensesAmountByType(type,context,yearNum);

            Log.d("PieChartHelper","Output test -->[" + type + ", " + yearAmount + ", " + i);
            Log.d("PieChartHelper","Year amount <= 0 --> " + (yearAmount <= 0));
            if (yearAmount > 0) {
                labels.add(type.toString());
                entries.add(new Entry(yearAmount, i));


                i++;
            }
        }
        return new Pair<>(entries,labels);
    }

    private static float getExpensesAmountByType(ExpenseType type, Context context, int yearNum) {
        ExpenseDataSource expenseDataSource = new ExpenseDataSource(context);
        ArchiveDataSource archiveDataSource = new ArchiveDataSource(context);
        expenseDataSource.open();
        archiveDataSource.open();

        List<Expense> totalExpenseList = new ArrayList<>();
        for(Expense e : expenseDataSource.getAllExpenses()){
            totalExpenseList.add(e);
        }
        for(Expense e : archiveDataSource.getAllExpenses()){
            totalExpenseList.add(e);
        }

        List<Expense> expenses = Filter.getExpensesByType(totalExpenseList,type);
        List<Expense> newList = new LinkedList<>();

        for(Expense e : expenses){
            if(new DateForCompare(e.getDate()).getYear() == yearNum)
                newList.add(e);
        }

        double amount = 0;
        for(Expense e : newList)
            amount += e.getValue();
        return (float)amount;
    }
}
