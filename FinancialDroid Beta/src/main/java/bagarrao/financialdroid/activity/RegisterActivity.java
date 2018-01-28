package bagarrao.financialdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import bagarrao.financialdroid.database.DataManager;
import bagarrao.financialdroid.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<AuthResult>{

    private DataManager dataManager = DataManager.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Button registerButton;

    private EditText emailEditText;
    private EditText passwordEditText;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Register new Account");
        init();
        registerButton.setOnClickListener(this);
    }

    public void init(){
        this.intent = new Intent(this, MainActivity.class);
        this.registerButton = (Button) findViewById(R.id.registerRegisterButton);
        this.emailEditText = (EditText) findViewById(R.id.registerEmailEditText);
        this.passwordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
    }

    @Override
    public void onClick(View v) {
        attempRegister();
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            dataManager.init(auth.getCurrentUser(), getApplicationContext());
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "Problems with sign in. Try again with other credentials", Toast.LENGTH_SHORT).show();
        }
    }

    public void attempRegister(){
        if (!isValidEmail())
            Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show();
        else if (!isValidPassword())
            Toast.makeText(this, "Invalid password!", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(emailEditText.getText().toString()))
            Toast.makeText(this, "email field is empty!", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(passwordEditText.getText().toString()))
            Toast.makeText(this, "password field is empty", Toast.LENGTH_SHORT).show();
        else {
            auth.createUserWithEmailAndPassword(emailEditText.getText().toString(),
                    passwordEditText.getText().toString()).addOnCompleteListener(this);
        }
    }

    private boolean isValidEmail() {
        return (emailEditText.getText().toString()).contains("@");
    }

    private boolean isValidPassword() {
        return (passwordEditText.getText().toString()).length() >= 8;
    }
}
