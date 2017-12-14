package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Random;

import bagarrao.financialdroid.backup.Backup;
import bagarrao.financialdroid.currency.CurrencyConverter;
import bagarrao.financialdroid.migration.Migrator;

/**
 * @author Eduardo Bagarrao
 */
public class SplashScreenActivity extends AppCompatActivity {

    private CurrencyConverter currencyConverter = CurrencyConverter.getInstance();

	private static final double AD_PROBABILITY = 0.4;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyConverter.init(this);
        new Backup(this).go(); //for now its not needed
        this.intent = new Intent(this, LoginActivity.class);
        setup();
        finish();
    }

    /**
     * setup of the class objects
     */
    public void setup(){
        if (Migrator.needsMigration(this)) {
            new Migrator(this).run();
        }
        if(Math.random() < AD_PROBABILITY)
            startActivity(new Intent(this, InterstitialAdActivity.class));
        else
            startActivity(intent);
    }
}
