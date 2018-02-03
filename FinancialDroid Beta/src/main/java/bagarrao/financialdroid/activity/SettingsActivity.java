package bagarrao.financialdroid.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.currency.Currency;
import bagarrao.financialdroid.utils.SharedPreferencesHelper;

public class SettingsActivity extends AppCompatActivity {

//	private CurrencyConverter currencyConverter = CurrencyConverter.getInstance();

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;

    private Spinner currencySpinner;
    private ArrayAdapter<CharSequence> currencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        setListeners();
    }

    private void init(){
        this.sharedPref = getSharedPreferences(SharedPreferencesHelper.CURRENCY_SPINNER_PREFERENCES_FILE,MODE_PRIVATE);
        this.currencySpinner = (Spinner)findViewById(R.id.currencyTypeSpinner);
        this.currencyAdapter = ArrayAdapter.createFromResource(this, R.array.currency_type,
                R.layout.support_simple_spinner_dropdown_item);
        currencyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);
        int startPosition = sharedPref.getInt(SharedPreferencesHelper.CURRENCY_SPINNER_POSITION_KEY,SharedPreferencesHelper.CURRENCY_SPINNER_POSITION_DEFAULT_VALUE);
        currencySpinner.setSelection(startPosition);
    }

    public void setListeners(){
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            Currency currentCurrency = null;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
				 currentCurrency = getCurrencyOrder(position);
				}catch(NullPointerException e){
					currentCurrency = Currency.DEFAULT_CURRENCY;
				}
				finally{
				    //TODO: call firebase  method to change currency
//                    manager.changeCurrency(currentCurrency);
//					currencyConverter.setCurrency(currentCurrency); call database manager
                    Toast.makeText(getApplicationContext(),"Current currency is now ["
//                            + currencyConverter.getCurrency().toString()
                            + "]",Toast.LENGTH_SHORT);
                    sharedPrefEditor = sharedPref.edit();
                    sharedPrefEditor.putInt(SharedPreferencesHelper.CURRENCY_SPINNER_POSITION_KEY,position);
                    sharedPrefEditor.apply();
				}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public Currency getCurrencyOrder(int index) {
        Currency currency;
        switch (index) {
            case 0:
                currency = Currency.EUR;
                break;
			case 1:
                currency = Currency.USD;
                break;
			case 2:
                currency = Currency.AUD;
                break;
			case 3:
                currency = Currency.BRL;
                break;
			case 4:
                currency = Currency.JPY;
                break;
			case 5:
                currency = Currency.KRW;
                break;
			case 6:
                currency = Currency.CNY;
                break;
			case 7:
                currency = Currency.GBP;
                break;
            default:
                Log.e("ExpensesActivity", "expenseOrder returned NULL");
                throw new NullPointerException();
        }
        return currency;
    }
}
