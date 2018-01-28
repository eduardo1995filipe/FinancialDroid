package bagarrao.financialdroid.database;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Vector;
//
//import bagarrao.financialdroid.currency.Currency;
//import bagarrao.financialdroid.expense.Expenditure;
//
///**
// * Class that handles all {@link FirebaseDatabase} data.
// * Retrieves, Inserts, Deletes and Inserts {@link Expenditure}
// * objects.
// *
// * @author Eduardo 2017-2018
// *
// * @since 0.1.8
// *
// * @see FirebaseUser
// * @see com.google.firebase.auth.FirebaseAuth
// * @see FirebaseDatabase
// * @see DatabaseReference
// *
// */
public class DatabaseManager {
//
//    /**
//     * Singleton instance of {@link DatabaseManager}.
//     */
//    private static final DatabaseManager INSTANCE = new DatabaseManager();
//
//    /**
//     * Node used to store recent {@link Expenditure} objects.
//     * A {@link Expenditure} is recent when where made in the current month.
//     */
    public static final String EXPENSE_NODE = "expense_node";
//
//    /**
//     * Node used to store older {@link Expenditure} objects.
//     * A {@link Expenditure} is recent when where made in the current month.
//     */
    public static final String ARCHIVE_NODE = "archive_node";
}
//
//    /**
//     * {@link CurrencyConverter} singleton instance call.
//     */
////    private CurrencyConverter currencyConverter = CurrencyConverter.getInstance();
//
//    /**
//     * {@link FirebaseDatabase} singleton instance call.
//     */
//    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//
////    /**
////     * {@link Context} to be used to access {@link #currencyConverter}
////     */
//    private Context context;
//
//    /**
//     * {@link Vector} that stores all {@link Expenditure} objects.
//     */
//    private Vector<Expenditure> expenditureVector;
//
//    /**
//     * Main reference that stores the {@link Expenditure} objects.
//     */
//    private DatabaseReference databaseReference;
//
//    /**
//     * current {@link FirebaseUser}.
//     */
//    private FirebaseUser user;
//
//    /**
//     * {@link DataSource} used to store {@link Expenditure}
//     * objects that have less than one month.
//     */
//    private DataSource expenseDataSource;
//
//    /**
//     * {@link DataSource} used to store {@link Expenditure}
//     * objects that have more than one month.
//     */
//    private DataSource archiveDataSource;
//
//    /**
//     * Attribute that determines if the user is using
//     * {@link DataSource} or {@link FirebaseDatabase}.
//     */
//    private boolean isLocal;
//
//    /**
//     * {@link DatabaseManager} constructor.
//     * {@link #user} starts at null value.
//     */
//    private DatabaseManager(){
//        this.isLocal = user == null;
//        this.expenseDataSource = new DataSource(DataSource.CURRENT,context);
//        this.archiveDataSource = new DataSource(DataSource.ARCHIVE,context);
//        this.expenditureVector = new Vector<>();
//        this.databaseReference = firebaseDatabase.getReference("ExpenseDatabase");
//        this.context = null;
//    }
//
////    /**
////     * Getter for the {@link DatabaseManager} {@link #INSTANCE}.
////     * Sets the {@link #context} to be used for {@link #currencyConverter}.
////     *
////     * @return DatabaseManager
////     */
//    public static DatabaseManager getInstance(Context context) {
//        INSTANCE.setContext(context);
//        return (INSTANCE != null) ? INSTANCE : new DatabaseManager();
//    }
//
//    /**
//     * Getter for the {@link DatabaseManager} {@link #INSTANCE}.
//     *
//     * @return DatabaseManager
//     */
//    public static DatabaseManager getInstance() {
//        return (INSTANCE != null) ? INSTANCE : new DatabaseManager();
//    }
//
//    /**
//     * Getter for the {@link FirebaseUser} uid.
//     * @return String
//     */
//    public String getUid() {
//        return (user != null ) ? user.getUid() : "";
//    }
//
////    /**
////     * Getter for the {@link Currency} that is being used.
////     *
////     * @return Currency
////     */
////    public Currency getCurrency(){
////        return currencyConverter.getCurrency();
////    }
//
//    /**
//     * Getter for the {@link #isLocal}. Checks if the database used is the
//     * {@link DataSource} ot the {@link #firebaseDatabase}.
//     *
//     * @see DataSource
//     * @see FirebaseDatabase
//     *
//     * @return boolean
//     */
//    public boolean isLocal(){
//        return isLocal;
//    }
//
//    /**
//     * Sets the current {@link FirebaseUser},
//     * and if the {@link #user} it's not null,
//     * will call the {@link #listenDatabase()} method.
//     *
//     * @see #listenDatabase()
//     *
//     * @param user {@link FirebaseUser}
//     */
//    public void setUser(FirebaseUser user){
//        if(!(isLocal = (user == null))) {
//            this.user = user;
//            listenDatabase();
//        }
//    }
//
////    /**
////     * Sets the current {@link Context},
////     * and if the {@link #context} it's not null,
////     * will call the {@link #setContext(Context)} method,
////     * to set the {@link Context} of {@link #currencyConverter}.
////     *
////     * @see CurrencyConverter
////     *
////     * @param context {@link Context}
////     */
//    public void setContext(Context context){
//        if(context != null){
//            this.context = context;
////            currencyConverter.setContext(context);
//        }
//    }
//
//    /**
//     *  makes a listener that changes {@link #expenditureVector}
//     *  in conforming to the changes to the {@link FirebaseDatabase}.
//     *  calls before {@link #getAllExpenditures()} and assigns his
//     *  value to the {@link #expenditureVector} variable.
//     */
//    public void listenDatabase(){
//        this.expenditureVector = getAllExpenditures();
//        databaseReference.addChildEventListener(new ChildEventListener() {
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Expenditure expenditure = dataSnapshot.getValue(Expenditure.class);
////                Currency currency = currencyConverter.getCurrency();
////                expenditure.setValue( currency.convert(expenditure.getValue(),Currency.DEFAULT_CURRENCY));
//                if(expenditure.getUid().equals(user.getUid()))
//                    expenditureVector.add(expenditure);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                //TODO currency conversion
//                //TODO: update expenditure
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Expenditure expenditure = dataSnapshot.getValue(Expenditure.class);
////                Currency currency = currencyConverter.getCurrency();
////                expenditure.setValue( Currency.DEFAULT_CURRENCY.convert(expenditure.getValue(),currency));
//                if(expenditure.getUid().equals(user.getUid()))
//                    expenditureVector.remove(expenditure);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }
//
//    /**
//     * Inserts a new {@link Expenditure} object into the {@link FirebaseDatabase}.
//     * {@link #expenditureVector} will be updated.
//     *
//     * @see #listenDatabase()
//     *
//     * @param expenditure {@link Expenditure}
//     */
//    public void insertExpenditure(Expenditure expenditure) {
//        if (!isLocal) {
////            Currency currency = currencyConverter.getCurrency();
////            expenditure.setValue(currency.convert(expenditure.getValue(),Currency.EUR));
//            DatabaseReference newReference = databaseReference.push();
//            newReference.setValue(expenditure);
//        }else{
//            if(!expenseDataSource.isOpen())
//                expenseDataSource.open();
//            if(!archiveDataSource.isOpen())
//                archiveDataSource.open();
////            ExpenseDistributor.addNewExpense(expenditure,context,expenseDataSource,archiveDataSource);
//            expenseDataSource.close();
//            archiveDataSource.close();
//        }
//    }
//
//    /**
//     * Retrieves a {@link Vector<Expenditure>} with all {@link Expenditure}
//     * objects that are stored in the {@link FirebaseDatabase}.
//     *
//     * @return Vector<Expenditure>
//     */
//    public Vector<Expenditure> getAllExpenditures(){
//        Vector<Expenditure> vector = new Vector<>();
//        if(!isLocal) {
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        Expenditure expenditure = postSnapshot.getValue(Expenditure.class);
//                        if (expenditure.getUid().equals(user.getUid())) {
////                            Currency defaultCurrency = Currency.DEFAULT_CURRENCY;
////                            Currency currency = currencyConverter.getCurrency();
////                            expenditure.setValue(defaultCurrency.convert(expenditure.getValue(), currency));
//                            vector.add(expenditure);
//                        }
//                    }
//                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        Expenditure expenditure = postSnapshot.getValue(Expenditure.class);
//                        if (expenditure.getUid().equals(user.getUid())) {
////                            Currency defaultCurrency = Currency.DEFAULT_CURRENCY;
////                            Currency currency = currencyConverter.getCurrency();
////                            expenditure.setValue(defaultCurrency.convert(expenditure.getValue(), currency));
//                            vector.add(expenditure);
//                        }
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError firebaseError) {
//                    Log.e("The read failed: ", firebaseError.getMessage());
//                }
//            });
//            return vector;
//        }else {
//            if(!expenseDataSource.isOpen())
//                expenseDataSource.open();
//            if(!archiveDataSource.isOpen())
//                archiveDataSource.open();
//            vector.addAll(archiveDataSource.getAllExpenditures());
//            vector.addAll(expenseDataSource.getAllExpenditures());
//            expenseDataSource.close();
//            archiveDataSource.close();
//        }
//        return vector;
//    }
//
//    /**
//     * Retrieves a {@link List<Expenditure>} with all recent {@link Expenditure} objects.
//     *
//     * @return List<Expenditure>
//     */
//    public List<Expenditure> getRecentExpenditures(){
//        List<Expenditure> list = new LinkedList<>();
//        if(!isLocal){
//            for(Expenditure e : expenditureVector){
//                if(e.getNode().equals(EXPENSE_NODE))
//                    list.add(e);
//            }
//        } else {
//            if(!expenseDataSource.isOpen())
//                expenseDataSource.open();
//            list = expenseDataSource.getAllExpenditures();
//            expenseDataSource.close();
//        }
//        return list;
//    }
//
//    /**
//     * Retrieves a {@link List<Expenditure>} with all older {@link Expenditure} objects.
//     *
//     * @return List<Expenditure>
//     */
//    public List<Expenditure> getArchiveExpenditures(){
//        List<Expenditure> list = new LinkedList<>();
//        if(!isLocal){
//            for(Expenditure e : expenditureVector){
//                if(e.getNode().equals(ARCHIVE_NODE))
//                    list.add(e);
//            }
//        } else {
//            if(!archiveDataSource.isOpen())
//                archiveDataSource.open();
//            list = archiveDataSource.getAllExpenditures();
//            archiveDataSource.close();
//        }
//        return list;
//    }
//
//    /**
//     *
//     * Removes a {@link Expenditure} object from the {@link FirebaseDatabase}.
//     * Before the remove, the {@link Expenditure} is converted to the default
//     * {@link Currency}.
//     *
//     * @see #listenDatabase()
//     *
//     * @param expenditure {@link Expenditure}
//     */
//    public void removeExpenditure(Expenditure expenditure){
//        if(!isLocal) {
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    DatabaseReference toRemove = null;
////                    Currency currency = currencyConverter.getCurrency();
////                    expenditure.setValue(currency.convert(expenditure.getValue(), Currency.DEFAULT_CURRENCY));
//                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        Expenditure dbExpenditure = postSnapshot.getValue(Expenditure.class);
//                        if (expenditure.getUid().equals(user.getUid()) && expenditure.equals(dbExpenditure)) {
//                            toRemove = postSnapshot.getRef();
//                        }
//                    }
//                    if (toRemove != null)
//                        databaseReference.removeValue();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError firebaseError) {
//                    Log.e("The read failed: ", firebaseError.getMessage());
//                }
//            });
//        } else{
//            if(!expenseDataSource.isOpen())
//                expenseDataSource.open();
//            if(!archiveDataSource.isOpen())
//                archiveDataSource.open();
//            List<Expenditure> oldExpenditureList = archiveDataSource.getAllExpenditures();
//            List<Expenditure> recentExpenditureList = expenseDataSource.getAllExpenditures();
//
//            for(Expenditure e : oldExpenditureList){
//                if(e.equals(expenditure)){
//                    archiveDataSource.deleteExpenditure(e);
//                    break;
//                }
//            }
//
//            for(Expenditure e : recentExpenditureList){
//                if(e.equals(expenditure)){
//                    archiveDataSource.deleteExpenditure(e);
//                    break;
//                }
//            }
//
//            expenseDataSource.close();
//            archiveDataSource.close();
//        }
//    }
//
//    /**
//     * Deletes all {@link Expenditure} objects that contains {@link #ARCHIVE_NODE},
//     * if {@link #isLocal} has false value, or deletes all {@link Expenditure} objects
//     * in {@link #archiveDataSource}.
//     */
//    public void resetArchiveExpenditures() {
//        if(isLocal){
//            if (!archiveDataSource.isOpen())
//                archiveDataSource.open();
//                archiveDataSource.deleteAllExpenditures();
//                archiveDataSource.close();
//        }else {
//            for(Expenditure e : expenditureVector){
//                if(e.getNode().equals(ARCHIVE_NODE))
//                    removeExpenditure(e);
//            }
//        }
//    }
//
//
////    /**
////     * Changes the currency in {@link #currencyConverter} singleton, and
////     * updates all {@link Expenditure} object values in {@link DataSource},
////     * if the {@link #isLocal} attribute has true value, otherwise all
////     * {@link Expenditure} objects in {@link #expenditureVector} will be
////     * changed their value.
////     *
////     * @param newCurrency {@link Currency}
////     */
////    public void changeCurrency(Currency newCurrency){
////        Currency oldCurrency = currencyConverter.getCurrency();
////        currencyConverter.setCurrency(newCurrency);
////        if(!isLocal){
////            Currency currency = currencyConverter.getCurrency();
////            for (Expenditure e : expenditureVector){
////                e.setValue(oldCurrency.convert(e.getValue(),currency));
////            }
////        } else{
////            currencyConverter.updateLocalExpenditures(newCurrency, oldCurrency);
////        }
////    }
//}