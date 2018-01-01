package bagarrao.financialdroid.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.currency.Currency;
import bagarrao.financialdroid.currency.CurrencyConverter;
import bagarrao.financialdroid.expense.Expenditure;
import bagarrao.financialdroid.expense.ExpenseType;
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

    /**
     *
     */
    private FirebaseManager(){
        this.databaseReference = firebaseDatabase.getReference("ExpenseDatabase");
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
                    (DateParser.getYear(expenditure.getDate()) < DateParser.getYear(date))){
                DatabaseReference newReference = databaseReference.child(ARCHIVE_NODE).push();
                newReference.setValue(expenditure);
            }
            else{
                DatabaseReference newReference = databaseReference.child(EXPENSE_NODE).push();
                newReference.setValue(expenditure);
            }



        }
    }

    public List<Expenditure> getAllExpenditures(String parentNode){
        List<Expenditure> list = new LinkedList<>();
        if(parentNode.equals(EXPENSE_NODE) ||
                parentNode.equals(ARCHIVE_NODE)){

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        list.add(postSnapshot.getValue(Expenditure.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.e("The read failed: " ,firebaseError.getMessage());
                }
            });
        }else
            Log.d("FirebaseManager","Inexistent node.");
        return list;
    }
}
