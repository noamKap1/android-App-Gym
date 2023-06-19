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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
//intent for showing the profile of the user.
public class Profile extends AppCompatActivity implements View.OnClickListener {

    Button btnGoBack, openCamera, openGallery, savePic;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    ImageView photo;
    String itImagePath, path = "";
    StorageReference storageRef,imgRef;
    FirebaseStorage storage;
    Uri uri;
    Intent galleryAdd, data;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView year,month,day,email,roll,firstName,LastName, Image;
    String emailS, passwordS, nameS, lastNameS, rollS;
    int dayS, monthS, yearS;
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        storage = FirebaseStorage.getInstance();
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
        openGallery = findViewById(R.id.gallery);
        btnGoBack = findViewById(R.id.manuDetail);
        openCamera = findViewById(R.id.openCamera);
        savePic = findViewById(R.id.save);
        btnGoBack.setOnClickListener(this);
        openCamera.setOnClickListener(this);
        openGallery.setOnClickListener(this);
        savePic.setOnClickListener(this);

        Activity pro = this;
        db.collection("users")
                .whereEqualTo("email", currentUser.getEmail())//find how to get the specific name of the last method
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user User = document.toObject(user.class);
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

                                Log.d("sssdadadsadasdasd", User.getItImagePath() + "ss");
//                                Log.d("tag", itImagePath);
                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });





    }
    //in charge of what happens when clicking a button.
    @Override
    public void onClick(View v) {
        if( v == btnGoBack)
        {
            db.collection("users")
                    .whereEqualTo("email", mAuth.getCurrentUser().getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("s", document.getId() + " => " + document.getData());
                                    user data = document.toObject(user.class);
                                    String roll = data.getRoll();
                                    if(roll.equals("trains")) {
                                        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                                        startActivity(intent);
                                    } else if(roll.equals("trainer")){
                                        Intent intent = new Intent(getBaseContext(),MainActivityTrainer.class);
                                        startActivity(intent);
                                    }
                                }
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }
        if (v == openCamera) {
            //CHECKING THAY I HAVE PREMISSON TO CAMERA
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            }
            dispatchTakePictureIntent();
        }
        if (v == openGallery){
            galleryAdd = new Intent(Intent.ACTION_PICK);
            galleryAdd.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryAdd.putExtra("GALLERY_REQ_CODE", 1000);
            someActivityResultLauncher.launch(galleryAdd);
        }
        if (v == savePic){
            UploadPhoto2();
        }
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        data = result.getData();
                        photo.setImageURI(data.getData());
                        photo.setTransitionName(data.getDataString());
                    }
                    else
                        Log.d("CAM","Problem erupt:"+ result);
                }
            });
    //creates and handels the camera request
    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, REQUEST_IMAGE_CAPTURE);
        //Log.d("CAM", data.toString());
    }
    //getting the uri from image
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    //for the camera its gets an intent and put on it the data from the camera and handels the camera
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(image);
            photo.setImageURI(data.getData());
            uri = getImageUri(this, image);
        }
    }
    //Camera intent
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("CAMERA_REQ_CODE",1000);
        openCamera();
    }
    //creating a random item path.
    public String itemPath() {

        for (int i = 0; i < 20; i++) {
            path = path + String.valueOf(Math.round(Math.random() * (10 - 1) + 1));
        }
        Log.d("PATH", path);
        return path;
    }

    //uploading an image to firestoreStorage
    private void UploadPhoto2() {
        Uri file = null;
        if(photo == null)
            Log.d("CAM","BASA");
        if(data != null)
            file = data.getData();
        else
        {
            file = uri;
        }
        itImagePath = itemPath();
        StorageReference riversRef = storageRef.child("pictures/" + itImagePath);
        UploadTask uploadTask = riversRef.putFile(file);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("UPIMAGE", exception.toString());
                Toast.makeText(Profile.this, "Sorry currently we are unable to upload tour image", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile.this, "The image was successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        });

        String name = firstName.getText().toString();
        db.collection("users").document(mAuth.getCurrentUser().getEmail())
                .update("itImagePath", itImagePath)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully updated!");
                        Log.d("changing", "name");
                        Toast.makeText(Profile.this, "name updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error updating document", e);
                    }
                });

    }
}