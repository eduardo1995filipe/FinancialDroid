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
import bagarrao.financialdroid.utils.DateForCompare;


/**
 * @author Eduardo Bagarrao
 */
public class ArchiveDataSource extends DataSource{

    private static final String TABLE = DataSQLiteOpenHelper.ARCHIVE_TABLE;

    /**
     * creates de ExpenseDataSource object
     *
     * @param context   context of the current activity where the DataSouce object is created
     */
    public ArchiveDataSource(Context context) {
        super(TABLE, context);
    }
}
