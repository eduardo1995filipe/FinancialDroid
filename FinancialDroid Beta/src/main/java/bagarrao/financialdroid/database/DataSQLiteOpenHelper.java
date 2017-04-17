package bagarrao.financialdroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Eduardo Bagarrao
 */
public class DataSQLiteOpenHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DataReader.db";
    public static final String EXPENSE_TABLE = "expenses";
    public static final String ARCHIVE_TABLE = "archive";
    //    public static final String ACCESS_TABLE = "accesses";
    public static final String EXPENSE_COLUMN_ID = "_id";
    public static final String EXPENSE_COLUMN_NAME_PRICE = "price";
    public static final String EXPENSE_COLUMN_NAME_TYPE = "type";
    public static final String EXPENSE_COLUMN_NAME_DESCRIPTION = "description";
    public static final String EXPENSE_COLUMN_NAME_DATE = "date";
//    public static final String ACCESS_COLUMN_ID = "_id";
//    public static final String ACCESS_COLUMN_NAME_DATE = "date";

    /**
     * queries to create tables
     */
    private static final String EXPENSE_TABLE_CREATE = "create table " + EXPENSE_TABLE +
            "(" + EXPENSE_COLUMN_ID + " integer primary key autoincrement, " + EXPENSE_COLUMN_NAME_PRICE +
            " real not null, " + EXPENSE_COLUMN_NAME_TYPE + " text not null, " + EXPENSE_COLUMN_NAME_DESCRIPTION +
            " text not null, " + EXPENSE_COLUMN_NAME_DATE + " text not null);";
    private static final String ARCHIVE_TABLE_CREATE = "create table " + ARCHIVE_TABLE +
            "(" + EXPENSE_COLUMN_ID + " integer primary key autoincrement, " + EXPENSE_COLUMN_NAME_PRICE +
            " real not null, " + EXPENSE_COLUMN_NAME_TYPE + " text not null, " + EXPENSE_COLUMN_NAME_DESCRIPTION +
            " text not null, " + EXPENSE_COLUMN_NAME_DATE + " text not null);";
//    private static final String ACCESS_TABLE_CREATE = "create table " + ACCESS_TABLE +
//            "(" + ACCESS_COLUMN_ID + " integer primary key autoincrement, " + ACCESS_COLUMN_NAME_DATE +
//            " text not null);";

    /**
     * Initializes the SQLiteOpenHelper
     * @param context context of activity to initialize the SQLiteOpenHelper
     */
    public DataSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EXPENSE_TABLE_CREATE);
        db.execSQL(ARCHIVE_TABLE_CREATE);
//        db.execSQL(ACCESS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataSQLiteOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARCHIVE_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + ACCESS_TABLE);
        onCreate(db);
    }
}