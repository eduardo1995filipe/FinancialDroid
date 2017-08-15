package bagarrao.financialdroid.utils;

import android.util.Log;

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

}
