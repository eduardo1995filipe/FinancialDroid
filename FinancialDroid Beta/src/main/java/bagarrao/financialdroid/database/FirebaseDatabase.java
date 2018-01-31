package bagarrao.financialdroid.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

import bagarrao.financialdroid.expense.Expenditure;


public class FirebaseDatabase implements Database<Expenditure>, ChildEventListener{

    private FirebaseUser user;
    private Vector<Expenditure> expenditureVector;
    private com.google.firebase.database.FirebaseDatabase database = com.google.firebase.database.FirebaseDatabase.getInstance();
    private DatabaseReference mainReference;

    public FirebaseDatabase(FirebaseUser user){
        this.user = user;
        this.expenditureVector = new Vector<>();
        this.mainReference = database.getReference("Expenditures");
        init();
    }

    public void init() {
        if(expenditureVector != null)
            expenditureVector.clear();
        else
            expenditureVector = new Vector<>();
        mainReference.addChildEventListener(this);
    }

    @Override
    public void insert(Expenditure expenditure) {
        DatabaseReference newReference = mainReference.push();
        newReference.setValue(expenditure);
    }

    @Override
    public void delete(Expenditure expenditure) {
        mainReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    DatabaseReference toRemove = null;
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Expenditure dbExpenditure = postSnapshot.getValue(Expenditure.class);
                        if (expenditure.getUid().equals(user.getUid()) && expenditure.equals(dbExpenditure)) {
                            toRemove = postSnapshot.getRef();
                        }
                    }
                    if (toRemove != null)
                        toRemove.removeValue();
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.e("The read failed: ", firebaseError.getMessage());
                }
            });
    }

    @Override
    public void update(Expenditure oldExpenditure, Expenditure newExpenditure) {
    }

    @Override
    public Vector<Expenditure> selectAll() {
        return expenditureVector;
    }

    @Override
    public void deleteAll() {
        for(Expenditure e : selectAll()){
            delete(e);
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Expenditure expenditure = dataSnapshot.getValue(Expenditure.class);
        if(expenditure.getUid().equals(user.getUid()))
            expenditureVector.add(expenditure);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Expenditure expenditure = dataSnapshot.getValue(Expenditure.class);
        if(expenditure.getUid().equals(user.getUid()))
            expenditureVector.remove(expenditure);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
