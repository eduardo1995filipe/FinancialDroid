package bagarrao.financialdroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bagarrao.financialdroid.Expense;
import bagarrao.financialdroid.utils.ExpenseType;


/**
 * Created by eduar on 24/01/2017.
 */

public class ExpenseDataSource {
    private SQLiteDatabase database;
    private DataSQLiteOpenHelper dbHelper;
    private String[] allColumns = {DataSQLiteOpenHelper.EXPENSE_COLUMN_ID,
            DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_PRICE, DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_TYPE,
            DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DESCRIPTION, DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DATE};

    /**
     * creates de DataSource object
     *
     * @param context context of the current activity where the DataSouce object is created
     */
    public ExpenseDataSource(Context context) {
        dbHelper = new DataSQLiteOpenHelper(context);
    }

    /**
     * opens the database in "WRITE" mode
     *
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
     *
     * @param expense to be added in the database
     * @return returns the Expense created
     */
    public Expense createExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_PRICE, expense.getValue());
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_TYPE, expense.getType().toString());
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DESCRIPTION, expense.getDescription());
        values.put(DataSQLiteOpenHelper.EXPENSE_COLUMN_NAME_DATE, expense.getDate().toString());

        long insertId = database.insert(DataSQLiteOpenHelper.EXPENSE_TABLE, null,
                values);

        Cursor cursor = database.query(DataSQLiteOpenHelper.EXPENSE_TABLE,
                allColumns, DataSQLiteOpenHelper.EXPENSE_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Expense newExpense = cursorToExpense(cursor);
        cursor.close();
        return newExpense;
    }

    /**
     * remove an Expense form the database
     *
     * @param expense Expense to be removed
     */
    public void deleteExpense(Expense expense) {
        long id = expense.getId();
        System.out.println("Tarefa removida com o ID: " + id);
        database.delete(DataSQLiteOpenHelper.EXPENSE_TABLE, DataSQLiteOpenHelper.EXPENSE_COLUMN_ID + " = " + id, null);
    }

    /**
     * gets all Expenses from the database
     *
     * @return a List with all Expenses
     */
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<Expense>();

        Cursor cursor = database.query(DataSQLiteOpenHelper.EXPENSE_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Expense expense = cursorToExpense(cursor);
            expenses.add(expense);
            cursor.moveToNext();
        }
        // Fechar o cursor
        cursor.close();
        return expenses;
    }

    /**
     * converts the Cursor on the respective Expense
     *
     * @param cursor Cursor to be converted to Expense
     * @return Expense converted
     */
    private Expense cursorToExpense(Cursor cursor) {
        long id = cursor.getLong(0);
//        arranjar forma de mudar uma string para uma data som a classe simple date format[classe precisa de alteracoes]
        String newDate = cursor.getString(4);
        Expense expense = new Expense(cursor.getDouble(1), ExpenseType.strToExpenseType(cursor.getString(2)),
                cursor.getString(3), new Date()/*por enquanto sera a new date por razoes experimentair*/);
        expense.setId(id);
        return expense;
    }
}
