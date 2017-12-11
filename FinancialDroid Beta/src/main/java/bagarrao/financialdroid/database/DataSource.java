package bagarrao.financialdroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.DateParser;

/**
 * @author Eduardo Bagarrao
 */
public class DataSource {

    private static final String[] allColumns = {DataSQLiteOpenHelper.EXPENSE_COLUMN_ID,
            DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_PRICE, DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_TYPE,
            DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DESCRIPTION, DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DATE};

    private boolean isOpen;
    private DataSQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;
    private String tableName;

    /**
     * creates de ExpenseDataSource object
     * @param context context of the current activity where the DataSouce object is created
     */
    protected DataSource(String tableName, Context context){
        this.isOpen = false;
        this.dbHelper = new DataSQLiteOpenHelper(context);
        this.tableName = tableName;
    }
    /**
     * returns the value if the the database is already isOpen
     *
     * @return the boolean value that shows that is isOpen or not
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * opens the database in "WRITE" mode
     * @throws SQLException if some problem occurs while access the database
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        isOpen = true;
    }

    /**
     * close the database
     */
    public void close() {
        dbHelper.close();
        isOpen = false;
    }

    /**
     * add an Expense to the database
     * @param expense to be added in the database
     * @return returns the Expense created
     */
    public Expense createExpense(Expense expense) {

        ContentValues values = new ContentValues();
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_PRICE, expense.getValue());
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_TYPE, expense.getType().toString());
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DESCRIPTION, expense.getDescription());
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DATE, DateParser.parseString(expense.getDate()));

        long insertId = database.insert(tableName, null,
                values);

        Cursor cursor = database.query(tableName,
                allColumns, DataSQLiteOpenHelper.EXPENSE_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Expense newExpense = cursorToExpense(cursor);
        cursor.close();
        return newExpense;
    }

    /**
     * remove an Expense form the database
     * @param expense Expense to be removed
     */
    public void deleteExpense(Expense expense) {
        long id = expense.getId();
        database.delete(tableName, DataSQLiteOpenHelper.EXPENSE_COLUMN_ID + " = " + id, null);
    }

    /**
     * gets all Expenses from the database
     * @return a List with all Expenses
     */
    public List<Expense> getAllExpenses() {

        List<Expense> expenses = new ArrayList<Expense>();

        Cursor cursor = database.query(tableName,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Expense expense = cursorToExpense(cursor);
            expenses.add(expense);
            cursor.moveToNext();
        }
        cursor.close();
        return expenses;
    }

    /**
     * removes all the expenses from the database
     */
    public void deleteAllExpenses() {
        List<Expense> list = getAllExpenses();
        for (Expense e : list) {
            deleteExpense(e);
        }
    }

    /**
     * converts the Cursor on the respective Expense
     * @param cursor Cursor to be converted to Expense
     * @return Expense converted
     */
    private Expense cursorToExpense(Cursor cursor) {
        long id = cursor.getLong(0);
        Expense expense = null;
        try {
            expense = new Expense(cursor.getDouble(1), ExpenseType.valueOf(cursor.getString(2).toUpperCase()),
                    cursor.getString(3), DateParser.parseDate(cursor.getString(4)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expense.setId(id);
        return expense;
    }
}
