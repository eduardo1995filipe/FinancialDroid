package bagarrao.financialdroid.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import bagarrao.financialdroid.R;

/**
 * @author Eduardo Bagarrao
 */
public class InfoActivity extends AppCompatActivity {

    private TextView versionTextView;
    private PackageManager packageManager;
    private PackageInfo info;
    private String version;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        MobileAds.initialize(this, "ca-app-pub-8899468184876323/3142457027");

        this.adView = (AdView) findViewById(R.id.infoAdBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
        init();
    }

    /**
     * initializes all the elements
     */
    public void init() {
        this.packageManager = this.getPackageManager();
        try {
            this.info = packageManager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            this.version = "[ERROR]???";
        } finally {
            this.versionTextView = (TextView) findViewById(R.id.versionEditText);
            this.versionTextView.setText("FinancialDroid Beta v" + version);
        }
    }
}
