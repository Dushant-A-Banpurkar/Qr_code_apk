package com.example.demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.util.Calendar;

public class Proffesor_DashBorad extends AppCompatActivity {
    Button logoutbtn;
    ImageView Scan;
    TextView name,phone,email,branch;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String Attendence;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_proffesor_sign_up_dash_borad);

        logoutbtn=findViewById(R.id.Logoutbtn);
        name=findViewById(R.id.profile_name);
        phone=findViewById(R.id.profile_phone);
        email=findViewById(R.id.profile_email);
        branch=findViewById(R.id.profile_branch);
        Scan=findViewById(R.id.scanbtn);
        toolbar=findViewById(R.id.toolbar);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userId=fAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Attendance list");
        DocumentReference documentReference=fStore.collection("Staff").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                phone.setText(documentSnapshot.getString("Phone"));
                name.setText(documentSnapshot.getString("Fullname"));
                email.setText(documentSnapshot.getString("Email"));
                branch.setText(documentSnapshot.getString("Branch"));



            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Proffesor_Sign_Page.class));
                finish();
            }
        });

        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator=new IntentIntegrator(Proffesor_DashBorad.this);
            intentIntegrator.setPrompt("For flash use volume up kay");
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setCaptureActivity(Capture.class);
            intentIntegrator.initiateScan();
            }

        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult.getContents()!=null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Proffesor_DashBorad.this);
            builder.setTitle("Attendance");
            Calendar calendar= Calendar.getInstance();
            String currentDate=DateFormat.getDateInstance().format(calendar.getTime());
            builder.setMessage(currentDate);
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("Present", new DialogInterface.OnClickListener() {
                private static final String TAG ="TAG" ;

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //ite apan presenty gheu  shakto
                    intentResult.getContents().toString();
                    String attendance=new String(String.valueOf(intentResult));
                    databaseReference.push().setValue(attendance);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }) ;
            builder.show();

        }else{
            Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_SHORT).show();
        }
    }
}