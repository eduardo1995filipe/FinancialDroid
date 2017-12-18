package bagarrao.financialdroid;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author Eduardo
 *
 * Class that manages the firebase login and the database queries
 */
public class FirebaseHelper {

    private static final FirebaseHelper INSTANCE = new FirebaseHelper();

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseUser currentUser;

    private String email;
    private String password;
    private boolean isConnected;

    private FirebaseHelper(){
        this.email = "";
        this.password = "";
        this.currentUser = auth.getCurrentUser();
        this.isConnected = auth.getCurrentUser() != null;
    }

    public static FirebaseHelper getInstance() {
        return (INSTANCE != null)? INSTANCE : new FirebaseHelper();
    }

    private void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Throwable connect(){
        Throwable throwable = null;
        //TODO: handle Firebase Connect
        return throwable;
    }

    public Throwable disconnect(){
        Throwable throwable = null;
        //TODO: handle Firebase Disconnect
        return throwable;
    }

    public Throwable register(){
        Throwable throwable = null;
        //TODO: handle Firebase Register
            return throwable;
    }
}
