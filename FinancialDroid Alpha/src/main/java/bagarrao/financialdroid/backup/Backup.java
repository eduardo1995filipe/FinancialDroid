package bagarrao.financialdroid.backup;

import android.content.Context;
import android.widget.Toast;

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
     *
     * @return
     */
    public String getAbsPath() {
        return this.absPath;
    }

    /**
     * @return
     */
    public String getBackupName() {
        return this.backupName;
    }


    /**
     * @return
     */
    public File getFile() {
        return this.file;
    }

    /**
     *
     */
    private void init() {
        this.date = new Date();
        this.dataSource = new ExpenseDataSource(context);
        this.file = new File(context.getFilesDir(), (BackupManager.FILE_NAME + BackupManager.FILE_EXTENSION));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        else {
            int lastOne = -1;
            for (int i = 1, index = 0; ; i++) {
                file = new File(context.getFilesDir(), BackupManager.FILE_NAME + "(" + i + ")" + BackupManager.FILE_EXTENSION);
                if (!file.exists()) {
                    if (lastOne == -1) {
                        lastOne = i;
                    }
                    if (index > 10)
                        break;
                    else {
                        index++;
                    }
                } else {
                    index = 0;
                    lastOne = -1;
                }
            }

            file = new File(context.getFilesDir(), BackupManager.FILE_NAME + "(" + lastOne + ")" + BackupManager.FILE_EXTENSION);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
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
        expenseList = dataSource.getAllExpenses();
        for (Expense e : expenseList) {
            try {
                bw.write(e.toString() + "\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        close();
        Toast.makeText(context, "Backup created successfully\n\n File name: " + file.getName() +
                "\n\n File Path: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getBackupName().equals(((Backup) obj).getBackupName());
    }

//    @Override
//    public void run() {
//        super.run();
//        open();
//        expenseList = dataSource.getAllExpenses();
//        for (Expense e : expenseList) {
//            try {
//                bw.write(e.toString() + "\n");
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//        close();
//        Toast.makeText(context, "Backup created successfully\n\n File name: " + file.getName() +
//                "\n\n File Path: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//    }
}
