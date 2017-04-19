package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import bagarrao.financialdroid.backup.Backup;
import bagarrao.financialdroid.migration.Migrator;

/**
 * @author Eduardo Bagarrao
 */
public class SplashScreenActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Backup(this).go();
        if (Migrator.needsMigration(this)) {
            new Migrator(this).run();
        }
        this.intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
