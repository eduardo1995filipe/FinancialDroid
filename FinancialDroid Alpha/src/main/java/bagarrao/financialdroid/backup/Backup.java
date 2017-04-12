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
 * Created by eduar on 12/04/2017.
 */

public class Backup {

    public static final String FILE_NAME = "backup";
    public static final String FILE_EXTENSION = ".csv";

    private Context context;
    private BufferedWriter bw;
    private File file;
    private Date date;
    private ExpenseDataSource dataSource;
    private String absPath;
    private String backupName;
    private List<Expense> expenseList;

    /**
     * @param context
     */
    public Backup(Context context) {
        this.context = context;
        init();
    }

    /**
     * @return
     */
    public String getAbsPath() {
        return absPath;
    }

    /**
     * @return
     */
    public String getBackupName() {
        return backupName;
    }

    /**
     *
     */
    private void init() {
        this.date = new Date();
        this.dataSource = new ExpenseDataSource(context);
        this.file = new File(context.getFilesDir(), (FILE_NAME + FILE_EXTENSION));
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        else {
            for (int i = 1; ; i++) {
                file = new File(context.getFilesDir(), FILE_NAME + "(" + i + ")" + FILE_EXTENSION);
                if (!file.exists())
                    break;
            }
        }
        this.backupName = file.getName();
        this.absPath = file.getAbsolutePath();
    }

    /**
     *
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
     *
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
     *
     */
    public void go() {
        open();
        for (Expense e : expenseList) {
            try {
                expenseList = dataSource.getAllExpenses();
                bw.write(e.toString() + "\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        close();
    }
}
