package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText semail,spassword;
    Button mloginbtn;
    TextView signupbtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        semail=findViewById(R.id.profile_email);
        spassword=findViewById(R.id.password);
        mloginbtn=findViewById(R.id.loginbtn);
        signupbtn=findViewById(R.id.signupbtn);
        progressBar=findViewById(R.id.progressBar);
        progressDialog=new ProgressDialog(this);
        fAuth=FirebaseAuth.getInstance();


        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = semail.getText().toString().trim();
                String password = spassword.getText().toString().trim();

                if (!email.matches(emailPattern)) {
                    semail.setError("Enter Email Proper");
                }
                if (TextUtils.isEmpty((email))) {
                    semail.setError("Email is Required.");
                }
                if (TextUtils.isEmpty((password))) {
                    spassword.setError("Password is Required.");

                }
                if (password.length() < 6) {
                    spassword.setError("Password Must be 8 characters.");
                } else {
                    progressDialog.setMessage("Registration");
                    progressDialog.setTitle("Registration");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    progressBar.setVisibility(View.VISIBLE);

                }
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Dashboard1.class));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Error !", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }

        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Singup.class);
                startActivity(intent);
            }
        });

    }
}