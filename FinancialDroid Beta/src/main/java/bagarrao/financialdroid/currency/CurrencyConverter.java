package bagarrao.financialdroid.currency;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;
import java.math.RoundingMode;

import bagarrao.financialdroid.utils.SharedPreferencesHelper;

public class CurrencyConverter {

    private static final CurrencyConverter INSTANCE = new CurrencyConverter();
    private boolean isInitialized;

    private SharedPreferences sharedPref;


    private SharedPreferences.Editor sharedPrefEditor;

    private Context context;

    private CurrencyConverter(){
        this.isInitialized = false;
    }

    public static CurrencyConverter getInstance() {
        return ((INSTANCE != null) ? INSTANCE : new CurrencyConverter());
    }

    public void setContext(Context context) {
        this.context = context;
        if(context != null)
            this.isInitialized = true;
    }
    public void init(Context context){
        setContext(context);
        this.sharedPref = context.getSharedPreferences(SharedPreferencesHelper.CURRENCY_PREFERENCES_FILE, context.MODE_PRIVATE);
    }

 public Currency getCurrency(){
        if(isInitialized)
            return Currency.valueOf(sharedPref.getString(SharedPreferencesHelper.CURRENCY_KEY, SharedPreferencesHelper.CURRENCY_DEFAULT_VALUE));
        else
            throw new NullPointerException("Context needs to be initialized before you use this Singleton!");
    }


   public void setCurrency(Currency currency){
        //TODO: a mudar
        if(isInitialized){
            this.sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.putString(SharedPreferencesHelper.CURRENCY_KEY, currency.toString());
            sharedPrefEditor.apply();
        }else
            throw new NullPointerException("Context needs to be initialized before you use this Singleton");
    }

//    public void updateLocalExpenditures(Currency currency, Currency lastCurrency) {
//        List<Expenditure> expenses, listToDelete;
//        listToDelete = expenses = manager.getAllExpenditures();
//        for(Expenditure e : listToDelete){
//            manager.removeExpenditure(e);
//        }
//        for(Expenditure e : expenses){
//            e.setValue(lastCurrency.convert(e.getValue(),currency));
//            manager.insertExpenditure(e);
//        }
//    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

