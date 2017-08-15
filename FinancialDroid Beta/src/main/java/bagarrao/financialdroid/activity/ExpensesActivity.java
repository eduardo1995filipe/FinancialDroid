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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.backup.Backup;
import bagarrao.financialdroid.database.ExpenseDataSource;
import bagarrao.financialdroid.expense.Expense;
import bagarrao.financialdroid.expense.ExpenseOrder;
import bagarrao.financialdroid.expense.ExpenseType;
import bagarrao.financialdroid.utils.DateForCompare;
import bagarrao.financialdroid.utils.ShowBy;

/**
 * @author Eduardo Bagarrao
 */
public class ExpensesActivity extends AppCompatActivity {

    public static final ExpenseOrder DEFAULT_ORDER = ExpenseOrder.DATE_DESCENDING;
    public static final ShowBy DEFAULT_SHOW_BY = ShowBy.ALL;

    private ExpenseOrder currentOrder;
    private ShowBy currentShowBy;
    private ListView expenseListView;
    private Spinner orderBySpinner;
    private Spinner showByTypeSpinner;
    private List<Expense> expenseList;
    private ArrayList<String> expenseListString;
    private ExpenseDataSource dataSource;
    private ArrayAdapter<CharSequence> spinnerOrderByAdapter;
    private ArrayAdapter<CharSequence> spinnerShowByTypeAdapter;
    private ArrayAdapter<String> expenseListAdapter;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        init();
        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(context, position);
            }
        });
    }

    /**
     * initializes all the elements
     */
    public void init() {
        this.context = this;
        this.currentOrder = DEFAULT_ORDER;
        this.currentShowBy = DEFAULT_SHOW_BY;
        this.expenseListString = new ArrayList<>();
        this.dataSource = new ExpenseDataSource(this);
        this.dataSource.open();
        this.expenseListView = (ListView) findViewById(R.id.expensesListView);

        this.orderBySpinner = (Spinner) findViewById(R.id.expenseOrderBySpinner);
        this.spinnerOrderByAdapter = ArrayAdapter.createFromResource(this, R.array.order_kind,
                R.layout.support_simple_spinner_dropdown_item);
        spinnerOrderByAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        orderBySpinner.setAdapter(spinnerOrderByAdapter);

        this.showByTypeSpinner = (Spinner) findViewById(R.id.expenseShowByExpensesSpinner);
        this.spinnerShowByTypeAdapter = ArrayAdapter.createFromResource(this, R.array.show_by_type,
                R.layout.support_simple_spinner_dropdown_item);
        spinnerShowByTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        showByTypeSpinner.setAdapter(spinnerShowByTypeAdapter);

        this.expenseListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, expenseListString);
        expenseListView.setAdapter(expenseListAdapter);
        readDB();
        setListeners();
    }

    /**
     * Creates the necessary listeners to the activity
     */
    public void setListeners() {
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

        showByTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    currentShowBy = getShowBy(position);
                } catch (NullPointerException e) {
                    currentShowBy = DEFAULT_SHOW_BY;
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
                Log.e("ExpensesActivity", "expenseOrder returned NULL");
                throw new NullPointerException();
        }
        return expenseOrder;
    }

    public ShowBy getShowBy(int index) {
        ShowBy showBy = null;
        switch (index) {
            case 0:
                showBy = ShowBy.ALL;
                break;
            case 1:
                showBy = ShowBy.FEEDING;
                break;
            case 2:
                showBy = ShowBy.TRANSPORTS;
                break;
            case 3:
                showBy = ShowBy.CLOTHING;
                break;
            case 4:
                showBy = ShowBy.OTHERS;
                break;
            default:
                Log.e("ExpensesActivity", "expenseOrder returned NULL");
        }
        return showBy;
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

    /**
     * Removes a expense that is on the index given on the parameter
     * @param index variable to get the Expense that is pretended to remove
     */
    public void removeExpense(int index) {
        this.expenseList = dataSource.getAllExpenses();
        if (expenseList == null)
            expenseList = new ArrayList<>();
        else if ((expenseList.size() - 1) >= index) {
            Expense e = expenseList.get(index);
            dataSource.deleteExpense(e);
            expenseList.remove(index);
        }
        expenseListString.clear();
        for (Expense e : expenseList) {
            String stringExpense = e.getDescription() + " | " + e.getValue() + "€ | " +
                    DateForCompare.DATE_FORMATTED.format(e.getDate()) + " | " + e.getType().toString();
            expenseListString.add(stringExpense);
        }
        expenseListAdapter.notifyDataSetChanged();
    }

    /**
     * reads and converts the database to an ArrayList, and Updates the ListView View
     */
    public void readDB() {
        this.expenseList = dataSource.getAllExpenses();
        if (expenseList == null)
            expenseList = new ArrayList<>();
        expenseListString.clear();
        currentOrder.sortByOrder(expenseList);
        if(currentShowBy != ShowBy.ALL)
            expenseList = getExpensesByType(expenseList,ExpenseType.valueOf(currentShowBy.toString()));
        for (Expense e : expenseList) {
            String stringExpense = e.getDescription() + " | " + e.getValue() + "€ | " + DateForCompare.DATE_FORMATTED.format(e.getDate()) + " | " + e.getType().toString();
            expenseListString.add(stringExpense);
        }
        expenseListAdapter.notifyDataSetChanged();
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

    //ERRO --> A CORRIGIR DOIS SPINNERS

    public static List<Expense> getExpensesByType(List<Expense> list,ExpenseType type){
        LinkedList<Expense> expenseListFiltered = new LinkedList<>();
        for(Expense e : list){
            if(e.getType() == type)
                expenseListFiltered.add(e);
        }
        return expenseListFiltered;
    }

}
