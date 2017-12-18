package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import bagarrao.financialdroid.R;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Button registerButton;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private String email;
    private String password;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Register new Account");

        init();

        registerButton.setOnClickListener(l -> {
            this.email = usernameEditText.getText().toString();
            this.password = passwordEditText.getText().toString();
            attempRegister();
        });
    }

    public void init(){
        this.intent = new Intent(this, LoginActivity.class);
        this.registerButton = (Button) findViewById(R.id.registerRegisterButton);
        this.usernameEditText = (EditText) findViewById(R.id.registerEmailEditText);
        this.passwordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
        this.email = "";
        this.password = "";
    }

    private boolean isValidEmail() {
        return email.contains("@");
    }

    private boolean isValidPassword() {
        return password.length() >= 8;
    }

    private void attempRegister() {
        if (!isValidEmail() || !isValidPassword()) {
            Toast.makeText(this, "Invalid login, please try again!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Empty email!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Empty password!", Toast.LENGTH_SHORT).show();
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //TODO: set new user on app
                    finish();
                    startActivity(intent);
                } else
                    Toast.makeText(this, "Problems with Sign In", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
