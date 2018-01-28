package bagarrao.financialdroid.database;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.utils.DateParser;
import bagarrao.financialdroid.utils.SharedPreferencesHelper;

/**
 * @author Eduardo Bagarrao
 */
public class Migrator {

    private DataSource expenseDataSource;
    private DataSource archiveDataSource;

    private Date date;


    /**
     * @param context
     */
    public Migrator(Context context) {
        this.date = new Date();
        this.expenseDataSource = new DataSource(DataSource.CURRENT, context);
        this.archiveDataSource = new DataSource(DataSource.ARCHIVE, context);
    }

    /**
     * checks if its's necessary or not to migrate the expenses
     *
     * @return boolean value that indicates if it's necessary to migrate the expenses
     */
    public static boolean needsMigration(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    SharedPreferencesHelper.ACCESS_DATE_PREF_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            Date currentDate = new Date();
            if (!sharedPreferences.contains(SharedPreferencesHelper.OLD_DATE_VALUE)) {
                putNewDate(sharedPreferences, sharedPreferencesEditor, currentDate);
                return false;
            } else {
                Date lastDate = DateParser.parseDate(
                        (sharedPreferences.getString(SharedPreferencesHelper.OLD_DATE_VALUE,
                                SharedPreferencesHelper.OLD_DATE_DEFAULT_VALUE)));
                if ((DateParser.getMonth(lastDate) < DateParser.getMonth(currentDate) &&
                        DateParser.getYear(lastDate) < DateParser.getYear(currentDate)) ||
                        (DateParser.getYear(lastDate) < DateParser.getYear(currentDate))) {
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
        if (preferences.contains(SharedPreferencesHelper.OLD_DATE_VALUE))
            editor.remove(SharedPreferencesHelper.OLD_DATE_VALUE);
        editor.putString(SharedPreferencesHelper.OLD_DATE_VALUE, DateParser.parseString(date));
        editor.commit();
    }

    /**
     *
     */
    public void run() {
        if (!expenseDataSource.isOpen())
            expenseDataSource.open();
        if (!archiveDataSource.isOpen())
            archiveDataSource.open();
        List<Expenditure> list = expenseDataSource.getAllExpenditures();
        expenseDataSource.deleteAllExpenditures();
        for (Expenditure e : list)
            if (DateParser.getMonth(e.getDate()) < DateParser.getMonth(date)
                    || DateParser.getYear(e.getDate()) < DateParser.getYear(date)) {
                archiveDataSource.createExpenditure(e);
        }
        if (expenseDataSource.isOpen())
            expenseDataSource.close();
        if (archiveDataSource.isOpen())
            archiveDataSource.close();
    }
}
