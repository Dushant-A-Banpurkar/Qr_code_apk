package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
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

public class Proffesor_Sign_Page extends AppCompatActivity {
    EditText semail,spassword;
    Button mloginbtn;
    TextView signupbtn,forgetbtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_proffesor_sign_page);
        setContentView(R.layout.activity_login);
        semail=findViewById(R.id.profile_email);
        spassword=findViewById(R.id.password);
        mloginbtn=findViewById(R.id.contbtn);
        signupbtn=findViewById(R.id.signupbtn);
        progressBar=findViewById(R.id.progressBar);
        forgetbtn=findViewById(R.id.forgetbtn);
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
                    progressDialog.setMessage("Sign in");
                    progressDialog.setTitle("Sign in");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();


                }
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(Proffesor_Sign_Page.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Proffesor_DashBorad.class));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Proffesor_Sign_Page.this, "Error !", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }

        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Proffesor_Sign_Page.this,Proffesor_SignUp_Page.class);
                startActivity(intent);
            }
        });
        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Proffesor_Sign_Page.this,forget.class);
                startActivity(intent);
            }
        });


    }
}