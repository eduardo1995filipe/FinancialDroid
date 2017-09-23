package bagarrao.financialdroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by eduar on 23/09/2017.
 */

public class CurrencyConverter {

    private static final String CURRENCY_PREF = "currencyPreferences";

    private static CurrencyConverter INSTANCE = null;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Context context;
    private Currency currency;

    private CurrencyConverter(Context context) {
        this.context = context;
        this.sharedPref = context.getSharedPreferences(CURRENCY_PREF,context.MODE_PRIVATE);
        this.currency = Currency.DEFAULT_CURRENCY;
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
        this.editor = sharedPref.edit();
        editor.putString(CURRENCY_PREF,currency.toString());
        editor.commit();
    }

    public Currency getCurrentCurrency(){
        return Currency.valueOf(sharedPref.getString(CURRENCY_PREF.toString(),Currency.DEFAULT_CURRENCY.toString()));
    }
}
