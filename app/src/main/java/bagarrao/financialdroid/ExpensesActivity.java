package bagarrao.financialdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import bagarrao.financialdroid.database.ExpenseDataSource;

public class ExpensesActivity extends AppCompatActivity {

    private ListView expenseListView;
    private Spinner orderSpinner;

    //    lists
    private List<Expense> expenseList;
    private ArrayList<String> expenseListString = new ArrayList<>();
    ;

    //    db
    private ExpenseDataSource dataSource;

    //    adapters
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private ArrayAdapter<String> expenseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * initializes all the elements
     */
    public void init() {
        //        init DB
//        this.dataSource = new ExpenseDataSource(this);
//        this.dataSource.open();

        //layout
        this.expenseListView = (ListView) findViewById(R.id.expensesListView);
        this.orderSpinner = (Spinner) findViewById(R.id.expensesSpinner);

        //        init Adapters
        this.spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.order_kind,
                R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        orderSpinner.setAdapter(spinnerAdapter);
        this.expenseListAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                android.R.layout.simple_list_item_1, expenseListString);
        expenseListView.setAdapter(expenseListAdapter);

        //        dbReader
//        readDB();
    }

    /**
     * reads and converts the database to an ArrayList, and Updates the ListView View
     */
    public void readDB() {
        this.expenseList = dataSource.getAllExpenses();
        if (expenseList == null)
            expenseList = new ArrayList<>();
        expenseListString.clear();
        for (Expense e : expenseList) {
            expenseListString.add(e.toString());
        }
        expenseListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
//        dataSource.open();
//        readDB();
        super.onResume();
    }

    @Override
    protected void onPause() {
//        dataSource.close();
        super.onPause();
    }

}
