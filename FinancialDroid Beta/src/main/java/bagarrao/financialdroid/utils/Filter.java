package bagarrao.financialdroid.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;

/**
 * @author Eduardo
 */
public class Filter {


    /**
     *
     * @param toFilter
     * @param type
     * @return
     */
    public static List<Expense> filterExpensesByType(List<Expense> toFilter, ExpenseType type) {
        if(type == null)
            return toFilter;
        else{
            LinkedList<Expense> newList = new LinkedList<>();
            for(Expense expense : toFilter)
                if(expense.getType() == type)
                    newList.add(expense);
            return  newList;
            }
    }

    /**
     *
     * @param toFilter
     * @param year
     * @return
     */
    public static List<Expense> filterExpensesByYear(List<Expense> toFilter, int year){
        List<Expense> newList = new ArrayList<Expense>();
        for(Expense e : toFilter){
            if(DateParser.getYear(e.getDate()) == year){
                newList.add(e);
                Log.d("Filter",  "[ExpensesByYear]Expense added --> " + e.toString());
            }
        }
        return newList;
    }

    /**
     *
     * @param toFilter
     * @param month
     * @return
     */
    public static List<Expense> filterExpensesByMonth(List<Expense> toFilter, int month){
        List<Expense> newList = new ArrayList<Expense>();
        for(Expense e : toFilter){
            if(DateParser.getMonth(e.getDate()) == month) {
                newList.add(e);
                Log.d("Filter",  "[ExpensesByMonth]Expense added --> " + e.toString());
            }
        }
        return newList;
    }

    /**
     *
     * @param toFilter
     * @param year
     * @param month
     * @return
     */
    public static List<Expense> filterExpensesByDate(List<Expense> toFilter, int year, int month){
        return(filterExpensesByMonth(filterExpensesByYear(toFilter,year),month));
    }
}
