package com.example.hgym;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
//intent for showing a participent.
public class showParticipent extends AppCompatActivity implements View.OnClickListener{

    Button btnGoBack;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    ImageView photo;
    String itImagePath, path = "";
    StorageReference storageRef,imgRef;
    FirebaseStorage storage;
    Uri uri;
    Intent galleryAdd, data,intent;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView year,month,day,email,roll,firstName,LastName, Image;
    String emailS, passwordS, nameS, lastNameS, rollS, otherEmail;
    int dayS, monthS, yearS;
    //creates the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_participent);
        storage = FirebaseStorage.getInstance();
        intent = getIntent();
        otherEmail = intent.getStringExtra("ID");
        Log.d("email",""+otherEmail);
        otherEmail = otherEmail.substring(6);
        Log.d("email",""+otherEmail);
        otherEmail = otherEmail.substring(1);
        Log.d("email",""+otherEmail);
        storageRef = storage.getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        year = (TextView) findViewById(R.id.year);
        month = (TextView) findViewById(R.id.month);
        day = (TextView) findViewById(R.id.day);
        email = (TextView) findViewById(R.id.Email);
        roll = (TextView) findViewById(R.id.roll);
        firstName = (TextView) findViewById(R.id.fName);
        LastName = (TextView) findViewById(R.id.lName);
        Image = (TextView) findViewById(R.id.imagePic);
        photo = (ImageView) findViewById(R.id.picture);
        btnGoBack = findViewById(R.id.manuDetail);
        btnGoBack.setOnClickListener(this);

        Activity pro = this;
        DocumentReference docRef = db.collection("users").document(""+otherEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("hi", "DocumentSnapshot data: " + document.getData());
                        user User = document.toObject(user.class);
                        Log.d("year:" ,""+User.getYearOfBirth());
                        year.setText(" ,year: " + User.getYearOfBirth());
                        month.setText(" ,month: " + User.getMonthOfBirth());
                        day.setText("day: " + User.getDayOfBirth());
                        roll.setText("roll: " + User.getRoll());
                        email.setText("email: " + User.getEmail());
                        firstName.setText("first name: " + User.getFirstname());
                        LastName.setText(" ,last name: " + User.getLastname());
                        itImagePath = User.getItImagePath();
                        if(itImagePath != null && !itImagePath.isEmpty()){
                            Log.d("tag1", itImagePath);
                            imgRef = storageRef.child("pictures/"+itImagePath);
                            Log.d("tag2", itImagePath);

                            Glide.with(pro)
                                    .load(imgRef)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(photo);
                        }
                    } else {
                        Log.d("hi", "No such document");
                    }
                } else {
                    Log.d("hi", "get failed with ", task.getException());
                }
            }
        });

    }
    //in charge of what happens when clicking on a button.
    @Override
    public void onClick(View v) {
        if( v == btnGoBack)
        {
            Intent intent = new Intent(this,listViewRollActivity.class);
            startActivity(intent);
        }

    }
}