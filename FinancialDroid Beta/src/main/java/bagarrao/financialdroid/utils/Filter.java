package bagarrao.financialdroid.utils;

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
}
