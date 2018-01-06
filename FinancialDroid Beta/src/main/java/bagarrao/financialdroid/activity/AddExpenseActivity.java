package bagarrao.financialdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.currency.CurrencyConverter;
import bagarrao.financialdroid.database.DataSource;
import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.expense.ExpenseDistributor;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.firebase.FirebaseManager;
import bagarrao.financialdroid.utils.DateParser;


/**
 * Class that is used to add a new {@link Expenditure} object,
 * whether on local storage ({@link #dataSource}) or with {@link #manager}
 * insert it to online storage.
 *
 * @author Eduardo Bagarrao
 */
public class AddExpenseActivity extends AppCompatActivity {

    /**
     * {@link com.google.firebase.database.FirebaseDatabase} singleton instance.
     */
    private FirebaseManager manager = FirebaseManager.getInstance();

    /**
     * {@link CurrencyConverter} singleton instance.
     */
    private CurrencyConverter currencyConverter = CurrencyConverter.getInstance();

    /**
     * {@link Button} to add a new {@link Expenditure} to the database,
     * {@link com.google.firebase.database.FirebaseDatabase} or to {@link #dataSource}.
     */
    private Button addExpenditureButton;

    /**
     * {@link Spinner} that shows all the {@link ExpenseType}
     * values of the {@link Expenditure} objects.
     */
    private Spinner expenditureTypeSpinner;

    /**
     * {@link EditText} that contains the price of the
     * {@link Expenditure} object that will be added.
     */
    private EditText priceEditText;

    /**
     * {@link EditText} that contains the description of the
     * {@link Expenditure} object that will be added.
     */
    private EditText descriptionEditText;

    /**
     * {@link TextView} of the price of the {@link Expenditure}
     * object. It's called programmatically to set the text according
     * to the current {@link bagarrao.financialdroid.currency.Currency}
     * that's accessible in the {@link #currencyConverter}.
     */
    private TextView priceTextView;

    /**
     * {@link CalendarView} to set's the {@link Expenditure}
     *  date object that will be added.
     */
    private CalendarView dateCalendarView;

    /**
     * {@link ArrayAdapter<CharSequence>} that stores the
     * {@link #expenditureTypeSpinner} {@link ExpenseType} values.
     */
    private ArrayAdapter<CharSequence> spinnerAdapter;

    /**
     * {@link DataSource} to store {@link Expenditure}
     * objects, is the {@link #isLocal} value is true.
     */
    private DataSource dataSource;

    /**
     * {@link Date} that is assigned to the {@link Expenditure}
     * object that ir being created.
     */
    private Date expenditureDate;

    /**
     * Value that decides whether if is used {@link #dataSource} as
     * local storage or if the {@link Expenditure} objects are stored
     * in the {@link com.google.firebase.database.FirebaseDatabase}.
     */
    private boolean isLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        init();

        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        expenditureTypeSpinner.setAdapter(spinnerAdapter);
        priceTextView.setText("Price(" + currencyConverter.getCurrency().toString() + ")");

        setListeners();
    }

    @Override
    protected void onResume() {
        expenditureDate = new Date();
        if (isLocal && dataSource != null)
            dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        expenditureTypeSpinner.setSelection(0);
        expenditureDate = null;
        if (isLocal && dataSource != null)
            dataSource.close();
        super.onPause();
    }

    /**
     * initializes all the objects of the class. {@link #dataSource}
     * will only be initiated if {@link #isLocal} has true value.
     */
    public void init() {
        this.isLocal = (!manager.getUid().equals(""));
        this.dataSource = isLocal ? null : new DataSource(DataSource.CURRENT, this);
        this.expenditureDate = new Date();
        this.priceTextView = (TextView) findViewById(R.id.priceTextView);
        this.addExpenditureButton = (Button) findViewById(R.id.addExpenseButton);
        this.priceEditText = (EditText) findViewById(R.id.priceEditText);
        this.descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        this.expenditureTypeSpinner = (Spinner) findViewById(R.id.expenseTypeSpinner);
        this.dateCalendarView = (CalendarView) findViewById(R.id.dateCalendarView);
        this.spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.expense_type_kind, R.layout.support_simple_spinner_dropdown_item);
    }

    /**
     * Sets the listeners for the {@link #dateCalendarView} and for the {@link #addExpenditureButton}.
     * On {@link #addExpenditureButton} the {@link Expenditure} object created will be inserted on
     * {@link #dataSource} if the user is using the local storage, or if it will be inserted
     * in {@link com.google.firebase.database.FirebaseDatabase} with {@link #manager}.
     */
    public void setListeners() {
        dateCalendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String myDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            try {
                expenditureDate = DateParser.parseDate(myDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        addExpenditureButton.setOnClickListener(v -> {
            String price = priceEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            boolean noNullFields = !price.trim().equals("") && !description.trim().equals("");
            if (noNullFields) {
                Expenditure expenditure = new Expenditure(Double.parseDouble(priceEditText.getText().toString()),
                        ExpenseType.valueOf(expenditureTypeSpinner.getSelectedItem().toString().toUpperCase()),
                        descriptionEditText.getText().toString(),
                        expenditureDate);
                if(isLocal)
                    ExpenseDistributor.addNewExpense(expenditure, getApplicationContext(), dataSource,
                        new DataSource(DataSource.ARCHIVE, getApplicationContext()));
                else
                    manager.insertExpenditure(expenditure);
                Toast.makeText(getApplicationContext(), "Expense sucessefully registered!", Toast.LENGTH_SHORT).show();
                finish();
            } else
                Toast.makeText(getApplicationContext(),
                        "Fill the fields that are null before register your new Expense!!", Toast.LENGTH_SHORT).show();
        });
    }
}