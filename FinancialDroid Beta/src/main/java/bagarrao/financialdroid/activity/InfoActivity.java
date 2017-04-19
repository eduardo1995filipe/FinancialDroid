package bagarrao.financialdroid.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import bagarrao.financialdroid.R;

/**
 * @author Eduardo Bagarrao
 */
public class InfoActivity extends AppCompatActivity {

    public static final String ACTIVITY_TITLE = "About FinancialDroid";

    private TextView versionTextView;
    private PackageManager packageManager;
    private PackageInfo info;
    private String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(ACTIVITY_TITLE);
        setSupportActionBar(toolbar);
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
