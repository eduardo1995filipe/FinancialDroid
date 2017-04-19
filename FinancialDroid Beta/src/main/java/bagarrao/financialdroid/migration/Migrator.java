package bagarrao.financialdroid.migration;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
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

    private static final String ACCESS_DATE = "accessDate";
    private static final String NULL_DATE = "nullValue";
    private static final String OLD_DATE = "oldDate";

    private Context context;
    private ExpenseDataSource expenseDataSource;
    private ArchiveDataSource archiveDataSource;
    private DateForCompare dateForCompare;
    private Date date;

    /**
     * @param context
     */
    public Migrator(Context context) {
        this.context = context;
        this.date = new Date();
        this.dateForCompare = new DateForCompare(date);
        this.expenseDataSource = new ExpenseDataSource(context);
        this.archiveDataSource = new ArchiveDataSource(context);
    }

    /**
     * checks if its's necessary or not to migrate the expenses
     *
     * @return boolean value that indicates if it's necessary to migrate the expenses
     */
    public static boolean needsMigration(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ACCESS_DATE, Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            Date currentDate = new Date();
            if (!sharedPreferences.contains(OLD_DATE)) {
                putNewDate(sharedPreferences, sharedPreferencesEditor, currentDate);
                return false;
            } else {
                Date lastDate = DateForCompare.DATE_FORMATTED.parse(sharedPreferences.getString(OLD_DATE, NULL_DATE));
                DateForCompare currentForCompare = new DateForCompare(currentDate);
                DateForCompare lastForCompare = new DateForCompare(lastDate);
                if ((lastForCompare.getMonth() < currentForCompare.getMonth() && lastForCompare.getYear() == currentForCompare.getYear()) ||
                        (lastForCompare.getYear() < currentForCompare.getYear())) {
                    putNewDate(sharedPreferences, sharedPreferencesEditor, currentDate);
                    return true;
                } else {
                    putNewDate(sharedPreferences, sharedPreferencesEditor, currentDate);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * saves the last date that the app was opened on SharedPreferences
     *
     * @param preferences
     * @param editor
     * @param date
     */
    public static void putNewDate(SharedPreferences preferences, SharedPreferences.Editor editor, Date date) {
        if (preferences.contains(OLD_DATE))
            editor.remove(OLD_DATE);
        editor.putString(OLD_DATE, DateForCompare.DATE_FORMATTED.format(date));
        editor.commit();
    }

    /**
     * inits the Expense Viewer database and the also the Archive Viewer Database
     */
    private void open() {
        expenseDataSource.open();
        archiveDataSource.open();
    }

    /**
     *
     */
    private void close() {
        expenseDataSource.close();
        archiveDataSource.close();
    }

	/**
	*
	*/
    public void run() {
        open();
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
}
