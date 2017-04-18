package bagarrao.financialdroid.migration;

import android.content.Context;

import java.util.Date;
import java.util.List;

import bagarrao.financialdroid.backup.Backup;
import bagarrao.financialdroid.database.ArchiveDataSource;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.utils.DateForCompare;

/**
 * @author Eduardo Bagarrao
 */
public class Migrator {

    private Context context;
    private ExpenseDataSource expenseDataSource;
    private ArchiveDataSource archiveDataSource;
    private DateForCompare dateForCompare;
    private Date date;

    public Migrator(Context context) {
        this.context = context;
        this.date = new Date();
        this.dateForCompare = new DateForCompare(date);
        this.expenseDataSource = new ExpenseDataSource(context);
        this.archiveDataSource = new ArchiveDataSource(context);
    }

	/**
	* inits the Expense Viewer database and the also the Archive Viewer Database
	*/
    private void init() {
        expenseDataSource.open();
        archiveDataSource.open();
    }

	/**
	*
	*/
    public void run() {
        init();
        List<Expense> expenseList = expenseDataSource.getAllExpenses();
        for (Expense e : expenseList) {
            DateForCompare expenseDFC = new DateForCompare(e.getDate());
            if (expenseDFC.getMonth() < dateForCompare.getMonth() || expenseDFC.getYear() < dateForCompare.getYear()) {
                archiveDataSource.createExpense(e);
                expenseDataSource.deleteExpense(e);
            }
        }
        close();
        new Backup().go();
    }

	/**
	*
	*/
    private void close() {
        expenseDataSource.close();
        archiveDataSource.close();
    }
}
