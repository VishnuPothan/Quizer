package com.vishnu.quizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail, editTextPassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.login:
                finish();
                startActivity(new Intent(this, signup.class));
                break;

            case R.id.save:
                userlogin();
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() !=null ){
            finish();
            startActivity(new Intent(this , Home.class));
        }
    }

    private void userlogin() {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty()){
                editTextEmail.setError("Email Required");
                editTextEmail.requestFocus();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextPassword.setError("Enter valid email address.");
                editTextPassword.requestFocus();
                return;
            }

            if(password.length()<6){
                editTextPassword.setError("Minimum length of password should be 6");
                editTextPassword.requestFocus();
                return;
            }

            if (password.isEmpty()){
                editTextPassword.setError("Email required");
                editTextPassword.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()){
                        finish();
                        Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this , Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
        }
}

