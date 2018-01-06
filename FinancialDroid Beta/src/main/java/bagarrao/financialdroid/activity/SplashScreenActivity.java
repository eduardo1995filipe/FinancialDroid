package bagarrao.financialdroid.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import bagarrao.financialdroid.currency.CurrencyConverter;

/**
 * Simple splash screen activity that shows
 * the logo and the name of the application.
 *
 * @author Eduardo Bagarrao
 */
public class SplashScreenActivity extends AppCompatActivity {

    /**
     * Probability of {@link InterstitialAdActivity} appear instead
     * of {@link LoginActivity}. This is also dependent from
     * the available internet connection.
     */
    private static final double AD_PROBABILITY = 0.35;

    /**
     * {@link CurrencyConverter} singleton instance.
     */
    private CurrencyConverter currencyConverter = CurrencyConverter.getInstance();

    /**
     * {@link Intent} that will launch {@link LoginActivity}.
     */
    private Intent loginIntent;

    /**
     * {@link Intent} that will launch {@link InterstitialAdActivity}.
     */
    private Intent interstitialAdIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new Backup(this).go();
        this.loginIntent = new Intent(this, LoginActivity.class);
        this.interstitialAdIntent = new Intent(this, InterstitialAdActivity.class);
        this.currencyConverter.init(this);
        setup();
        finish();
    }

    /**
     * setup of the {@link SplashScreenActivity} objects.
     * Checks if the with a random number if {@link InterstitialAdActivity}
     * will appear or if it goes to {@link LoginActivity} without showing the ad Activity.
     */
    public void setup(){
//        if (Migrator.needsMigration(this)) { isto ja nao vai ser feito aqui
//            new Migrator(this).run();
//        }
        if(Math.random() < AD_PROBABILITY && isNetworkConnected())
            startActivity(interstitialAdIntent);
        else
            startActivity(loginIntent);
    }

    /**
     * Checks whether if exists a internet connection available or not.
     * This will decide whether if the {@link InterstitialAdActivity} will
     * appear or not.
     *
     * @return boolean
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
