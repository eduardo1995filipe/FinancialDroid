package bagarrao.financialdroid.backup;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import bagarrao.financialdroid.AddExpenseActivity;
import bagarrao.financialdroid.database.ArchiveDataSource;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.DateForCompare;

/**
 * @author Eduardo Bagarrao
 */
public class Backup {

    public static final String FILE_NAME = "backup.csv";
    private static Context context;

    private BufferedWriter bw;
    private File file;
    private ExpenseDataSource expenseDataSource;
    private ArchiveDataSource archiveDataSource;
    private List<Expense> expenseList;
    private List<Expense> archiveList;

    /**
     * Constructor that is only used for the first time that a Backup object is created.
     * ATENTION: It is mandatory to use this contructor for the first time, or it could origin some undesired crashes!!!!
     * @param context Context of the activity when first time Backup object is created
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

    public static boolean restorer() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                Log.e("Backup", "File doesn't exist.");
                return false;
            } else if (context == null) {
                Log.e("Backup", "context is not set. Please set it before use this function.");
                return false;
            }
            ExpenseDataSource expenseDataSource = new ExpenseDataSource(context);
            ArchiveDataSource archiveDataSource = new ArchiveDataSource(context);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String text = "";
            expenseDataSource.open();
            archiveDataSource.open();
            expenseDataSource.deleteAllExpenses();
            archiveDataSource.deleteAllExpenses();
            while ((text = br.readLine()) != null) {
                String[] nExp = text.split(";");
                Expense e = new Expense(Double.parseDouble(nExp[0]), ExpenseType.valueOf(nExp[1]), nExp[2], DateForCompare.DATE_FORMATTED.parse(nExp[3]));
                AddExpenseActivity.addNewExpense(e, context, expenseDataSource);
                //TODO need to test
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * initiates all objects
     */
    private void init() {
        this.expenseDataSource = new ExpenseDataSource(context);
        this.archiveDataSource = new ArchiveDataSource(context);
        this.file = new File(context.getFilesDir(), (FILE_NAME));
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inits BufferedWriter and opens the database
     */
    public void open() {
        try {
            this.bw = new BufferedWriter(new FileWriter(file, true/* APPEND MODE*/));
            expenseDataSource.open();
            archiveDataSource.open();
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
            expenseDataSource.close();
            archiveDataSource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  writes all current Expenses on database into a .csv file
     */
    public void go() {
        open();
        expenseList = expenseDataSource.getAllExpenses();
        archiveList = archiveDataSource.getAllExpenses();
        for (Expense e : expenseList) {
            try {
                bw.write(e.toString() + "\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        for (Expense e : archiveList) {
            try {
                bw.write(e.toString() + "\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        close();
    }
}
