package bagarrao.financialdroid.currency;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import bagarrao.financialdroid.activity.SplashScreenActivity;
import bagarrao.financialdroid.database.ArchiveDataSource;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.util.SharedPreferencesHelper;

import static android.content.Context.MODE_PRIVATE;
import static bagarrao.financialdroid.activity.SplashScreenActivity.DEFAULT_CURRENCY;

/**
 * Created by eduar on 23/09/2017.
 */

public class CurrencyConverter {

    private static CurrencyConverter INSTANCE = null;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Context context;
    private Currency currency;

    private CurrencyConverter(Context context) {
        this.context = context;
        this.sharedPref = context.getSharedPreferences(SharedPreferencesHelper.CURRENCY_PREF_FILE, MODE_PRIVATE);
        if(!sharedPref.contains(SharedPreferencesHelper.CURRENCY_VALUE)){
            this.editor = sharedPref.edit();
            editor.putString(SharedPreferencesHelper.CURRENCY_DEFAULT_VALUE, SharedPreferencesHelper.CURRENCY_VALUE);
            editor.commit();
        }
        this.currency = sharedPref.getString(SharedPreferencesHelper.CURRENCY_VALUE, Currency.valueOf(CURRENCY_DEFAULT_VALUE));
    }

    public static CurrencyConverter getInstance() {
        return INSTANCE;
    }

    public static void init(Context context){
        INSTANCE = new CurrencyConverter(context);
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setCurrency(Currency currency){
        
		//calcular a currency de todas as despesas
		ExpenseDataSource expenseDataSource = new ExpenseDataSource(context);
        ArchiveDataSource archiveDataSource = new ArchiveDataSource(context);
		expenseDataSource.open();
		archiveDataSource.open();
		
		//start code here
		
		List<Expense> expenses = expenseDataSource.getAllExpenses();
		List<Expense> archiveExpenses = archiveDataSource.getAllExpenses();
		
		expenseDataSource.deleteAllExpenses();
		archiveDataSource.deleteAllExpenses();
		
		for(Expense e : expenses){
			e.setValue(Currency.convert(e.getValue(),getCurrentCurrency(),currency));
			expenseDataSource.createExpense(e);
		}
		
		for(Expense e : archiveExpenses){
			e.setValue(Currency.convert(e.getValue(),getCurrentCurrency(),currency));
			archiveDataSource.createExpense(e);
		}
		
		//end code here
		expenseDataSource.close();
		archiveDataSource.close();
		this.editor = sharedPref.edit();
        editor.putString(currency.toString(),SharedPreferencesHelper.CURRENCY_VALUE);
        editor.commit();
    }

    public Currency getCurrentCurrency(){
            return Currency.valueOf(sharedPref.getString(SharedPreferencesHelper.CURRENCY_VALUE, SharedPreferencesHelper.CURRENCY_DEFAULT_VALUE));
    }
}
