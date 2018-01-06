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
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.DateParser;

/**
 *
 *
 *
 * @author Eduardo Bagarrao
 */
public class DataSource {

    public static final String ARCHIVE = DataSQLiteOpenHelper.ARCHIVE_TABLE;
    public static final String CURRENT = DataSQLiteOpenHelper.EXPENSE_TABLE;

    private static final String[] allColumns = {DataSQLiteOpenHelper.EXPENSE_COLUMN_ID,
            DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_PRICE, DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_TYPE,
            DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DESCRIPTION, DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DATE};

    private boolean isOpen;
    private DataSQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;
    private String mode;

    /**
     * creates de ExpenseDataSource object
     * @param context context of the current activity where the DataSouce object is created
     */
    public DataSource(String mode, Context context){
        this.isOpen = false;
        this.dbHelper = new DataSQLiteOpenHelper(context);
        this.mode = mode;
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
     *
     * @return
     */
    public String getMode() {
        return mode;
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
     * @param expenditure to be added in the database
     * @return returns the Expense created
     */
    public Expenditure createExpense(Expenditure expenditure) {

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
     * remove an Expense form the database
     * @param expenditure Expense to be removed
     */
    public void deleteExpenditure(Expenditure expenditure) {
        long id = expenditure.getId();
        database.delete(mode, DataSQLiteOpenHelper.EXPENSE_COLUMN_ID + " = " + id, null);
    }

    /**
     * gets all Expenses from the database
     * @return a List with all Expenses
     */
    public List<Expenditure> getAllExpenditures() {

        List<Expenditure> expenditures = new ArrayList<Expenditure>();

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
     * removes all the expenses from the database
     */
    public void deleteAllExpenditures() {
        List<Expenditure> list = getAllExpenditures();
        for (Expenditure e : list) {
            deleteExpenditure(e);
        }
    }

    /**
     * converts the Cursor on the respective Expense
     * @param cursor Cursor to be converted to Expense
     * @return Expense converted
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
