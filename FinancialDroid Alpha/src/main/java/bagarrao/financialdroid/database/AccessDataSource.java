package bagarrao.financialdroid.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import bagarrao.financialdroid.utils.AccessDate;
import bagarrao.financialdroid.utils.DateForCompare;

/**
 * @author Eduardo Bagarrao
 */
public class AccessDataSource {
    private SQLiteDatabase database;
    private DataSQLiteOpenHelper dbHelper;
    private String[] allColumns = {DataSQLiteOpenHelper.ACCESS_COLUMN_ID,
            DataSQLiteOpenHelper.ACCESS_COLUMN_NAME_DATE};

    /**
     * creates de DataSource object
     * @param context context of the current activity where the DataSouce object is created
     */
    public AccessDataSource(Context context) {
        dbHelper = new DataSQLiteOpenHelper(context);
    }

    /**
     * opens the database in "WRITE" mode
     * @throws SQLException if some problem occurs while access the database
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * close the database
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * add an Expense to the database
     * @param date to be added in the database
     * @return returns the Expense created
     */
    public AccessDate createExpense(AccessDate date) {
        ContentValues values = new ContentValues();
        values.put(DataSQLiteOpenHelper.ACCESS_COLUMN_NAME_DATE, date.getDate().toString());

        long insertId = database.insert(DataSQLiteOpenHelper.ARCHIVE_TABLE, null,
                values);

        Cursor cursor = database.query(DataSQLiteOpenHelper.ARCHIVE_TABLE,
                allColumns, DataSQLiteOpenHelper.EXPENSE_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        AccessDate newAccessDate = cursorToAccessDate(cursor);
        cursor.close();
        return newAccessDate;
    }

    /**
     * remove an Expense form the database
     * @param date Expense to be removed
     */
    public void deleteAccessDate(AccessDate date) {
        long id = date.getId();
        System.out.println("Tarefa removida com o ID: " + id);
        database.delete(DataSQLiteOpenHelper.ACCESS_TABLE, DataSQLiteOpenHelper.ACCESS_COLUMN_ID + " = " + id, null);
    }

    /**
     * gets all Expenses from the database
     * @return a List with all Expenses
     */
    public List<AccessDate> getAllAccessDates() {
        List<AccessDate> accessDates = new ArrayList<AccessDate>();

        Cursor cursor = database.query(DataSQLiteOpenHelper.ACCESS_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AccessDate date = cursorToAccessDate(cursor);
            accessDates.add(date);
            cursor.moveToNext();
        }
        cursor.close();
        return accessDates;
    }

    /**
     * converts the Cursor on the respective Expense
     * @param cursor Cursor to be converted to Expense
     * @return Expense converted
     */
    private AccessDate cursorToAccessDate(Cursor cursor) {
        long id = cursor.getLong(0);
        AccessDate date = null;
        try {
            date = new AccessDate(DateForCompare.DATE_FORMATTED.parse(cursor.getString(1)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setId(id);
        return date;
    }
}