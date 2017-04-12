package bagarrao.financialdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Eduardo Bagarrao
 */
public class SplashScreenActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
