package bagarrao.financialdroid.database;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import bagarrao.financialdroid.expense.Expenditure;

public class DataManager{

    private static final DataManager INSTANCE = new DataManager();

    private FirebaseUser user;
    private Database<Expenditure> database;
    private boolean isLocal;
    private Context applicationContext;

    public static synchronized DataManager getInstance() {
        return (INSTANCE != null) ? INSTANCE : new DataManager();
    }

    private DataManager() {
        this.user = null;
        this.isLocal = true;
    }

    public void init(FirebaseUser user,Context context){
        this.applicationContext = context;
        this.isLocal = (this.user = user) == null;
        this.database = isLocal ? new SQLiteDatabase(applicationContext) : new FirebaseDatabase(user);
    }

    public FirebaseUser getUser() {
        return this.user;
    }

    public boolean isLocal() {
        return this.isLocal;
    }

    public void insert(Expenditure expenditure){
        Log.d("DataManager","Is database null? " + (database == null));
        database.insert(expenditure);
    }

    public void delete(Expenditure expenditure){
        database.delete(expenditure);
    }

    public Vector<Expenditure> selectAll(){
        return database.selectAll();
    }

    public List<Expenditure> selectRecent(){
        Vector<Expenditure> expenditureVector = database.selectAll();
        List<Expenditure> recentExpenditureList = new LinkedList<>();
        for(Expenditure e : expenditureVector){
            if(!e.isOld()){
                Log.d("DataManager", "It's a recent Expenditure!!!");
                recentExpenditureList.add(e);
            }
        }
        return recentExpenditureList;
    }

    public List<Expenditure> selectAllOlder(){
        Vector<Expenditure> expenditureVector = database.selectAll();
        List<Expenditure> recentExpenditureList = new LinkedList<>();
        for(Expenditure e : expenditureVector){
            if(e.isOld())
                recentExpenditureList.add(e);
        }
        return recentExpenditureList;
    }

    public void deleteAllOlder(){
        for(Expenditure e : selectAllOlder()){
            delete(e);
        }
    }

    public void deleteAllRecent(){
        for(Expenditure e : selectRecent()){
            delete(e);
        }
    }

    public Context getApplicationContext() {
        return applicationContext;
    }
}
