package bagarrao.financialdroid.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import bagarrao.financialdroid.currency.Currency;
import bagarrao.financialdroid.currency.CurrencyConverter;
import bagarrao.financialdroid.expense.Expenditure;

/**
 * Class that handles all {@link FirebaseDatabase} data.
 * Retrieves, Inserts, Deletes and Inserts {@link Expenditure}
 * objects.
 *
 * @author Eduardo 2017-2018
 *
 * @since 0.1.8
 *
 * @see FirebaseUser
 * @see com.google.firebase.auth.FirebaseAuth
 * @see FirebaseDatabase
 * @see DatabaseReference
 *
 */
public class FirebaseManager{

    /**
     * Singleton instance of {@link FirebaseManager}.
     */
    private static final FirebaseManager INSTANCE = new FirebaseManager();

    /**
     * Node used to store recent {@link Expenditure} objects.
     * A {@link Expenditure} is recent when where made in the current month.
     */
    public static final String EXPENSE_NODE = "expense_node";

    /**
     * Node used to store older {@link Expenditure} objects.
     * A {@link Expenditure} is recent when where made in the current month.
     */
    public static final String ARCHIVE_NODE = "archive_node";

    /**
     * {@link FirebaseDatabase} singleton instance call.
     */
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    /**
     * {@link Vector} that stores all {@link Expenditure} objects.
     */
    private Vector<Expenditure> expenditureVector;

    /**
     * Main reference that stores the {@link Expenditure} objects.
     */
    private DatabaseReference databaseReference;

    /**
     * current {@link FirebaseUser}.
     */
    private FirebaseUser user;

    /**
     * {@link FirebaseManager} constructor.
     * {@link #user} starts at null value.
     */
    private FirebaseManager(){
        this.expenditureVector = new Vector<>();
        this.databaseReference = firebaseDatabase.getReference("ExpenseDatabase");
        this.user = null;
    }

    /**
     * Getter for the {@link FirebaseManager} {@link #INSTANCE}.
     * @return FirebaseManager
     */
    public static FirebaseManager getInstance() {
        return INSTANCE;
    }

    /**
     * Getter for the {@link FirebaseUser} uid.
     * @return String
     */
    public String getUid() {
        return (user != null ) ? user.getUid() : "";
    }

    /**
     * Sets the current {@link FirebaseUser},
     * and if the {@link #user} it's not null,
     * will call the {@link #listenDatabase()} method.
     *
     * @see #listenDatabase()
     *
     * @param user {@link FirebaseUser}
     */
    public void setUser(FirebaseUser user){
        if(user != null){
            this.user = user;
            listenDatabase();
        }
    }

    /**
     *  makes a listener that changes {@link #expenditureVector}
     *  in conforming to the changes to the {@link FirebaseDatabase}.
     *  calls before {@link #getAllExpenditures()} and assigns his
     *  value to the {@link #expenditureVector} variable.
     */
    public void listenDatabase(){
        this.expenditureVector = getAllExpenditures();
        databaseReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Expenditure expenditure = dataSnapshot.getValue(Expenditure.class);
                Currency currency = CurrencyConverter.getInstance().getCurrency();
                expenditure.setValue((float) currency.convert(expenditure.getValue(),Currency.DEFAULT_CURRENCY));
                if(expenditure.getUid().equals(user.getUid()))
                    expenditureVector.add(expenditure);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //TODO currency conversion
                //TODO: update expenditure
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Expenditure expenditure = dataSnapshot.getValue(Expenditure.class);
                Currency currency = CurrencyConverter.getInstance().getCurrency();
                expenditure.setValue((float) Currency.DEFAULT_CURRENCY.convert(expenditure.getValue(),currency));
                if(expenditure.getUid().equals(user.getUid()))
                    expenditureVector.remove(expenditure);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Inserts a new {@link Expenditure} object into the {@link FirebaseDatabase}.
     * {@link #expenditureVector} will be updated.
     *
     * @see #listenDatabase()
     *
     * @param expenditure {@link Expenditure}
     */
    public void insertExpenditure(Expenditure expenditure) {
        if (user != null) {
            Currency currency = CurrencyConverter.getInstance().getCurrency();
            expenditure.setValue((float)currency.convert(expenditure.getValue(),Currency.EUR));
            DatabaseReference newReference = databaseReference.child(expenditure.getNode()).push();
            newReference.setValue(expenditure);
        }
    }

    /**
     * Retrieves a {@link Vector<Expenditure>} with all {@link Expenditure}
     * objects that are stored in the {@link FirebaseDatabase}.
     *
     * @return Vector<Expenditure>
     */
    public Vector<Expenditure> getAllExpenditures(){
        Vector<Expenditure> vector = new Vector<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.child(EXPENSE_NODE).getChildren()) {
                    Expenditure expenditure = postSnapshot.getValue(Expenditure.class);
                    if(expenditure.getUid().equals(user.getUid())) {
                        Currency defaultCurrency = Currency.DEFAULT_CURRENCY;
                        Currency currency = CurrencyConverter.getInstance().getCurrency();
                        expenditure.setValue((float) defaultCurrency.convert(expenditure.getValue(), currency));
                        vector.add(expenditure);
                    }
                }
                for (DataSnapshot postSnapshot: snapshot.child(ARCHIVE_NODE).getChildren()) {
                    Expenditure expenditure = postSnapshot.getValue(Expenditure.class);
                    if(expenditure.getUid().equals(user.getUid())) {
                        Currency defaultCurrency = Currency.DEFAULT_CURRENCY;
                        Currency currency = CurrencyConverter.getInstance().getCurrency();
                        expenditure.setValue((float) defaultCurrency.convert(expenditure.getValue(), currency));
                        vector.add(expenditure);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
        return vector;
    }

    /**
     * Retrieves a {@link List<Expenditure>} with all recent {@link Expenditure} objects.
     *
     * @return List<Expenditure>
     */
    public List<Expenditure> getRecentExpenditures(){
        List<Expenditure> list = new LinkedList<>();
        for(Expenditure e : expenditureVector){
            if(e.getNode().equals(EXPENSE_NODE))
                list.add(e);
        }
        return list;
    }

    /**
     * Retrieves a {@link List<Expenditure>} with all older {@link Expenditure} objects.
     *
     * @return List<Expenditure>
     */
    public List<Expenditure> getArchiveExpenditures(){
        List<Expenditure> list = new LinkedList<>();
        for(Expenditure e : expenditureVector){
            if(e.getNode().equals(ARCHIVE_NODE))
                list.add(e);
        }
        return list;
    }

    /**
     *
     * Removes a {@link Expenditure} object from the {@link FirebaseDatabase}.
     * Before the remove, the {@link Expenditure} is converted to the default
     * {@link Currency}.
     *
     * @see #listenDatabase()
     *
     * @param expenditure {@link Expenditure}
     */
    public void removeExpenditure(Expenditure expenditure){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                DatabaseReference toRemove = null;
                Currency currency = CurrencyConverter.getInstance().getCurrency();
                expenditure.setValue((float) currency.convert(expenditure.getValue(), Currency.DEFAULT_CURRENCY));
                for (DataSnapshot postSnapshot: snapshot.child(expenditure.getNode()).getChildren()) {
                    Expenditure dbExpenditure = postSnapshot.getValue(Expenditure.class);
                    if(expenditure.getUid().equals(user.getUid()) && expenditure.equals(dbExpenditure)) {
                        toRemove = postSnapshot.getRef();
                    }
                }
                if(toRemove != null)
                    databaseReference.removeValue();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }
}