package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.backup.Backup;
import bagarrao.financialdroid.currency.Currency;
import bagarrao.financialdroid.currency.CurrencyConverter;

public class SettingsActivity extends AppCompatActivity {

	private CurrencyConverter currencyConverter = CurrencyConverter.getInstance();

    private Currency currentCurrency = Currency.DEFAULT_CURRENCY;

    private Button exportButton;
    private Button importButton;

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
        this.exportButton = (Button)findViewById(R.id.exportCsvButton);
        this.importButton = (Button)findViewById(R.id.importCsvButton);

        this.currencySpinner = (Spinner)findViewById(R.id.currencyTypeSpinner);
        this.currencyAdapter = ArrayAdapter.createFromResource(this, R.array.currency_type,
                R.layout.support_simple_spinner_dropdown_item);
        currencyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);
    }

    public void setListeners(){
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Backup().go();
            }
        });

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO import button function
            }
        });
		
		currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
				 currentCurrency = getCurrencyOrder(position);
				}catch(NullPointerException e){
					currentCurrency = Currency.DEFAULT_CURRENCY;
				}
				finally{
					currencyConverter.setCurrency(currentCurrency);
                    Toast.makeText(getApplicationContext(),"Current currency is now [" + currencyConverter.getCurrentCurrency().toString() + "]",Toast.LENGTH_SHORT);
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
