package bagarrao.financialdroid;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private TextView versionTextView;
    private PackageManager packageManager;
    private PackageInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("About FinancialDroid");
        setSupportActionBar(toolbar);
        this.packageManager = this.getPackageManager();
        String version = "";
        try {
            this.info = packageManager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.versionTextView = (TextView) findViewById(R.id.versionEditText);
        this.versionTextView.setText("FinancialDroid " + version + "v");

    }

}
