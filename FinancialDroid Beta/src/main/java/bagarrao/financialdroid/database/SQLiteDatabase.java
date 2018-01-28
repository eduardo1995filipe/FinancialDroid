package bagarrao.financialdroid.database;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.utils.DateParser;

public class SQLiteDatabase implements Database<Expenditure> {

    private Context context;
    private DataSource expenseDataSource;
    private DataSource archiveDataSource;

    public SQLiteDatabase(Context context){
        this.context = context;
        this.expenseDataSource = new DataSource(DataSource.CURRENT,context);
        this.archiveDataSource = new DataSource(DataSource.ARCHIVE,context);
    }

    @Override
    public void insert(Expenditure expenditure) {
        if(expenseDataSource.getMode().equals(DataSource.CURRENT) && archiveDataSource.getMode().equals(DataSource.ARCHIVE)) {
            Date date = new Date();
            if ((DateParser.getMonth(expenditure.getDate()) < DateParser.getMonth(date) &&
                    DateParser.getYear(expenditure.getDate()) < DateParser.getYear(date)) ||
                    (DateParser.getYear(expenditure.getDate()) < DateParser.getYear(date))) {
                if (archiveDataSource.isOpen())
                    archiveDataSource.createExpenditure(expenditure);
                else {
                    archiveDataSource.open();
                    archiveDataSource.createExpenditure(expenditure);
                }
                archiveDataSource.close();
            } else {
                if (expenseDataSource.isOpen())
                    expenseDataSource.createExpenditure(expenditure);
                else {
                    expenseDataSource.open();
                    expenseDataSource.createExpenditure(expenditure);
                }
                expenseDataSource.close();
            }
        }else{
            Log.e("ExpenseDistributor", "Datasources have not different modes");
        }
    }

    @Override
    public void delete(Expenditure expenditure) {
        expenseDataSource.open();
        archiveDataSource.open();
        if(expenditure.isOld())
            archiveDataSource.deleteExpenditure(expenditure);
        else
            expenseDataSource.deleteExpenditure(expenditure);
        expenseDataSource.close();
        archiveDataSource.close();
    }

    @Override
    public void update(Expenditure oldExpenditure, Expenditure newExpenditure) {
        DataSource dataSource = (!oldExpenditure.isOld()) ? expenseDataSource : archiveDataSource;
        if (!dataSource.isOpen())
            dataSource.open();
        List<Expenditure> expenditureList = dataSource.getAllExpenditures();
        for (Expenditure e : expenditureList){
            if (e.equals(oldExpenditure)) {
                expenseDataSource.deleteExpenditure(e);
                expenseDataSource.createExpenditure(newExpenditure);
                break;
            }
        }
        if(dataSource.isOpen())
            dataSource.close();
    }

    @Override
    public Vector<Expenditure> selectAll() {
        expenseDataSource.open();
        archiveDataSource.open();
        Vector<Expenditure> totalExpenseVector = new Vector<>();
        totalExpenseVector.addAll(expenseDataSource.getAllExpenditures());
        totalExpenseVector.addAll(archiveDataSource.getAllExpenditures());
        expenseDataSource.close();
        archiveDataSource.close();
        return totalExpenseVector;
    }

    @Override
    public void deleteAll() {
        for(Expenditure e : selectAll()){
            delete(e);
        }
    }
}
