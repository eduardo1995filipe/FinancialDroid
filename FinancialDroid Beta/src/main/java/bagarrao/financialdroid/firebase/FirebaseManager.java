package bagarrao.financialdroid.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by eduar on 30/12/2017.
 */

public class FirebaseManager{

    private static final FirebaseManager INSTANCE = new FirebaseManager();

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    private FirebaseUser user;
    private boolean isConnected;

    private FirebaseManager(){
        this.isConnected = false;
        this.databaseReference = firebaseDatabase.getReference("Expenses");
        this.user = null;
    }

    public void setUser(FirebaseUser user){
        this.user = user;
    }

    public static FirebaseManager getInstance() {
        return INSTANCE;
    }
}
