package bagarrao.financialdroid.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.currency.CurrencyConverter;
import bagarrao.financialdroid.database.DataSource;
import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.expense.ExpenseOrder;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.firebase.FirebaseManager;
import bagarrao.financialdroid.utils.DateParser;
import bagarrao.financialdroid.utils.Filter;

/**
 *
 * This class shows {@link Expenditure} objects that have less than one month.
 * The {@link Expenditure} objects can be sorted for {@link java.util.Date} and
 * for his value, both for in a ascending or descending way, and can be also
 * filtered by {@link ExpenseType}.
 *
 * @author Eduardo Bagarrao
 */
public class ExpensesActivity extends AppCompatActivity {

    /**
     * Default order that the {@link Expenditure} objects are sorted.
     */
    public static final ExpenseOrder DEFAULT_ORDER = ExpenseOrder.DATE_DESCENDING;

    /**
     * Default {@link ExpenseType}. Its assigned to null by default.
     */
    public static final ExpenseType DEFAULT_EXPENSE_TYPE = null;

    /**
     * {@link com.google.firebase.database.FirebaseDatabase} singleton instance.
     */
    private FirebaseManager manager = FirebaseManager.getInstance();

    /**
     * {@link CurrencyConverter} singleton instance.
     */
    private CurrencyConverter currencyConverter = CurrencyConverter.getInstance();

    /**
     * Value that decides whether if is used {@link #dataSource} as
     * local storage or if the {@link Expenditure} objects are stored
     * in the {@link com.google.firebase.database.FirebaseDatabase}.
     */
    private boolean isLocal;

    /**
     * View of the banner.
     */
    private AdView adView;

    /**
     * actual {@link ExpenseOrder} of the {@link Expenditure} objects.
     *
     * @see #DEFAULT_ORDER
     */
    private ExpenseOrder currentOrder;

    /**
     * actual {@link ExpenseType}. default value is set to null.
     *
     * @see #DEFAULT_EXPENSE_TYPE
     */
    private ExpenseType currentType;

    /**
     * {@link Spinner} that stores the order that
     * the {@link Expenditure} objects are sorted.
     * Default value is ascending.
     *
     * @see #DEFAULT_ORDER
     */
    private Spinner orderBySpinner;

    /**
     * {@link Spinner} that stores the {@link ExpenseType}
     * that the {@link Expenditure} objects are filtered.
     * Default value is null(all {@link ExpenseType} are shown).
     *
     * @see #DEFAULT_EXPENSE_TYPE
     */
    private Spinner typeSpinner;

    /**
     * {@link ArrayAdapter<CharSequence>} for the orders
     * of the {@link #orderBySpinner}.
     */
    private ArrayAdapter<CharSequence> spinnerOrderByAdapter;

    /**
     * {@link ArrayAdapter<CharSequence>} for the {@link ExpenseType} values
     * of the {@link #typeSpinner}.
     */
    private ArrayAdapter<CharSequence> spinnerTypeAdapter;

    /**
     * {@link ListView} populated by all recent {@link Expenditure}
     * objects, with the respective sort and filter.
     *
     * @see ExpenseType
     * @see bagarrao.financialdroid.utils.Sorter
     * @see Filter
     */
    private ListView expenseListView;

    /**
     * {@link List<Expenditure>} of all recent {@link Expenditure}
     * objects.
     */
    private List<Expenditure> expenseList;

    /**
     * {@link List<Expenditure>} of all recent {@link Expenditure}
     * objects.
     */
    private ArrayList<String> expenseListString;

    /**
     * {@link DataSource} that is used for local storage({@link #isLocal}
     * at true value), otherwise will be used {@link #manager} to use
     * {@link com.google.firebase.database.FirebaseDatabase}.
     */
    private DataSource dataSource;

    /**
     * {@link ArrayAdapter<String>} fpr {@link #expenseList}.
     */
    private ArrayAdapter<String> expenseListAdapter;

    /**
     * {@link Context} of {@link ExpensesActivity}.
     */
    private Context context;

    /**
     * {@link AdRequest} for {@link #adView}.
     */
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        MobileAds.initialize(this, "ca-app-pub-8899468184876323/7706665790");
        init();
        setup();
        readDB();
        setListeners();
    }

    /**
     * initializes all the objects of the class. {@link #dataSource}
     * will only be initiated if {@link #isLocal} has true value.
     */
    public void init() {
        this.isLocal = manager.getUid().equals("");
        this.context = this;
        this.dataSource = (!isLocal) ? null : new DataSource(DataSource.CURRENT, this);
        this.adView = (AdView) findViewById(R.id.expenseViewerAdBanner);
        this.adRequest = new AdRequest.Builder().build();
        this.currentOrder = DEFAULT_ORDER;
        this.currentType = DEFAULT_EXPENSE_TYPE;
        this.expenseListString = new ArrayList<>();
        this.expenseListView = (ListView) findViewById(R.id.expensesListView);
        this.orderBySpinner = (Spinner) findViewById(R.id.expenseOrderBySpinner);
        this.spinnerOrderByAdapter = ArrayAdapter.createFromResource(this, R.array.order_kind,
                R.layout.support_simple_spinner_dropdown_item);
        this.typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        this.spinnerTypeAdapter = ArrayAdapter.createFromResource(this, R.array.filter_expense_type,
                R.layout.support_simple_spinner_dropdown_item);
        this.expenseListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, expenseListString);
    }

    /**
     * Setups some of the initialized objects. If {@link #isLocal} has
     * a true value, {@link #dataSource} will be opened.
     * {@link #adView} is loaded here.
     */
    public void setup(){
        if(isLocal && dataSource != null)
            dataSource.open();
        spinnerOrderByAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        orderBySpinner.setAdapter(spinnerOrderByAdapter);
        spinnerTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinnerTypeAdapter);
        expenseListView.setAdapter(expenseListAdapter);
        adView.loadAd(adRequest);
    }

    /**
     * Sets the listeners for the {@link #expenseListView}, for the {@link #orderBySpinner} and
     * for the {@link #typeSpinner}. On {@link #expenseListView}, on every click a {@link android.app.Dialog}
     * should appear that requests if the user want's to delete the {@link Expenditure} that was clicked.
     *
     * On {@link #orderBySpinner} every change will sort the {@link #expenseList}.
     *
     * On {@link #orderBySpinner} every change will filter {@link #expenseList}.
     *
     * @see bagarrao.financialdroid.utils.Sorter
     * @see Filter
     */
    public void setListeners() {
        expenseListView.setOnItemClickListener((parent, view, position, id) -> showDeleteDialog(context, position));
        orderBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    currentOrder = getExpenseOrder(position);
                } catch (NullPointerException e) {
                    currentOrder = DEFAULT_ORDER;
                } finally {
                    readDB();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    currentType = ExpenseType.getTypeByIndex(position);
                } catch (NullPointerException e) {
                    currentType = DEFAULT_EXPENSE_TYPE;
                } finally {
                    readDB();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Gives an {@link ExpenseOrder} based on the
     * {@link Integer} that is given on the parameter.
     *
     * @param index int
     * @return ExpenseOrder
     */
    public ExpenseOrder getExpenseOrder(int index) {
        ExpenseOrder expenseOrder;
        switch (index) {
            case 0:
                expenseOrder = ExpenseOrder.DATE_DESCENDING;
                break;
            case 1:
                expenseOrder = ExpenseOrder.DATE_ASCENDING;
                break;
            case 2:
                expenseOrder = ExpenseOrder.PRICE_DESCENDING;
                break;
            case 3:
                expenseOrder = ExpenseOrder.PRICE_ASCENDING;
                break;
            default:
                Log.e("ExpensesActivity", "expenseOrder returned NULL");
                throw new NullPointerException();
        }
        return expenseOrder;
    }

    /**
     * Show's a {@link android.app.Dialog} interface to user to confirm
     * that he's sure to delete the {@link Expenditure} given in the
     * second parameter.
     *
     * @param context {@link Context}
     * @param index   int
     */
    public void showDeleteDialog(final Context context, final int index) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this expense?");
        builder.setTitle("Delete Expense");
        builder.setPositiveButton("Yes", (dialog, which) -> {
                removeExpense(index);
                Toast.makeText(context, "Expense removed successfully!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    /**
     * Removes a {@link Expenditure} object that corresponds to the given index on the parameter.
     * Either removes the {@link Expenditure} from {@link #dataSource} or from
     * {@link com.google.firebase.database.FirebaseDatabase} throug {@link #manager},
     * depending of the {@link #isLocal} value.
     *
     * @param index int
     */
    public void removeExpense(int index) {
            this.expenseList = (!isLocal) ? manager.getAllExpenditures() : dataSource.getAllExpenditures();
        if (expenseList == null)
            expenseList = new ArrayList<>();
        else if ((expenseList.size() - 1) >= index) {
            Expenditure e = expenseList.get(index);
            if(isLocal)
                dataSource.deleteExpenditure(e);
            else
                manager.removeExpenditure(e);
            expenseList.remove(index);
        }
        expenseListString.clear();
        for (Expenditure e : expenseList) {
            String stringExpense = e.getDescription() + " | " + e.getValue() + " " + currencyConverter.getCurrency().toString() + " | " +
                    DateParser.parseString(e.getDate()) + " | " + e.getType().toString();
            expenseListString.add(stringExpense);
        }
        expenseListAdapter.notifyDataSetChanged();
    }

    /**
     * Reloads {@link #expenseList}, either from local storage ({@link #dataSource}) or
     * to {@link com.google.firebase.database.FirebaseDatabase} ({@link #manager}), based
     * on {@link #isLocal} value.
     *
     * Assigns {@link #expenseList} values to {@link #expenseListString}, sorts and
     * filters it, and then notifies {@link #expenseListAdapter}.
     */
    public void readDB() {
        this.expenseList = (!isLocal) ? manager.getAllExpenditures() : dataSource.getAllExpenditures();
        if (expenseList == null)
            expenseList = new ArrayList<>();
        expenseListString.clear();
        currentOrder.sortByOrder(expenseList);
        expenseList = Filter.filterExpensesByType(expenseList,currentType);
        for (Expenditure e : expenseList) {
            String stringExpense = e.getDescription() +
                    " | " + CurrencyConverter.round(e.getValue(), 2) +
                    " " + currencyConverter.getCurrency().toString() +  " | " +
                    DateParser.parseString(e.getDate()) + " | " +
                    e.getType().toString();
            expenseListString.add(stringExpense);
        }
        expenseListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        if(isLocal && dataSource != null)
            dataSource.open();
        readDB();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(isLocal && dataSource != null)
            dataSource.close();
        super.onPause();
    }
}
