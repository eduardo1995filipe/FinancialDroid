package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import bagarrao.financialdroid.R;

public class AnalyticsActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ListView[] monthList;
    private ImageButton[] chartByMonthButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
    }

    public void init() {

    }
}
