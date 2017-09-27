package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import bagarrao.financialdroid.backup.Backup;
import bagarrao.financialdroid.migration.Migrator;
import bagarrao.financialdroid.currency.Currency;

/**
 * @author Eduardo Bagarrao
 */
public class SplashScreenActivity extends AppCompatActivity {

	public static final String DEFAULT_CURRENCY = "defaultCurrency";
    public static final double AD_PROBABILITY = 0.2;

    private Intent intent;
	private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Backup(this).go();
        if (Migrator.needsMigration(this)) {
            new Migrator(this).run();
        }
        this.intent = new Intent(this, MainActivity.class);
		this.sharedPref = getSharedPreferences(DEFAULT_CURRENCY,MODE_PRIVATE);
		if(!sharedPref.contains(DEFAULT_CURRENCY)){
			this.editor = sharedPref.edit();
			editor.putString(DEFAULT_CURRENCY, Currency.EUR.toString()/*change to magic variable*/);
			editor.commit();	
		}
		if(Math.random() <= AD_PROBABILITY)
		    startActivity(new Intent(this, InterstitialAdActivity.class));
		else
		    startActivity(intent);
        finish();
    }
}
