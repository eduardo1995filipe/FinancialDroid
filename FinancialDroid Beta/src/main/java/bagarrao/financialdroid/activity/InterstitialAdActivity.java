package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import bagarrao.financialdroid.R;

public class InterstitialAdActivity extends AppCompatActivity {


    private Intent mainIntent;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad);

        this.mainIntent = new Intent(this, MainActivity.class);

        this.interstitialAd = new InterstitialAd(this);
        this.interstitialAd.setAdUnitId("ca-app-pub-8899468184876323/9676301128");
        this.interstitialAd.loadAd(new AdRequest.Builder().build());

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
                Runnable waitForAd = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(2000);
                            startActivity(mainIntent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(waitForAd).start();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed");
                startActivity(mainIntent);
            }
        });

    }
}




