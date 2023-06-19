package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
//intent for showing a non equipment exercise.
public class showNonExercises extends AppCompatActivity implements View.OnClickListener{
    private TextView exp,name,level,duration,muscle;
    private Button returnBut;
    String gameId;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fDB = FirebaseFirestore.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    Intent intent;
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_non_exercises);
        intent = getIntent();
        gameId = intent.getStringExtra("gameId");
        exp = (TextView) findViewById(R.id.exp);
        name = (TextView) findViewById(R.id.nameOfEx);
        level = (TextView) findViewById(R.id.level);
        duration = (TextView) findViewById(R.id.duration);
        muscle = (TextView) findViewById(R.id.muscle);
        returnBut = findViewById(R.id.ret);
        returnBut.setOnClickListener(this);
        fDB.collection("users")
                .whereEqualTo("email", currentUser.getEmail())//find how to get the specific name of the last method
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user User = document.toObject(user.class);
                                ArrayList<nonEquipmentExercise> Methods = User.getNonEquipmentExercise();
                                int index=0;
                                for(int i=0;i<Methods.size();i++){
                                    if(Methods.get(i).getName().equals(gameId)){
                                        index = i;
                                    }
                                }
                                exp.setText("explanation: " + Methods.get(index).getExplanation());
                                name.setText("name: " + Methods.get(index).getName());
                                level.setText("level:" + Methods.get(index).getLevelOfDifficulty());
                                duration.setText("duration:" + Methods.get(index).getDuration());
                                muscle.setText("muscle group: " + Methods.get(index).getMuscleGroup());
                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    //in charge of what happens when clicking on a button.
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