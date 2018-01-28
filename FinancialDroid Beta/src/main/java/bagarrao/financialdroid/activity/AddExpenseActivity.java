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

import bagarrao.financialdroid.database.DataManager;
import bagarrao.financialdroid.R;
import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.DateParser;


/**
 * Class that is used to add a new {@link Expenditure} object, whether on
 * local storage ({@link bagarrao.financialdroid.database.DataSource}) or
 * with  insert it to online storage.
 *
 * @author Eduardo Bagarrao
 */
public class AddExpenseActivity extends AppCompatActivity {

    private DataManager manager = DataManager.getInstance();

    /**
     * {@link Button} to add a new {@link Expenditure} to the database,
     * {@link com.google.firebase.database.FirebaseDatabase} or to
     * {@link bagarrao.financialdroid.database.DataSource}.
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
     * to the current {@link bagarrao.financialdroid.currency.Currency}.
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
     * {@link Date} that is assigned to the {@link Expenditure}
     * object that ir being created.
     */
    private Date expenditureDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        init();
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        expenditureTypeSpinner.setAdapter(spinnerAdapter);
        priceTextView.setText("Price ( " /* + manager.getCurrency().toString() */ +  " ) :");
        setListeners();
    }

    @Override
    protected void onResume() {
        expenditureDate = new Date();
        super.onResume();
    }

    @Override
    protected void onPause() {
        expenditureTypeSpinner.setSelection(0);
        expenditureDate = null;
        super.onPause();
    }

    /**
     * initializes all the objects of the class.
     * {@link bagarrao.financialdroid.database.DataSource}
     * will only be initiated on a local database.
     */
    public void init() {
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
     * {@link bagarrao.financialdroid.database.DataSource} if the user is using the local storage,
     * or if it will be inserted in {@link com.google.firebase.database.FirebaseDatabase} with
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
                if(!manager.isLocal())
                    expenditure.setUid(manager.getUser().getUid());
                manager.insert(expenditure);
                Toast.makeText(this, expenditure.toString() + "| time: " + expenditure.getTime(), Toast.LENGTH_SHORT).show();
                finish();
            } else
                Toast.makeText(getApplicationContext(),
                        "Fill the fields that are null before register your new Expense!!", Toast.LENGTH_SHORT).show();
        });
    }
}