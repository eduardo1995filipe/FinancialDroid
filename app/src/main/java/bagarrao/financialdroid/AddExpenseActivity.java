package bagarrao.financialdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import bagarrao.financialdroid.database.ExpenseDataSource;

public class AddExpenseActivity extends AppCompatActivity {

    private Button addExpenseButton;
    private Spinner expenseTypeSpinner;
    private EditText priceEditText;
    private EditText descriptionEditText;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private ExpenseDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Expense sucessefully registered!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * initializes all the elements
     */
    public void init() {
        this.addExpenseButton = (Button) findViewById(R.id.addExpenseButton);
        this.priceEditText = (EditText) findViewById(R.id.priceEditText);
        this.descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        this.expenseTypeSpinner = (Spinner) findViewById(R.id.expenseTypeSpinner);
        this.spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.expense_type_kind, R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        expenseTypeSpinner.setAdapter(spinnerAdapter);
//        db init
        this.dataSource = new ExpenseDataSource(this);
        this.dataSource.open();
    }


}
