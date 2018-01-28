package bagarrao.financialdroid.currency;

import android.content.Context;
import android.content.SharedPreferences;

import bagarrao.financialdroid.utils.SharedPreferencesHelper;

public class CurrencyManager {

    private static final CurrencyManager INSTANCE = null;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;

    public static CurrencyManager getInstance() {
        return (INSTANCE != null) ? INSTANCE : new CurrencyManager();
    }

    private CurrencyManager() {
//        this.sharedPref = context.getSharedPreferences(SharedPreferencesHelper.CURRENCY_PREFERENCES_FILE, context.MODE_PRIVATE);
    }

    public Currency getCurrency(){
        return Currency.valueOf(sharedPref.getString(SharedPreferencesHelper.CURRENCY_KEY, SharedPreferencesHelper.CURRENCY_DEFAULT_VALUE));
    }

    public void setCurrency(Currency currency){
            this.sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.putString(SharedPreferencesHelper.CURRENCY_KEY, currency.toString());
            sharedPrefEditor.apply();
    }
}
