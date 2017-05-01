package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import java.util.LinkedList;

public class AnalyticsActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    private LinkedList<Integer> yearList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     */
    public void init() {
        this.linearLayout = new LinearLayout(this);
        this.linearLayout.setOrientation(LinearLayout.VERTICAL);
        this.yearList = new LinkedList<>();
    }

    /**
     *
     */
    public void setupLayout() {
        setContentView(linearLayout);
    }
}
