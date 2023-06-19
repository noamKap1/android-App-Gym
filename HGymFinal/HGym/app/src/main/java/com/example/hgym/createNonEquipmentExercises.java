package com.example.hgym;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
//intent for creating a non equipment exercises.
public class createNonEquipmentExercises extends AppCompatActivity implements View.OnClickListener {

    EditText emailEdit ,nameE, muscleE, levelE, expE, durationE;
    Button btnSave, btnReturn, btnDelete, btnUpdate;
    String name, muscle, level, exp, duration;
    boolean isSaved = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_non_equipment_exercises);
        nameE = findViewById(R.id.name);
        muscleE = findViewById(R.id.muscleGroup);
        levelE =findViewById(R.id.level);
        expE = findViewById(R.id.exp);
        durationE = findViewById(R.id.duration);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnReturn = findViewById(R.id.btnReturn);
        btnSave.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);


//        db.collection("users")
//                .whereEqualTo("email", currentUser.getEmail())//find how to get the specific name of the last method
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("Success", document.getId() + " => " + document.getData());
//                                equipmentExercise ex = document.toObject(equipmentExercise.class);
//                                String name = nameE.getText().toString();
//                                muscleE.setText(ex.getMuscleGroup());
//                                levelE.setText(ex.getLevelOfDifficulty());
//                                expE.setText(ex.getExplanation());
//                                weightE.setText(""+ex.getWeight());
//                            }
//                        } else {
//                            Log.d("Error", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });



    }
    //in charge of what happens when clicking a button.
    @Override
    public void onClick(View v) {
        if(v == btnSave) {
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
                                    ArrayList<nonEquipmentExercise> eq = data.getNonEquipmentExercise();
                                    name = nameE.getText().toString();
                                    muscle = muscleE.getText().toString();
                                    level = levelE.getText().toString();
                                    exp = expE.getText().toString();
                                    duration = durationE.getText().toString();
                                    nonEquipmentExercise exercise = new nonEquipmentExercise(muscle, name, level, exp, Double.parseDouble(duration));
                                    boolean flag = eq.add(exercise);
                                    DocumentReference docRef = db.collection("users").document(document.getId());
                                    docRef.update("nonEquipmentExercise", eq)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.d("TAG" , "CHANGE SUCCESSFUL");
                                                    }
                                                    else{
                                                        Log.d("TAG" , "CHANGE NOT SUCCESSFUL");
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });
            Intent intent = new Intent(this, listViewActivity.class);
            startActivity(intent);
        }
        if(v == btnReturn){
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
        //remember!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if(v == btnDelete)
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
                                    ArrayList<nonEquipmentExercise> eq = data.getNonEquipmentExercise();
                                    name = nameE.getText().toString();
                                    muscle = muscleE.getText().toString();
                                    level = levelE.getText().toString();
                                    exp = expE.getText().toString();
                                    duration = durationE.getText().toString();
                                    nonEquipmentExercise exercise = new nonEquipmentExercise(muscle, name, level, exp, Double.parseDouble(duration));
                                    ArrayList<nonEquipmentExercise> neqEq = new ArrayList<>();
                                    for(int i=0;i<eq.size();i++){
                                        if(!eqEx(exercise, eq.get(i))){
                                            neqEq.add(eq.get(i));
                                        } else {
                                            System.out.println("sec");
                                        }
                                    }
                                    DocumentReference docRef = db.collection("users").document(document.getId());
                                    docRef.update("nonEquipmentExercise", neqEq)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.d("TAG" , eq.get(1).name);
                                                        Log.d("TAG" , "CHANGE SUCCESSFUL1");
                                                    }
                                                    else{
                                                        Log.d("TAG" , "CHANGE NOT SUCCESSFUL");
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });
            Intent intent = new Intent(this, listViewActivity.class);
            startActivity(intent);
        }
        if(v == btnUpdate)
        {
            name = nameE.getText().toString();
            muscle = muscleE.getText().toString();
            level = levelE.getText().toString();
            exp = expE.getText().toString();
            duration = durationE.getText().toString();

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
                                    ArrayList<nonEquipmentExercise> exercises = data.getNonEquipmentExercise();
                                    ArrayList<nonEquipmentExercise> newArr = new ArrayList<>();
                                    for(int i=0;i<exercises.size();i++){
                                        Log.d("name", exercises.get(i).getName());
                                        Log.d("nameforil", name);
                                        if(name.equals(exercises.get(i).getName())){
                                            nonEquipmentExercise exercise = new nonEquipmentExercise(muscle, name, level, exp, Double.parseDouble(duration));
                                            Log.d("duration", duration);
                                            newArr.add(exercise);
                                            db.collection("users").document(mAuth.getCurrentUser().getEmail())
                                                    .update("nonEquipmentExercise", newArr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Log.d("TAG","good");
                                                                Toast.makeText(createNonEquipmentExercises.this, "deleting succeeded", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Log.d("TAG", "bad");
                                                                Toast.makeText(createNonEquipmentExercises.this, "deleting failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            newArr.add(exercises.get(i));
                                        }
                                    }
                                }
                            }
                        }

                    });
            Intent intent = new Intent(this, listViewActivity.class);
            startActivity(intent);
        }

    }
    //checking if 2 exercises are the same.
    public boolean eqEx(nonEquipmentExercise ex1, nonEquipmentExercise ex2){
        if(ex1.getDuration() == ex2.getDuration()){
            if(ex1.getExplanation().equals(ex2.getExplanation())){
                if(ex1.getMuscleGroup().equals(ex2.getMuscleGroup())){
                    if(ex1.getName().equals(ex2.getName())){
                        if(ex1.getLevelOfDifficulty().equals(ex2.getLevelOfDifficulty())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}