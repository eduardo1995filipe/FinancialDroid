package bagarrao.financialdroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bagarrao.financialdroid.Expense.Expense;
import bagarrao.financialdroid.Expense.ExpenseType;
import bagarrao.financialdroid.backup.Backup;
import bagarrao.financialdroid.database.ExpenseDataSource;

/**
 * @author Eduardo Bagarrao
 */
public class AddExpenseActivity extends AppCompatActivity {

    private Button addExpenseButton;
    private Spinner expenseTypeSpinner;
    private EditText priceEditText;
    private EditText descriptionEditText;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private ExpenseDataSource dataSource;
    private CalendarView dateCalendarView;
    private Date expenseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        init();
        setListeners();
    }

    @Override
    protected void onResume() {
        expenseDate = new Date();
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        expenseTypeSpinner.setSelection(0);
        expenseDate = null;
        dataSource.close();
        super.onPause();
    }

    /**
     * initializes all the elements
     */
    public void init() {
        this.addExpenseButton = (Button) findViewById(R.id.addExpenseButton);
        this.priceEditText = (EditText) findViewById(R.id.priceEditText);
        this.descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        this.expenseTypeSpinner = (Spinner) findViewById(R.id.expenseTypeSpinner);
        this.dateCalendarView = (CalendarView) findViewById(R.id.dateCalendarView);
        this.spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.expense_type_kind, R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        expenseTypeSpinner.setAdapter(spinnerAdapter);
        this.dataSource = new ExpenseDataSource(this);
        this.expenseDate = new Date();
    }

    /**
     * sets the listeners of the views
     */
    public void setListeners() {
        dateCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String myDate = dayOfMonth + "-" + month + "-" + year;
                try {
                    expenseDate = new SimpleDateFormat("dd-M-yyyy").parse(myDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = priceEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                boolean noNullFields = !price.trim().equals("") && !description.trim().equals("");
                if (noNullFields) {
                    Expense expense = new Expense(Double.parseDouble(priceEditText.getText().toString()),
                            ExpenseType.valueOf(expenseTypeSpinner.getSelectedItem().toString().toUpperCase()), descriptionEditText.getText().toString(),
                            expenseDate);
                    dataSource.createExpense(expense);
                    new Backup().go();
                    Toast.makeText(getApplicationContext(), "Expense sucessefully registered!", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Fill the fields that are null before register your new Expense!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
