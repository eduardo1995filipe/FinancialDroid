package bagarrao.financialdroid.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.backup.Backup;
import bagarrao.financialdroid.database.ArchiveDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseOrder;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.DateForCompare;
import bagarrao.financialdroid.utils.Filter;

public class ArchiveActivity extends AppCompatActivity {

    public static final ExpenseOrder DEFAULT_ORDER = ExpenseOrder.DATE_DESCENDING;
    public static final ExpenseType DEFAULT_EXPENSE_TYPE = null;

    private ExpenseOrder currentOrder;
    private ExpenseType currentType;

    private Spinner orderSpinner;
    private Spinner typeSpinner;

    private ArrayAdapter<CharSequence> spinnerOrderAdapter;
    private ArrayAdapter<CharSequence> spinnerTypeAdapter;

    private ListView archiveListView;
    private List<Expense> archiveList;
    private ArrayList<String> archiveListString;
    private ArchiveDataSource dataSource;
    private ArrayAdapter<String> archiveListAdapter;

    private Context context;

    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        init();
        readDB();
        setListeners();
    }

    /**
     * initializes all the elements
     */
    public void init() {
        this.context = this;

        this.currentOrder = DEFAULT_ORDER;
        this.currentType = DEFAULT_EXPENSE_TYPE;

        this.archiveListString = new ArrayList<>();
        this.dataSource = new ArchiveDataSource(this);
        this.dataSource.open();
        this.archiveListView = (ListView) findViewById(R.id.archiveExpensesListView);

        this.resetButton = (Button) findViewById(R.id.resetArchiveButton);

        this.orderSpinner = (Spinner) findViewById(R.id.archiveOrderBySpinner);
        this.spinnerOrderAdapter = ArrayAdapter.createFromResource(this, R.array.order_kind,
                R.layout.support_simple_spinner_dropdown_item);
        spinnerOrderAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        orderSpinner.setAdapter(spinnerOrderAdapter);

        this.typeSpinner = (Spinner) findViewById(R.id.archiveShowByExpensesSpinner);
        this.spinnerTypeAdapter = ArrayAdapter.createFromResource(this, R.array.filter_expense_type,
                R.layout.support_simple_spinner_dropdown_item);
        spinnerTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinnerTypeAdapter);

        this.archiveListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, archiveListString);
        archiveListView.setAdapter(archiveListAdapter);
    }

    /**
     * Creates the necessary listeners to the activity
     */
    public void setListeners() {

        archiveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(context, position);
            }
        });

        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    currentType = Filter.getTypeByIndex(position);
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

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetDialog(context);
            }
        });
    }

    /**
     * Gets an order to sort the expenses
     *
     * @param index index of the ExpenseOrder selected on Spinner
     * @return correspondant Enum of OrderExpense index
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
                Log.e("ArchiveActivity", "expenseOrder returned NULL");
                throw new NullPointerException();
        }
        return expenseOrder;
    }

    /**
     * Show's a Dialog interface to user to confirm that he's sure to delete the clicked Expense
     *
     * @param context Activity context to give as parameter to AlerdDialog.Builder
     * @param index   index of the Expense clicked on the ListView
     */
    public void showDeleteDialog(final Context context, final int index) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this expense?");
        builder.setTitle("Delete Expense");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeExpense(index);
                new Backup().go();
                Toast.makeText(context, "Expense removed successfully!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    public void showResetDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to reset your archive?");
        builder.setTitle("Reset Archive");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataSource.deleteAllExpenses();
                readDB();
                Toast.makeText(context, "Archive was reset successfully!", Toast.LENGTH_SHORT).show();
                new Backup().go();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }


    /**
     * Removes a expense that is on the index given on the parameter
     *
     * @param index variable to get the Expense that is pretended to remove
     */
    public void removeExpense(int index) {
        this.archiveList = dataSource.getAllExpenses();
        if (archiveList == null)
            archiveList = new ArrayList<>();
        else if ((archiveList.size() - 1) >= index) {
            Expense e = archiveList.get(index);
            dataSource.deleteExpense(e);
            archiveList.remove(index);
        }
        archiveListString.clear();
        for (Expense e : archiveList) {
            String stringExpense = e.getDescription() + " | " + e.getValue() + "€ | " +
                    DateForCompare.DATE_FORMATTED.format(e.getDate()) + " | " + e.getType().toString();
            archiveListString.add(stringExpense);
        }
        archiveListAdapter.notifyDataSetChanged();
    }

    /**
     * reads and converts the database to an ArrayList, and Updates the ListView View
     */
    public void readDB() {
        this.archiveList = dataSource.getAllExpenses();
        if (archiveList == null)
            archiveList = new ArrayList<>();
        archiveListString.clear();
        currentOrder.sortByOrder(archiveList);
        archiveList = Filter.getExpensesByType(archiveList,currentType);
        for (Expense e : archiveList) {
            String stringExpense = e.getDescription() + " | " + e.getValue() + "€ | " + DateForCompare.DATE_FORMATTED.format(e.getDate()) + " | " + e.getType().toString();
            archiveListString.add(stringExpense);
        }
        archiveListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        readDB();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
