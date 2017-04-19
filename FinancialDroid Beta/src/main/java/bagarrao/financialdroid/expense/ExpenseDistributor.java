package bagarrao.financialdroid.expense;

import android.content.Context;

import java.util.Date;

import bagarrao.financialdroid.backup.Backup;
import bagarrao.financialdroid.database.ArchiveDataSource;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.utils.DateForCompare;

/**
 * @author Eduardo Bagarrao
 */
public class ExpenseDistributor {


    /**
     * Adds a new expense to the DB and checks if it goes to the Expense Viewer or if it goes to the Archive
     *
     * @param expense
     * @param context
     * @param expenseDataSource
     * @param archiveDataSource
     */
    public static void addNewExpense(Expense expense, Context context, ExpenseDataSource expenseDataSource, ArchiveDataSource archiveDataSource) {
        DateForCompare expenseDFC = new DateForCompare(expense.getDate());
        DateForCompare currentDFC = new DateForCompare(new Date());
        if ((expenseDFC.getMonth() < currentDFC.getMonth() && expenseDFC.getYear() == currentDFC.getYear()) ||
                (expenseDFC.getYear() < currentDFC.getYear())) {
            if (archiveDataSource.isOpen())
                archiveDataSource.createExpense(expense);
            else {
                archiveDataSource.open();
                archiveDataSource.createExpense(expense);
            }
            archiveDataSource.close();
        } else {
            if (expenseDataSource.isOpen())
                expenseDataSource.createExpense(expense);
            else {
                expenseDataSource.open();
                expenseDataSource.createExpense(expense);
            }
            expenseDataSource.close();
        }
        new Backup().go();
    }
}
