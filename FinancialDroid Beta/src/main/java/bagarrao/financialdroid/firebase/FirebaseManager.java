package bagarrao.financialdroid.firebase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import bagarrao.financialdroid.currency.Currency;
import bagarrao.financialdroid.currency.CurrencyConverter;
import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.utils.DateParser;

/**
 * @author Eduardo
 */
public class FirebaseManager{

    private static final FirebaseManager INSTANCE = new FirebaseManager();

    private static final String EXPENSE_NODE = "expense_node";
    private static final String ARCHIVE_NODE = "archive_node";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    private FirebaseUser user;
    private boolean isConnected;

    /**
     *
     */
    private FirebaseManager(){
        this.isConnected = false;
        this.databaseReference = firebaseDatabase.getReference("Expenses");
        this.user = null;
    }

    /**
     *
     * @param user
     */
    public void setUser(FirebaseUser user){
        this.user = user;
    }

    /**
     *
     * @return
     */
    public static FirebaseManager getInstance() {
        return INSTANCE;
    }

    /**
     *
     * @param expense
     */
    public void insertExpense(Expenditure expense) {
        if (user != null) {
            Currency currency = CurrencyConverter.getInstance().getCurrency();
            expense.setValue((float)currency.convert(expense.getValue(),Currency.EUR));
            Date date = new Date();
            if ((DateParser.getMonth(expense.getDate()) < DateParser.getMonth(date) &&
                    DateParser.getYear(expense.getDate()) < DateParser.getYear(date)) ||
                    (DateParser.getYear(expense.getDate()) < DateParser.getYear(date)))
                databaseReference.child(ARCHIVE_NODE).child(expense.getID()).setValue(expense);
            else
                databaseReference.child(EXPENSE_NODE).child(expense.getID()).setValue(expense);
        }
    }


}
