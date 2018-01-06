package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.firebase.FirebaseManager;
import bagarrao.financialdroid.migration.Migrator;

/**
 * Activity that is used whether to login in a account,
 * use local storage(no account, and if you delete the
 * app the user will lose all data) or if he wants to
 * register for a new account in {@link RegisterActivity}.
 *
 * @author Eduardo 2017-2018
 *
 * @since 0.1.8
 */
public class LoginActivity extends AppCompatActivity implements OnCompleteListener<AuthResult>,FirebaseAuth.AuthStateListener{

    /**
     * {@link #toolbar} title.
     */
    private static final String TOOLBAR_TITLE = "Sign in";

    /**
     * {@link #localUseTextView} text.
     */
    private static final String LOCAL_USE_TEXT = "No account(local use)";

    /**
     * User for authentication to a online account.
     * Not used in local storage mode.
     */
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    /**
     * Singleton call that handles {@link com.google.firebase.database.FirebaseDatabase} operation.
     * Not used in local storage mode.
     */
    private FirebaseManager manager = FirebaseManager.getInstance();

    /**
     * text view that contains {@link #LOCAL_USE_TEXT}. When clicked
     * it calls {@link #migrator} object to check if it's needed
     * {@link bagarrao.financialdroid.expense.Expenditure} migration.
     * Also if it's clicked the app runs on local storage mode.
     */
    private TextView localUseTextView;

    /**
     * {@link EditText} that contains the email of the
     * {@link com.google.firebase.auth.FirebaseUser}.
     */
    private EditText emailEditText;

    /**
     * {@link EditText} that contains the password of the
     * {@link com.google.firebase.auth.FirebaseUser}.
     */
    private EditText passwordEditText;

    /**
     * {@link Button} used to sign in the {@link com.google.firebase.auth.FirebaseUser}.
     */
    private Button signInButton;

    /**
     * {@link Button} that calls the {@link RegisterActivity} and
     * kills this activity({@link LoginActivity}).
     */
    private Button registerButton;

    /**
     * {@link Toolbar} of the {@link LoginActivity}.
     */
    private Toolbar toolbar;

    /**
     * {@link Intent} used for login into {@link MainActivity},
     * whether is in local storage mode or in firebase mode.
     */
    private Intent loginIntent;

    /**
     * {@link Intent} used for calling the {@link RegisterActivity},
     * to create a new account.
     */
    private Intent registerIntent;

    /**
     * {@link SpannableString} that contains the content of the
     * {@link #localUseTextView}. Its used with the intention to have
     * underlined text.
     */
    private SpannableString content;

    /**
     * Used to migrate {@link bagarrao.financialdroid.expense.Expenditure}
     * objects, used only in local storage mode.
     *
     */
    private Migrator migrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setup();
        setSupportActionBar(toolbar);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            manager.setUser(auth.getCurrentUser());
            startActivity(loginIntent);
            finish();
        }
        else {
            Toast.makeText(this, "Problems with sign in. Try again with other credentials", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(auth.getCurrentUser() != null){
            manager.setUser(auth.getCurrentUser());
            startActivity(loginIntent);
            finish();
        }
    }

    /**
     * Used for initialize all the objects that {@link LoginActivity} contains.
     */
    public void init() {
        this.content = new SpannableString(LOCAL_USE_TEXT);
        this.migrator = new Migrator(this);
        this.registerIntent = new Intent(this,RegisterActivity.class);
        this.loginIntent = new Intent(this, MainActivity.class);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.localUseTextView = (TextView) findViewById(R.id.localUseTextView);
        this.emailEditText = (EditText) findViewById(R.id.emailEditText);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.signInButton = (Button) findViewById(R.id.signInButton);
        this.registerButton = (Button) findViewById(R.id.registerButton);
    }

    /**
     * Used to setup all the objects that {@link LoginActivity} contains.
     * Sets the {@link #toolbar} title, the {@link #localUseTextView}
     * underlined text and inits the {@link android.net.sip.SipSession.Listener}
     * objects. Checks also if the {@link bagarrao.financialdroid.expense.Expenditure}
     * need to be migrated.
     *
     * @see Migrator
     * @see com.google.firebase.auth.FirebaseAuth.AuthStateListener
     */
    public void setup(){
        toolbar.setTitle(TOOLBAR_TITLE);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        localUseTextView.setText(content);

        auth.addAuthStateListener(this);
        localUseTextView.setOnClickListener(l -> {
            if(migrator.needsMigration(this))
                migrator.run();
            startActivity(loginIntent);
        });
        signInButton.setOnClickListener(l -> attempLogin());
        registerButton.setOnClickListener(l -> {
            startActivity(registerIntent);
            finish();
        });
    }

    /**
     * Attemps a login in order to use {@link com.google.firebase.database.FirebaseDatabase}
     * services. Validates both password and email used in {@link #emailEditText}
     * and {@link #passwordEditText}, and after all conditions validated
     * proceeds to the connection.
     */
    public void attempLogin(){
            if (!isValidEmail())
                Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show();
            else if (!isValidPassword())
                Toast.makeText(this, "Invalid password!", Toast.LENGTH_SHORT).show();
            else if (TextUtils.isEmpty(emailEditText.getText().toString()))
                Toast.makeText(this, "email field is empty!", Toast.LENGTH_SHORT).show();
            else if (TextUtils.isEmpty(passwordEditText.getText().toString()))
                Toast.makeText(this, "password field is empty", Toast.LENGTH_SHORT).show();
            else {
                auth.signInWithEmailAndPassword(emailEditText.getText().toString(),passwordEditText.getText().toString()).
                        addOnCompleteListener(this);
            }
    }

    /**
     * Checks if the email on {@link #emailEditText} is valid.
     * A valid email should contain a '@' char, at least for now.
     * 
     * @return boolean
     */
    private boolean isValidEmail() {
        return (emailEditText.getText().toString()).contains("@");
    }

    /**
     * Checks if the email on {@link #passwordEditText} is valid.
     * A valid password should have a length bigger than 8,
     * at least for now.
     *
     * @return boolean
     */
    private boolean isValidPassword() {
        return (passwordEditText.getText().toString()).length() >= 8;
    }
}
