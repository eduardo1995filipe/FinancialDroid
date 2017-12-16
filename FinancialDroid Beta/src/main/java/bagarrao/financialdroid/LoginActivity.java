package bagarrao.financialdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import bagarrao.financialdroid.activity.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Intent intent;

    private String email;
    private String password;

    private static String ROOT = "ROOT;ROOT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign in");
        setSupportActionBar(toolbar);
        init();

        auth.addAuthStateListener(firebaseAuth -> {
            if (auth.getCurrentUser() != null) {
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(l -> {
            this.email = emailEditText.getText().toString();
            this.password = passwordEditText.getText().toString();
            attemptLogin();
        });
    }


    public void init() {
        this.emailEditText = (EditText) findViewById(R.id.emailEditText);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.signInButton = (Button) findViewById(R.id.signInButton);
        this.intent = new Intent(this, MainActivity.class);
    }

    private boolean isValidEmail() {
//        return ROOT.split(";")[0].equals(emailEditText.getText().toString().toUpperCase());
        return true;
    }

    private boolean isValidPassword() {
//        return ROOT.split(";")[1].equals(passwordEditText.getText().toString().toUpperCase());
        return true;
    }

    private void attemptLogin() {
        if (!isValidEmail() || !isValidPassword()) {
            Log.d("LoginActivity", "Invalid login, please try again!");
        } else if (TextUtils.isEmpty(email)) {
            Log.d("LoginActivity", "Empty email!!!");

        } else if (TextUtils.isEmpty(password)) {
            Log.d("LoginActivity", "Empty password");
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(intent);
                    finish();
                } else
                    Log.d("LoginActivity", "Problems with Sign In");
            });

        }
    }

}
