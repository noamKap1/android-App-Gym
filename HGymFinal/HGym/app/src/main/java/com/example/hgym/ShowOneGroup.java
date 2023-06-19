package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
//intent for showing one group.
public class ShowOneGroup extends AppCompatActivity implements View.OnClickListener {
    private TextView exp, fName, lName, duration, email;
    private Button returnBut, joinBut, leaveBut;
    private ImageView image;
    String gameId, date, hour, minute, gmailOfTrainer;
    group g = new group();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fDB = FirebaseFirestore.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private CollectionReference audioColl;
    StorageReference storageRef, imgRef;
    FirebaseStorage storage;

    String itImagePath, roll1;
    Intent intent;
    String path = "Groups";
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_one_group);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        intent = getIntent();
        gameId = intent.getStringExtra("gameId");
        date = intent.getStringExtra("date");
        hour = intent.getStringExtra("hour");
        minute = intent.getStringExtra("minute");
        Log.d("gameId,date,hour,minute", gameId + date + hour + minute);
        exp = (TextView) findViewById(R.id.exp);
        fName = (TextView) findViewById(R.id.fName);
        lName = (TextView) findViewById(R.id.lName);
        duration = (TextView) findViewById(R.id.duration);
        email = (TextView) findViewById(R.id.email);
        returnBut = findViewById(R.id.returnToGroups);
        returnBut.setOnClickListener(this);
        image = findViewById(R.id.picture);
        joinBut = findViewById(R.id.join);
        joinBut.setOnClickListener(this);
        leaveBut = findViewById(R.id.leave);
        leaveBut.setOnClickListener(this);
        path += gameId;
        Log.d("path", path);
        ArrayList<group> methods = new ArrayList<>();
        db.collection(path)
                .whereEqualTo("date", date)//find how to get the specific name of the last method
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                group record = document.toObject(group.class);
                                methods.add(record);
                                Log.d("come", "here");
                            }
                            for (int i = 0; i < methods.size(); i++) {
                                if (methods.get(i).getDate().equals(date) && methods.get(i).getMinute().equals(minute)
                                        && methods.get(i).getHour().equals(hour)) {
                                    g = methods.get(i);
                                }
                            }
                            exp.setText("explanation of the practice: " + g.getExp());
                            duration.setText("duration of the practice: " + g.getDuration() + "");
                            email.setText("email of the trainer: " + g.getEmailOfTrainer());
                            gmailOfTrainer = g.getEmailOfTrainer();
                            Log.d("method size, g exp", methods.size() + "," + g.getExp());
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
        Log.d("real path", path + date + hour + minute);


        Activity pro = this;
        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            db.collection("users")
                                    .whereEqualTo("email", g.getEmailOfTrainer())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("s", document.getId() + " => " + document.getData());
                                                    user data = document.toObject(user.class);
                                                    String f = "name of the trainer: " + data.getFirstname() + " ";
                                                    fName.setText(f);
                                                    lName.setText(data.getLastname());
                                                    itImagePath = data.getItImagePath();
                                                    if (itImagePath != null && !itImagePath.isEmpty()) {
                                                        Log.d("tag1", itImagePath);
                                                        imgRef = storageRef.child("pictures/" + itImagePath);
                                                        Log.d("tag2", itImagePath);

                                                        Glide.with(pro)
                                                                .load(imgRef)
                                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                                .into(image);
                                                    }
                                                }
                                            } else {
                                                Log.d("", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.d("exercise", "No data found in query");
                            Toast.makeText(ShowOneGroup.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowOneGroup.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //in charge of what happens when clicking a button.
    @Override
    public void onClick(View v) {
        if (v == returnBut) {
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
                                    roll1 = roll;
                                    if (roll.equals("trains")) {
                                        Intent intent = new Intent(getBaseContext(), ShowGroups.class);
                                        startActivity(intent);
                                    } else if (roll.equals("trainer")) {
                                        Intent intent = new Intent(getBaseContext(), ShowGroupsTrainer.class);
                                        startActivity(intent);
                                    }
                                }
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }
        if (v == joinBut) {
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
                                    roll1 = data.getRoll();
                                    Log.d("roll", roll1);
                                    if (!roll1.equals("trainer")) {
                                        path = "Groups" + gameId;
                                        audioColl = db.collection(path);
                                        ArrayList<group> methods = new ArrayList<>();
                                        audioColl.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                    group record = documentSnapshot.toObject(group.class);
                                                    Log.d(record.getDate() + record.getEmailOfTrainer() + record.getDate(), date + gmailOfTrainer + hour);
                                                    if (record.getDate().equals(date) && record.getEmailOfTrainer().equals(gmailOfTrainer) && record.getHour().equals(hour)) {
                                                        ArrayList<user> par = record.getParticipants();
                                                        db.collection("users").get()
                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                        if (!queryDocumentSnapshots.isEmpty()) {
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
                                                                                                    boolean flag = true;
                                                                                                    for (int i = 0; i < par.size(); i++) {
                                                                                                        if (par.get(i).getFirstname().equals(data.getFirstname()) && par.get(i).getLastname().equals(data.getLastname())) {
                                                                                                            flag = false;
                                                                                                        }
                                                                                                    }
                                                                                                    if (!flag) {
                                                                                                        Toast.makeText(ShowOneGroup.this, "you are already in the group", Toast.LENGTH_SHORT).show();
                                                                                                    } else {
                                                                                                        par.add(data);
                                                                                                        db.collection("Groups" + data.getEmail()).document(""+data.getEmail()+record.getDate()+record.getHour()+record.getMinute()).set(record);
                                                                                                        record.setParticipants(par);
                                                                                                        db.collection("Groups" + gmailOfTrainer).document(gmailOfTrainer + record.getDate() + record.getHour() + record.getMinute())
                                                                                                                .update("participants", par)
                                                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                                        Log.d("Success", "DocumentSnapshot successfully updated!");
                                                                                                                        Toast.makeText(ShowOneGroup.this, "name updated", Toast.LENGTH_SHORT).show();
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
                                                                                            } else {
                                                                                                Log.d("", "Error getting documents: ", task.getException());
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        } else {
                                                                            Log.d("exercise", "No data found in query");
                                                                            Toast.makeText(ShowOneGroup.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                        Toast.makeText(ShowOneGroup.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                                                        startActivity(intent);
                                                    }

                                                }
                                            }
                                        });
                                    } else {
                                        Log.d("came", "here");
                                        Toast.makeText(ShowOneGroup.this, "you are a trainer not a trainee", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });


        }
        if (v == leaveBut) {
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
                                    roll1 = data.getRoll();
                                    if (!roll1.equals("trainer")) {
                                        path = "Groups" + gameId;
                                        audioColl = db.collection(path);
                                        ArrayList<group> methods = new ArrayList<>();
                                        audioColl.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                    group record = documentSnapshot.toObject(group.class);
                                                    Log.d(record.getDate() + record.getEmailOfTrainer() + record.getDate(), date + gmailOfTrainer + hour);
                                                    if (record.getDate().equals(date) && record.getEmailOfTrainer().equals(gmailOfTrainer) && record.getHour().equals(hour)) {
                                                        ArrayList<user> par = record.getParticipants();
                                                        db.collection("users").get()
                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                        if (!queryDocumentSnapshots.isEmpty()) {
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
                                                                                                    boolean flag = false;
                                                                                                    for (int i = 0; i < par.size(); i++) {
                                                                                                        if (par.get(i).getFirstname().equals(data.getFirstname()) && par.get(i).getLastname().equals(data.getLastname())) {
                                                                                                            flag = true;
                                                                                                        }
                                                                                                    }
                                                                                                    if (!flag) {
                                                                                                        Toast.makeText(ShowOneGroup.this, "you are not in the group", Toast.LENGTH_SHORT).show();
                                                                                                    } else {
                                                                                                        ArrayList<user> parNew = new ArrayList<>();
                                                                                                        for (int j = 0; j < par.size(); j++) {
                                                                                                            if (par.get(j).getFirstname().equals(data.getFirstname()) && par.get(j).getLastname().equals(data.getLastname())) {
                                                                                                                Log.d("user:", data.getEmail());
                                                                                                            } else {
                                                                                                                parNew.add(par.get(j));
                                                                                                            }
                                                                                                        }
                                                                                                        record.setParticipants(parNew);
                                                                                                        db.collection("Groups" + gmailOfTrainer).document(gmailOfTrainer + record.getDate() + record.getHour() + record.getMinute())
                                                                                                                .update("participants", parNew)
                                                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                                        Log.d("Success", "DocumentSnapshot successfully updated!");
                                                                                                                        Toast.makeText(ShowOneGroup.this, "name updated", Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                })
                                                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                                                    @Override
                                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                                        Log.w("Error", "Error updating document", e);
                                                                                                                    }
                                                                                                                });
                                                                                                        db.collection("Groups" + data.getEmail()).document(data.getEmail() + g.getDate() + g.getHour() + g.getMinute()).delete();

                                                                                                    }

                                                                                                }
                                                                                            } else {
                                                                                                Log.d("", "Error getting documents: ", task.getException());
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        } else {
                                                                            Log.d("exercise", "No data found in query");
                                                                            Toast.makeText(ShowOneGroup.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                        Toast.makeText(ShowOneGroup.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                                                        startActivity(intent);
                                                    }


                                                }
                                            }
                                        });
                                    } else{
                                        Log.d("came", "here");
                                        Toast.makeText(ShowOneGroup.this, "you are a trainer not a trainee", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
}