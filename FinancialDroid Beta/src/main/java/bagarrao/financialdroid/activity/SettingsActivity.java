package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.backup.Backup;

public class SettingsActivity extends AppCompatActivity {

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
    }

}
