package bagarrao.financialdroid.currency;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import bagarrao.financialdroid.database.ArchiveDataSource;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.utils.SharedPreferencesHelper;

/**
 * @author Eduardo Bagarrao
 */
public class CurrencyConverter {

    private static final CurrencyConverter INSTANCE = new CurrencyConverter();

    private boolean isInitialized;

    private Context context;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;

    private ExpenseDataSource expenseDataSource;
    private ArchiveDataSource archiveDataSource;

    private CurrencyConverter(){
        this.isInitialized = false;
    }

    public static CurrencyConverter getInstance() {
        return ((INSTANCE != null) ? INSTANCE : new CurrencyConverter());
    }

    public void init(Context context){
        this.context = context;
        this.isInitialized = true;
        this.sharedPref = context.getSharedPreferences(SharedPreferencesHelper.CURRENCY_PREFERENCES_FILE, context.MODE_PRIVATE);
        this.expenseDataSource = new ExpenseDataSource(context);
        this.archiveDataSource = new ArchiveDataSource(context);
    }

    public Currency getCurrency(){
        if(isInitialized)
            return Currency.valueOf(sharedPref.getString(SharedPreferencesHelper.CURRENCY_KEY, SharedPreferencesHelper.CURRENCY_DEFAULT_VALUE));
        else
            throw new NullPointerException("Context needs to be initialized before you use this Singleton!");
    }

    public void setCurrency(Currency currency){
        if(isInitialized){
            Currency lastCurrency = getCurrency();

            expenseDataSource.open();
            archiveDataSource.open();

            List<Expense> expenses = expenseDataSource.getAllExpenses();
            List<Expense> archiveExpenses = archiveDataSource.getAllExpenses();

            expenseDataSource.deleteAllExpenses();
            archiveDataSource.deleteAllExpenses();

            for(Expense e : expenses){
                e.setValue(lastCurrency.convert(e.getValue(),currency));
                expenseDataSource.createExpense(e);
            }

            for(Expense e : archiveExpenses){
                e.setValue(lastCurrency.convert(e.getValue(),currency));
                archiveDataSource.createExpense(e);
            }

            expenseDataSource.close();
            expenseDataSource.close();

            this.sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.putString(SharedPreferencesHelper.CURRENCY_KEY, currency.toString());
            sharedPrefEditor.commit();
        }else
            throw new NullPointerException("Context needs to be initialized before you use this Singleton");
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
