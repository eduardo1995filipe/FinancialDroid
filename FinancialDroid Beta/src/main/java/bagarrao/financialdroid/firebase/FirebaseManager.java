package bagarrao.financialdroid.firebase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

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
     * @param expenditure
     */
    public void insertExpensediture(Expenditure expenditure) {
        if (user != null) {
            Currency currency = CurrencyConverter.getInstance().getCurrency();
            expenditure.setValue((float)currency.convert(expenditure.getValue(),Currency.EUR));
            Date date = new Date();
            if ((DateParser.getMonth(expenditure.getDate()) < DateParser.getMonth(date) &&
                    DateParser.getYear(expenditure.getDate()) < DateParser.getYear(date)) ||
                    (DateParser.getYear(expenditure.getDate()) < DateParser.getYear(date)))
                databaseReference.child(ARCHIVE_NODE).child(new String(user.getEmail()).replace('.','_')).
                        child(expenditure.getID()).setValue(expenditure);
            else
                databaseReference.child(EXPENSE_NODE).child(new String(user.getEmail()).replace('.','_')).
                        child(expenditure.getID()).setValue(expenditure);
        }
    }

    public List<Expenditure> getAllExpenditures(){
//        TODO:
        return null;
    }
}
