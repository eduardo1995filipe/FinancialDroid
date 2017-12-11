package bagarrao.financialdroid.database;

import android.content.Context;


/**
 * @author Eduardo Bagarrao
 */
public class ExpenseDataSource extends DataSource{

    private static final String TABLE = DataSQLiteOpenHelper.EXPENSE_TABLE;

    /**
     * creates de ExpenseDataSource object
     *
     * @param context  context of the current activity where the DataSouce object is created
     */
    public ExpenseDataSource(Context context) {
        super(TABLE, context);
    }
}
