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

import java.util.LinkedList;
import java.util.List;

import bagarrao.financialdroid.currency.Currency;
import bagarrao.financialdroid.currency.CurrencyConverter;
import bagarrao.financialdroid.database.DataManager;
import bagarrao.financialdroid.R;
import bagarrao.financialdroid.database.Migrator;
import bagarrao.financialdroid.expense.Expenditure;
import butterknife.BindView;
import butterknife.ButterKnife;

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

    private CurrencyConverter cm = CurrencyConverter.getInstance();
    private DataManager dataManager = DataManager.getInstance();

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
     * text view that contains {@link #LOCAL_USE_TEXT}. When clicked
//     * it calls migrator object to check if it's needed
     * {@link bagarrao.financialdroid.expense.Expenditure} migration.
     * Also if it's clicked the app runs on local storage mode.
     */
    @BindView(R.id.localUseTextView)
    TextView localUseTextView;

    /**
     * {@link EditText} that contains the email of the
     * {@link com.google.firebase.auth.FirebaseUser}.
     */
    @BindView(R.id.emailEditText)
    EditText emailEditText;

    /**
     * {@link EditText} that contains the password of the
     * {@link com.google.firebase.auth.FirebaseUser}.
     */
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    /**
     * {@link Button} used to sign in the {@link com.google.firebase.auth.FirebaseUser}.
     */
    @BindView(R.id.signInButton)
    Button signInButton;

    /**
     * {@link Button} that calls the {@link RegisterActivity} and
     * kills this activity({@link LoginActivity}).
     */
    @BindView(R.id.registerButton)
    Button registerButton;



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
     */
    private Migrator migrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        setup();
//        setSupportActionBar(toolbar);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            dataManager.init(auth.getCurrentUser(),getApplicationContext());
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
            dataManager.init(auth.getCurrentUser(), getApplicationContext());
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
    }


    public void setup(){
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        localUseTextView.setText(content);

        auth.addAuthStateListener(this);
        localUseTextView.setOnClickListener(l -> {
            if(migrator.needsMigration(this))
                migrator.run();
            dataManager.init(null, getApplicationContext());

            //TODO: nulify currency here!!!
            cm.init(getApplicationContext());
            for(Expenditure e : dataManager.selectAll()){
                e.setValue(cm.getCurrency().convertToEuro(e.getValue()));
            }
            cm.setCurrency(Currency.EUR);

            startActivity(loginIntent);
            finish();
        });
        signInButton.setOnClickListener(l -> attempLogin());
        registerButton.setOnClickListener(l ->
            startActivity(registerIntent));
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
