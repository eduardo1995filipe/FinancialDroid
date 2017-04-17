package bagarrao.financialdroid.backup;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import bagarrao.financialdroid.Expense.Expense;
import bagarrao.financialdroid.database.ExpenseDataSource;

/**
 * @author Eduardo Bagarrao
 */
public class Backup {

    public static final String FILE_NAME = "backup.csv";

    private static Context context;

    private BufferedWriter bw;
    private File file;
    private ExpenseDataSource dataSource;
    private String absPath;
    private String backupName;
    private List<Expense> expenseList;
    private Date date;

    /**
     * Constructor that is only used for the first time that a Backup object is created.
     * ATENTION: It is mandatory to use this contructor for the first time, or it could origin some undesired crashes!!!!
     * @param context
     */
    public Backup(Context context) {
        this.context = context;
        init();
    }

    /**
     * Secondary constructor. Used when you already setted the context.
     */
    public Backup() {
        if (context != null)
            init();
    }

    /**
     * initiates all objects
     */
    private void init() {
        this.date = new Date();
        this.dataSource = new ExpenseDataSource(context);
        this.file = new File(context.getFilesDir(), (FILE_NAME));
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.backupName = file.getName();
        this.absPath = file.getAbsolutePath();
    }

    /**
     * Inits BufferedWriter and opens the database
     */
    public void open() {
        try {
            this.bw = new BufferedWriter(new FileWriter(file, true/* APPEND MODE*/));
            dataSource.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * flushes and closes BufferedWriter and closes the database
     */
    public void close() {
        try {
            bw.flush();
            bw.close();
            dataSource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  writes all current Expenses on database into a .csv file
     */
    public void go() {
        open();
        expenseList = dataSource.getAllExpenses();
        for (Expense e : expenseList) {
            try {
                bw.write(e.toString() + "\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        close();
    }
}
