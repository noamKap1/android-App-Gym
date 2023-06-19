package com.example.hgym;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
//intent for showing people from the same roll.
public class listViewRollActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView lvMethods;
    ArrayList<trainerForListView> alMethods;
    private TextView putRoll;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

//לשנות!!!!!!!!!!!!!!
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_roll);
        putRoll = findViewById(R.id.tvRoll);
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
                                if(roll.equals("trains")){
                                    putRoll.setText("Trainees");
                                } else {
                                    putRoll.setText("Trainers");
                                }
                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });

        db = FirebaseFirestore.getInstance();
        lvMethods = findViewById(R.id.trainers);
        alMethods = new ArrayList<>();
//
        loadGamesToListView();


    }
    //loading the personal of the same roll as the user.
    private void loadGamesToListView() {
        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty())
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
                                                    // if the snapshot is not empty we are hiding
                                                    // our progress bar and adding our data in a list.
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                    Log.d("exercise", "List size: " + list.size());
                                                    for (DocumentSnapshot d : list) {
                                                        // after getting this list we are passing
                                                        // that list to our object class.
                                                        user u = d.toObject(user.class);
                                                        Log.d("exercise", u.toString());
                                                        // after getting data from Firebase we are
                                                        // storing that data in our array list
                                                        if(u.getRoll().equals(roll)){
                                                            trainerForListView ex = new trainerForListView("first name: " + u.getFirstname(), "last name: " + u.getLastname(), "gmail: " + u.getEmail());
                                                            alMethods.add(ex);
                                                        }
                                                    }
                                                    // after that we are passing our array list to our adapter class.
                                                    makeRollAdapter adapter = new makeRollAdapter(listViewRollActivity.this, alMethods);

                                                    // after passing this array list to our adapter
                                                    // class we are setting our adapter to our list view.
                                                    lvMethods.setAdapter(adapter);
                                                }
                                            } else {
                                                Log.d("", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            Log.d("exercise","No data found in query");
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(listViewRollActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // we are displaying a toast message
                        // when we get any error from Firebase.
                        Toast.makeText(listViewRollActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //showing the menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    //in charge of what happens when pressing on item of the menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.menu_showExercises) {
            Intent intent = new Intent(this, listViewActivity.class);
            startActivity(intent);
        }
        if(id == R.id.menu_profile){
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }
        if(id == R.id.menu_createEqExercises){
            Intent intent = new Intent(this, createEquipmentExercises.class);
            startActivity(intent);
        }
        if(id == R.id.menu_createNonEqExercises){
            Intent intent = new Intent(this, createNonEquipmentExercises.class);
            startActivity(intent);
        }
        if(id == R.id.menu_backToMenu){
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
        if(id == R.id.menu_logOut)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("are you sure you want to log out?")
                    .setTitle("log out");

            builder.setPositiveButton("yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(listViewRollActivity.this, LogInActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog dialog = builder.create();

            dialog.show();
        }
        return true;
    }
}