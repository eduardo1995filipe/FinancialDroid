package bagarrao.financialdroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.DateParser;

/**
 *
 * Class that handles the database of the local {@link Expenditure} objects.
 * It's parallel to {@link DatabaseManager} and its
 * intended to use as a local storage.
 *
 * @author Eduardo Bagarrao
 */
class DataSource {

    /**
     * Table used to store {@link Expenditure} objects
     * that are more than one month old.
     */
    static final String ARCHIVE = DataSQLiteOpenHelper.ARCHIVE_TABLE;

    /**
     * Table used to store {@link Expenditure} objects
     * that are less than one month old.
     */
    static final String CURRENT = DataSQLiteOpenHelper.EXPENSE_TABLE;

    /**
     * Columns of both {@link #ARCHIVE} and {@link #CURRENT} {@link Expenditure} objects.
     * Both tables are equal it's just to differentiate the older
     * {@link Expenditure} objects and the newer {@link Expenditure} objects.
     */
    private static final String[] allColumns = {DataSQLiteOpenHelper.EXPENSE_COLUMN_ID,
            DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_PRICE, DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_TYPE,
            DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DESCRIPTION, DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DATE};

    /**
     * checks if the session between the database
     * and the application is true or false.
     */
    private boolean isOpen;

    /**
     * Have the schema tables and methods.
     *
     * @see android.database.sqlite.SQLiteOpenHelper
     */
    private DataSQLiteOpenHelper dbHelper;

    /**
     * {@link SQLiteDatabase} object.
     *
     */
    private SQLiteDatabase database;

    /**
     * Mode means the table that is being used. Either
     * can be {@link #CURRENT} or {@link #ARCHIVE}.
     */
    private String mode;

    /**
     * {@link DataSource} constructor.
     *
     * @param mode {@link String}
     * @param context {@link Context}
     */
    protected DataSource(String mode, Context context){
        this.isOpen = false;
        this.dbHelper = new DataSQLiteOpenHelper(context);
        this.mode = mode;
    }

    /**
     * Getter of the {@link #isOpen} attribute.
     *
     * @return boolean
     */
    boolean isOpen() {
        return isOpen;
    }

    /**
     * Getter of the {@link #mode} attribute.
     *
     * @return String
     */
    String getMode() {
        return mode;
    }

    /**
     * Opens a connection between the application and the database.
     * {@link #dbHelper} will handle that connection.
     * {@link #isOpen} will be turned true.
     *
     * @throws SQLException
     *
     * @see DataSQLiteOpenHelper
     */
    void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        isOpen = true;
    }

    /**
     * Closes the {@link #dbHelper} and
     * turns {@link #isOpen} value to false.
     */
    void close() {
        dbHelper.close();
        isOpen = false;
    }

    /**
     * Inserts an {@link Expenditure} object into the {@link SQLiteDatabase}
     * on the table of the current {@link #mode}.
     *
     * @param expenditure {@link Expenditure}
     * @return Expenditure
     */
    Expenditure createExpenditure(Expenditure expenditure) {

        ContentValues values = new ContentValues();
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_PRICE, expenditure.getValue());
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_TYPE, expenditure.getType().toString());
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DESCRIPTION, expenditure.getDescription());
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DATE, DateParser.parseString(expenditure.getDate()));

        long insertId = database.insert(mode, null,
                values);

        Cursor cursor = database.query(mode,
                allColumns, DataSQLiteOpenHelper.EXPENSE_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Expenditure newExpenditure = cursorToExpenditure(cursor);
        cursor.close();
        return newExpenditure;
    }

    /**
     * Removes an {@link Expenditure} object into the {@link SQLiteDatabase}
     * in the table of the current {@link #mode}.
     *
     * @param expenditure {@link Expenditure}
     */
    void deleteExpenditure(Expenditure expenditure) {
        long id = expenditure.getId();
        database.delete(mode, DataSQLiteOpenHelper.EXPENSE_COLUMN_ID + " = " + id, null);
    }

    /**
     * retrieves all {@link Expenditure} objects that are
     * present in the table in the current {@link #mode}.
     *
     * @return Expenditure
     */
    List<Expenditure> getAllExpenditures() {

        List<Expenditure> expenditures = new ArrayList<>();

        Cursor cursor = database.query(mode,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Expenditure expenditure = cursorToExpenditure(cursor);
            expenditures.add(expenditure);
            cursor.moveToNext();
        }
        cursor.close();
        return expenditures;
    }

    /**
     * removes all {@link Expenditure} objects that are
     * present in the table in the current {@link #mode}.
     */
    void deleteAllExpenditures() {
        List<Expenditure> list = getAllExpenditures();
        for (Expenditure e : list) {
            deleteExpenditure(e);
        }
    }

    /**
     * Receives a {@link Cursor} and retrieves the {@link Expenditure}
     * object that {@link Cursor} points. It will return null when the
     * {@link Cursor doesn't point to no {@link Expenditure}}
     *
     * @param cursor {@link Cursor}
     * @return Expenditure
     */
    private Expenditure cursorToExpenditure(Cursor cursor) {
        long id = cursor.getLong(0);
        Expenditure expenditure = null;
        try {
            expenditure = new Expenditure(cursor.getDouble(1), ExpenseType.valueOf(cursor.getString(2).toUpperCase()),
                    cursor.getString(3), DateParser.parseDate(cursor.getString(4)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expenditure.setId(id);
        return expenditure;
    }
}