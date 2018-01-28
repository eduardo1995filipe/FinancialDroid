//package bagarrao.financialdroid.backup;
//
//import android.content.Context;
//import android.util.Log;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.List;
//
//import bagarrao.financialdroid.database.DatabaseManager;
//import bagarrao.financialdroid.expense.Expenditure;
//import bagarrao.financialdroid.expense.ExpenseType;
//import bagarrao.financialdroid.utils.DateParser;
//
///**
// * @author Eduardo Bagarrao
// */
//public class Backup {
//
//    public static DatabaseManager manager= DatabaseManager.getInstance();
//
//    public static final String FILE_BEGINING= "FINANCIALDROID -" + ((long)((Math.random() + 0.5) * 100_000_000)) + "droid";
//    public static final String FILE_NAME = "backup.csv";
//    private static Context context;
//
//
//    private BufferedWriter bw;
//    private File file;
//    private List<Expenditure> list;
//
//    /**
//     * Constructor that is only used for the first time that a Backup object is created.
//     * @ATENTION: It is mandatory to use this contructor for the first time, or it could origin some undesired crashes!!!!
//     * @param context Context of the activity when first time Backup object is created
//     */
//    public Backup(Context context) {
//        this.context = context;
//        manager.setContext(context);
//        init();
//    }
//
//    /**
//     * Secondary constructor. Used when you already setted the context.
//     */
//    public Backup() {
//        if (context != null)
//            init();
//    }
//
//    /**
//     * Restores the last backup file saved
//     *
//     * @return boolean value that shows if the restore was successfull or if it failed
//     */
//    public static boolean restorer() {
//        //TODO change restorer method the way he works in order to read a csv file
//        try {
//            File file = new File(FILE_NAME);
//            if (!file.exists()) {
//                Log.e("Backup", "File doesn't exist.");
//                return false;
//            } else if (context == null) {
//                Log.e("Backup", "context is not set. Please set it before use this function.");
//                return false;
//            }
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String text = "";
//
//            manager.resetArchiveExpenditures();
//            while ((text = br.readLine()) != null) {
//                String[] nExp = text.split(";");
//                manager.insertExpenditure(new Expenditure(Double.parseDouble(nExp[0]), ExpenseType.valueOf(nExp[1]),
//                        nExp[2], DateParser.parseDate(nExp[3])));
//                //TODO: change e for expenditure
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
//
//    /**
//     * initiates all objects
//     */
//    private void init() {
//        this.file = new File(context.getFilesDir(), (FILE_NAME));
//        if (file.exists())
//            file.delete();
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Inits BufferedWriter and opens the database
//     */
//    public void open() {
//        try {
//            this.bw = new BufferedWriter(new FileWriter(file, false));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * flushes and closes BufferedWriter and closes the database
//     */
//    public void close() {
//        try {
//            bw.flush();
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     *  writes all current Expenses on database into a .csv file
//     */
//    public void go() {
//        open();
//        list = manager.getAllExpenditures();
//        try {
//            bw.write(FILE_BEGINING);
//            for (Expenditure e : list)
//                    bw.write(e.toString() + "\n");
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//        close();
//    }
//}
