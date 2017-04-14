package bagarrao.financialdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import bagarrao.financialdroid.backup.BackupManager;

/**
 * @author Eduardo Bagarrao
 */
public class SplashScreenActivity extends AppCompatActivity {

    private BackupManager bm = BackupManager.getInstance();

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bm.setContext(this);
        if (bm.isAutoBackupEnabled())
            bm.createBackup();
        this.intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
