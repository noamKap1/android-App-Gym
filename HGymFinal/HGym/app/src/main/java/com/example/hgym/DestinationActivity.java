package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//the destination acticity thats been shown after pressing a notification.
public class DestinationActivity extends AppCompatActivity implements View.OnClickListener{
    private Button goGroups, goManu;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        goGroups = findViewById(R.id.goToYourGroups);
        goGroups.setOnClickListener(this);
        goManu = findViewById(R.id.goToManu);
        goManu.setOnClickListener(this);
    }
    //in charge of what happens when pressing a button.
    @Override
    public void onClick(View v) {

        if( v == goGroups)
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
                                        Intent intent = new Intent(getBaseContext(), ShowGroups.class);
                                        startActivity(intent);

                                    } else if(roll.equals("trainer")){
                                        Intent intent = new Intent(getBaseContext(),ShowGroupsTrainer.class);
                                        startActivity(intent);
                                    }
                                }
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        if( v == goManu){
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
    }
}