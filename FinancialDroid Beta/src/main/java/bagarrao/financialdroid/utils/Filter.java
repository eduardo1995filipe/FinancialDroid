package bagarrao.financialdroid.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;

/**
 * Created by eduar on 15/08/2017.
 */

public class Filter {

    public static List<Expense> getExpensesByType(List<Expense> toFilter,ExpenseType type) {
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

    public static ExpenseType getTypeByIndex(int index){
        ExpenseType type = null;
        switch(index){
            case 0 :
                break;
            case 1 :
                type = ExpenseType.FEEDING;
                break;
            case 2 :
                type = ExpenseType.TRANSPORTS;
                break;
            case 3 :
                type = ExpenseType.CLOTHING;
                break;
            case 4 :
                type = ExpenseType.SCHOOL;
                break;
            case 5 :
                type = ExpenseType.OTHERS;
                break;
            default:
                Log.d("Filter", "getTypeByIndex: unexpected error, index value is out of bounds");
                break;
        }
        return type;
    }

    public static List<Expense> filterExpensesByYear(List<Expense> toFilter, int year){
        List<Expense> newList = new ArrayList<Expense>();
        for(Expense e : toFilter){
            if(new DateForCompare(e.getDate()).getYear() == year){
                newList.add(e);
                Log.d("Filter",  "[ExpensesByYear]Expense added --> " + e.toString());
            }
        }
        return newList;
    }
    public static List<Expense> filterExpensesByMonth(List<Expense> toFilter, int month){
        List<Expense> newList = new ArrayList<Expense>();
        for(Expense e : toFilter){
            if(new DateForCompare(e.getDate()).getMonth() == month) {
                newList.add(e);
                Log.d("Filter",  "[ExpensesByMonth]Expense added --> " + e.toString());
            }
        }
        return newList;
    }
}
