package bagarrao.financialdroid.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import bagarrao.financialdroid.expense.Expenditure;
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
    public static List<Expenditure> filterExpensesByType(List<Expenditure> toFilter, ExpenseType type) {
        if(type == null)
            return toFilter;
        else{
            LinkedList<Expenditure> newList = new LinkedList<>();
            for(Expenditure expense : toFilter)
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
    public static List<Expenditure> filterExpensesByYear(List<Expenditure> toFilter, int year){
        List<Expenditure> newList = new ArrayList<>();
        for(Expenditure e : toFilter){
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
    public static List<Expenditure> filterExpensesByMonth(List<Expenditure> toFilter, int month){
        List<Expenditure> newList = new ArrayList<Expenditure>();
        for(Expenditure e : toFilter){
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
    public static List<Expenditure> filterExpensesByDate(List<Expenditure> toFilter, int year, int month){
        return(filterExpensesByMonth(filterExpensesByYear(toFilter,year),month));
    }

    public static List<Expenditure> filterRecentExpenditures(List<Expenditure> vector){
        return null;
    }

    public static List<Expenditure> filterOlderExpenditures(List<Expenditure> vector){
        return null;
    }
}
