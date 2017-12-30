package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.firebase.FirebaseManager;

public class LoginActivity extends AppCompatActivity implements OnCompleteListener<AuthResult>,FirebaseAuth.AuthStateListener{

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseManager manager = FirebaseManager.getInstance();

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button registerButton;
    private Intent intent;
    private Intent registerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign in");
        setSupportActionBar(toolbar);
        init();

        auth.addAuthStateListener(this);
        signInButton.setOnClickListener(l -> attempLogin());
        registerButton.setOnClickListener(l -> {
            startActivity(registerIntent);
            finish();
        });
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            manager.setUser(auth.getCurrentUser());
            startActivity(intent);
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
            startActivity(intent);
            finish();
        }
    }

    public void init() {
        this.emailEditText = (EditText) findViewById(R.id.emailEditText);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.signInButton = (Button) findViewById(R.id.signInButton);
        this.registerButton = (Button) findViewById(R.id.registerButton);
        this.registerIntent = new Intent(this,RegisterActivity.class);
        this.intent = new Intent(this, MainActivity.class);
    }

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

    private boolean isValidEmail() {
        return (emailEditText.getText().toString()).contains("@");
    }

    private boolean isValidPassword() {
        return (passwordEditText.getText().toString()).length() >= 8;
    }
}
