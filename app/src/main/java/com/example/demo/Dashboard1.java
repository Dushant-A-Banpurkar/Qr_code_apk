package com.example.demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Dashboard1 extends AppCompatActivity {
    Button logoutbtn,upload;
    ImageView image,qrcode;
    TextView name,enrollment,phone,email,branch;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard1);
        logoutbtn=findViewById(R.id.Logoutbtn);
        name=findViewById(R.id.profile_name);
        enrollment=findViewById(R.id.profile_enrollment);
        phone=findViewById(R.id.profile_phone);
        email=findViewById(R.id.profile_email);
        branch=findViewById(R.id.profile_branch);
        fAuth=FirebaseAuth.getInstance();
        qrcode=findViewById(R.id.Qrcode);
        fStore=FirebaseFirestore.getInstance();
        userId=fAuth.getCurrentUser().getUid();


        DocumentReference documentReference=fStore.collection("user").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                phone.setText(documentSnapshot.getString("Phone"));
                name.setText(documentSnapshot.getString("Fullname"));
                enrollment.setText(documentSnapshot.getString("Enrollment"));
                email.setText(documentSnapshot.getString("Email"));
                branch.setText(documentSnapshot.getString("Branch"));



            }
        });


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

    }

}