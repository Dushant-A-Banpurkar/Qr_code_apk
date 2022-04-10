package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forget extends AppCompatActivity {
    Button login2btn;
    EditText EM_PH;
    TextView signinbtn;
    FirebaseAuth fAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget);
        login2btn=findViewById(R.id.contbtn);
        EM_PH=findViewById(R.id.code1);

        signinbtn=findViewById(R.id.singinbtn);
        fAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        login2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
                Intent intent=new Intent(forget.this,Login.class);
                startActivity(intent);
            }
        });

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void resetpassword() {
        String email=EM_PH.getText().toString().trim();

        if(email.isEmpty()){
            EM_PH.setError("Email is required");
            EM_PH.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EM_PH.setError("Please provide valid email!");
            EM_PH.requestFocus();
            return;
        }
        progressDialog.setMessage("Sign in");
        progressDialog.setTitle("Sign in");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(forget.this,"Check your email to reset your password!",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(forget.this,"Try again! Something wrong happened!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}