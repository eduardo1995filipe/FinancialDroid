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
import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.database.DataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;

/**
 * @author Eduardo
 */
public class PieChartHelper {

    /**
     *
     * @param activity
     * @param id
     * @param entries
     * @param labels
     * @return
     */
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
        pieChart.animateX(2500);
        return pieChart;
    }

    /**
     *
     * @param context
     * @param entries
     * @param labels
     * @return
     */
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
        pieChart.animateX(2500);
        return pieChart;
    }

    /**
     * return the total value of the ExpenseType given in the parameter
     * @param type type of the
     * @return total value of the expenses of that ExpenseType
     */
    public static float getExpensesAmountByType(ExpenseType type, Context context, int month, int year){
        DataSource expenseDataSource = new DataSource(DataSource.CURRENT,context);
        DataSource archiveDataSource = new DataSource(DataSource.ARCHIVE,context);
        expenseDataSource.open();
        archiveDataSource.open();

        List<Expense> totalExpenseList = new ArrayList<>();
        totalExpenseList.addAll(expenseDataSource.getAllExpenses());
        totalExpenseList.addAll(archiveDataSource.getAllExpenses());
        List<Expense> expenses = Filter.filterExpensesByType(totalExpenseList,type);
        List<Expense> expensesFiltered = Filter.filterExpensesByDate(expenses,month,year);

        double amount = 0;
        for(Expense e : expensesFiltered)
            amount += e.getValue();
        return (float)amount;
    }

    /**
     *
     * @param context
     * @param month
     * @param year
     * @return
     */
    public static Pair<List<Entry>, List<String>> setExpensesAmount(Context context, int month, int year){
        List<Entry> entries = new LinkedList<>();
        List<String> labels = new LinkedList<>();
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

    /**
     *
     * @param context
     * @param yearNum
     * @return
     */
    public static Pair<List<Entry>,List<String>> setExpensesAmount(Context context, int yearNum) {
        List<Entry> entries = new LinkedList<>();
        List<String> labels = new LinkedList<>();
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

    /**
     *
     * @param type
     * @param context
     * @param yearNum
     * @return
     */
    private static float getExpensesAmountByType(ExpenseType type, Context context, int yearNum) {
        DataSource expenseDataSource = new DataSource(DataSource.CURRENT, context);
        DataSource archiveDataSource = new DataSource(DataSource.ARCHIVE, context);
        expenseDataSource.open();
        archiveDataSource.open();

        List<Expense> totalExpenseList = new ArrayList<>();
        totalExpenseList.addAll(expenseDataSource.getAllExpenses());
        totalExpenseList.addAll(archiveDataSource.getAllExpenses());

        List<Expense> expenses = Filter.filterExpensesByType(totalExpenseList,type);
        List<Expense> newList = new LinkedList<>();

        for(Expense e : expenses){
            if(DateParser.getYear(e.getDate()) == yearNum)
                newList.add(e);
        }

        double amount = 0;
        for(Expense e : newList)
            amount += e.getValue();
        return (float)amount;
    }
}
