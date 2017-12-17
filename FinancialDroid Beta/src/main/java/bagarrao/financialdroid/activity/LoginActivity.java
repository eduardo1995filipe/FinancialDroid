package bagarrao.financialdroid.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import bagarrao.financialdroid.R;
import bagarrao.financialdroid.activity.MainActivity;
import bagarrao.financialdroid.backup.Backup;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button registerButton;
    private Intent intent;
    private Intent registerIntent;

    private String email;
    private String password;


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

        registerButton.setOnClickListener(l -> startActivity(registerIntent));
    }


    public void init() {
        this.emailEditText = (EditText) findViewById(R.id.emailEditText);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.signInButton = (Button) findViewById(R.id.signInButton);
        this.intent = new Intent(this, MainActivity.class);
        this.intent = new Intent(this,RegisterActivity.class);
    }

    private boolean isValidEmail() {
        return email.contains("@");
    }

    private boolean isValidPassword() {
        return password.length() >= 8;
    }

    private void attemptLogin() {
        if (!isValidEmail() || !isValidPassword()) {
            Toast.makeText(this, "Invalid login, please try again!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Empty email!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Empty password!", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(this, "Problems with Sign In", Toast.LENGTH_SHORT).show();
            });
        }
    }

}
