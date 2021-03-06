package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import bagarrao.financialdroid.R;

/**
 * @author Eduardo Bagarrao
 */
public class InterstitialAdActivity extends AppCompatActivity {

    private Intent mainIntent;
    private InterstitialAd interstitialAd;
    private Runnable adWaiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad);
        init();
        setup();
        setListeners();
    }

    /**
     * inits all the class objects
     */
    private void init(){
        this.mainIntent = new Intent(this, LoginActivity.class);
        this.interstitialAd = new InterstitialAd(this);
        this.adWaiter = () -> {
            try {
                Thread.sleep(2000);
                startActivity(mainIntent);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * setup all class objects
     */
    private void setup(){
        this.interstitialAd.setAdUnitId("ca-app-pub-8899468184876323/9676301128");
        this.interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    /**
     * sets the needed listeners for the class objects
     */
    private void setListeners(){
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
                startActivity(mainIntent);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
                Toast.makeText(getApplicationContext(),"Don't forget that you are helping us while you click in the ad! :D", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
                new Thread(adWaiter).start();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed");
                startActivity(mainIntent);
                finish();
            }
        });
    }

}