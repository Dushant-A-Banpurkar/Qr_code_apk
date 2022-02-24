package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Singup extends AppCompatActivity {
    private static final String TAG ="TAG" ;
    TextView signinbtn;
    ProgressBar progressBar;
    EditText mname,memail,mphone,menrollment,mbranch,mcpassword,mcnpassword;
    Button mregisterbtn;
    ImageView sqrcode;
    FirebaseAuth fAuth;
    FirebaseUser mUser;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressBar=findViewById(R.id.progressBar);
        mname=findViewById(R.id.profile_name);
        menrollment=findViewById(R.id.profile_enrollment);
        mbranch=findViewById(R.id.profile_branch);
        memail=findViewById(R.id.profile_email);
        mphone=findViewById(R.id.profile_phone);
        mcpassword=findViewById(R.id.cpassword);
        mcnpassword=findViewById(R.id.password);
        mregisterbtn=findViewById(R.id.registerbtn);
        signinbtn=findViewById(R.id.signinbtn);
        sqrcode=findViewById(R.id.Qrcode);
        progressDialog=new ProgressDialog(this);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();


        fAuth=FirebaseAuth.getInstance();
        //if(fauth.getCurrentUser() !=null){
        //    startActivity(new Intent(getApplicationContext(),Login.class));
        //    finish();
       // }
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Singup.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
        mregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuthh();

            }
        });

    }

    private void PerforAuthh() {
        String email=memail.getText().toString().trim();
        String password=mcpassword.getText().toString().trim();
        String confirmPassword=mcnpassword.getText().toString().trim();
        String fullname=mname.getText().toString().trim();
        String enrollment=menrollment.getText().toString().trim();
        String branch=mbranch.getText().toString().trim();
        String phone=mphone.getText().toString().trim();


        if(!email.matches(emailPattern)){
            memail.setError("Enter Email Proper");
        }
        if(TextUtils.isEmpty((email))){
            memail.setError("Email is Required.");
            return;
        }
        if(TextUtils.isEmpty((password))){
            mcpassword.setError("Password is Required.");
            return;
        }
        if(password.length()<6){
            mcpassword.setError("Password Must be 8 characters.");
            return;
        }
        else if(!password.equals(confirmPassword)){
            mcnpassword.setError("Password Not match");
        }
        else{
            progressDialog.setMessage("Registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            progressBar.setVisibility(View.VISIBLE);



        }

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(Singup.this,"User Created",Toast.LENGTH_SHORT).show();
                    userID= fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference=fStore.collection("user").document(userID);
                    Map<String,Object> user= new HashMap<>();
                    user.put("Fullname",fullname);
                    user.put("Enrollment",enrollment);
                    user.put("Branch",branch);
                    user.put("Phone",phone);
                    user.put("Email",email);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG,"onSucces: user proifle is created"+ userID);
                        }
                    });
                    startActivity(new Intent(getApplicationContext(),Dashboard1.class));
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Singup.this,"Error !" + task.getException(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });

    }
}