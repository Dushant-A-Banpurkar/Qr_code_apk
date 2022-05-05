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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Dashboard1 extends AppCompatActivity {
    Button logoutbtn;
    TextView name,enrollment,phone,email,branch;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
   ImageView iv;

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
        iv = findViewById(R.id.image);

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

                try{
                    bitmap = textToImageEncode(enrollment.getText().toString());
                    iv.setImageBitmap(bitmap);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

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

    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeWidth, null);
        } catch (IllegalArgumentException e) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

}