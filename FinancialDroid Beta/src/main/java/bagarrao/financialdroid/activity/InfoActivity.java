package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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

    private ImageView imgV1;
    private ImageView imgV2;
    private TextView txtViewLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        MobileAds.initialize(this, "ca-app-pub-8899468184876323/3142457027");

        this.imgV1 = (ImageView)findViewById(R.id.imageView2);
        imgV1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.linkedin.com/in/eduardo-bagarrão-2a1464113/"));
                startActivity(intent);
            }
        });

        this.imgV2 = (ImageView)findViewById(R.id.imageView3);
        imgV2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://beatrizcrisstomo.wixsite.com/arte-e-design/portf-lio"));
                startActivity(intent);
            }
        });

        this.txtViewLink = (TextView) findViewById(R.id.textViewLink2_2);
        txtViewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://morelovebeatriz.blogspot.pt"));
                startActivity(intent);
            }
        });

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
