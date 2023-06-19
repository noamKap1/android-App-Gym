package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//intent for showing the groups of the user.
public class ShowGroups extends AppCompatActivity implements View.OnClickListener{
    private ListView lvMethods;
    Button join,go;
    ArrayList<group> alMethods;
    TextView text;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    ArrayList<String> emails = new ArrayList<>();
    private CollectionReference audioColl = db.collection("Groups" + currentUser.getEmail());
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_groups);
        lvMethods = findViewById(R.id.groups);
        join = findViewById(R.id.join);
        join.setOnClickListener(this);
        go = findViewById(R.id.goAlarm);
        go.setOnClickListener(this);
        text =findViewById(R.id.text);
        text.setText("MY GROUPS");
        alMethods = new ArrayList<>();
        loadGamesToListView1();
    }
    //loading the groups of the user.
    private void loadGamesToListView1() {
        String dat = "";
        ArrayList<group> methods = new ArrayList<>();
        audioColl.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for( QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    group g = documentSnapshot.toObject(group.class);
                    Date currentTime = Calendar.getInstance().getTime();
                    if(Integer.parseInt(g.getDate().substring(6)) >= currentTime.getYear()){
                        if(Integer.parseInt(g.getDate().substring(3,5)) >= currentTime.getMonth() || Integer.parseInt(g.getDate().substring(6)) > currentTime.getYear()){
                            if(Integer.parseInt(g.getDate().substring(0,2)) >= Integer.parseInt((currentTime).toString().substring(8,10)) || Integer.parseInt(g.getDate().substring(3,5)) > currentTime.getMonth()|| Integer.parseInt(g.getDate().substring(6)) > currentTime.getYear()){
                                if(Integer.parseInt(g.getHour()) >= currentTime.getHours() || Integer.parseInt(g.getDate().substring(0,2)) > Integer.parseInt((currentTime).toString().substring(8,10)) || Integer.parseInt(g.getDate().substring(3,5)) > currentTime.getMonth()|| Integer.parseInt(g.getDate().substring(6)) > currentTime.getYear()){
                                    if(Integer.parseInt(g.getMinute()) >= currentTime.getMinutes() || Integer.parseInt(g.getHour()) > (currentTime).getHours() || Integer.parseInt(g.getDate().substring(0,2)) > Integer.parseInt((currentTime).toString().substring(8,10)) || Integer.parseInt(g.getDate().substring(3,5)) > currentTime.getMonth()|| Integer.parseInt(g.getDate().substring(6)) > currentTime.getYear()){
                                        methods.add(g);
                                    }
                                }
                            }
                        }
                    }
                    //25.05.2005
                    Log.d("","method size" + methods.size());
                }
                if(methods.size()!=0) {
                    groupAdapter adapter = new groupAdapter(getBaseContext(), methods);
                    lvMethods.setAdapter(adapter);
                }
            }
        });

        if(methods.size()!=0) {
            groupAdapter adapter = new groupAdapter(getParent().getBaseContext(), methods);
            lvMethods.setAdapter(adapter);
        }

    }
    //loading all the groups.
    private void loadGamesToListView2() {
        ArrayList<group> methods = new ArrayList<>(),again = new ArrayList<>();
        ArrayList<group> what = new ArrayList<>();

        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty())
                        {
                            db.collection("users")
                                    .whereEqualTo("roll", "trainer")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("s", document.getId() + " => " + document.getData());
                                                    user data = document.toObject(user.class);
                                                    String email = data.getEmail();
                                                    emails.add(email);
                                                    Log.d("email size", ""+emails.size());
                                                    for(int i=0;i<emails.size();i++){

                                                        db.collection("Groups"+emails.get(i)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                for(QueryDocumentSnapshot doc : task.getResult()){
                                                                    group g = doc.toObject(group.class);
                                                                    Log.d("tags you know",g.getDate()+g.getHour()+g.getMinute());
                                                                    Date currentTime = Calendar.getInstance().getTime();
                                                                    if(Integer.parseInt(g.getDate().substring(6)) >= currentTime.getYear()){
                                                                        if(Integer.parseInt(g.getDate().substring(3,5)) >= currentTime.getMonth() || Integer.parseInt(g.getDate().substring(6)) > currentTime.getYear()){
                                                                            if(Integer.parseInt(g.getDate().substring(0,2)) >= Integer.parseInt((currentTime).toString().substring(8,10)) || Integer.parseInt(g.getDate().substring(3,5)) > currentTime.getMonth()|| Integer.parseInt(g.getDate().substring(6)) > currentTime.getYear()){
                                                                                if(Integer.parseInt(g.getHour()) >= currentTime.getHours() || Integer.parseInt(g.getDate().substring(0,2)) > Integer.parseInt((currentTime).toString().substring(8,10)) || Integer.parseInt(g.getDate().substring(3,5)) > currentTime.getMonth()|| Integer.parseInt(g.getDate().substring(6)) > currentTime.getYear()){
                                                                                    if(Integer.parseInt(g.getMinute()) >= currentTime.getMinutes() || Integer.parseInt(g.getHour()) > (currentTime).getHours() || Integer.parseInt(g.getDate().substring(0,2)) > Integer.parseInt((currentTime).toString().substring(8,10)) || Integer.parseInt(g.getDate().substring(3,5)) > currentTime.getMonth()|| Integer.parseInt(g.getDate().substring(6)) > currentTime.getYear()){
                                                                                        methods.add(g);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                Log.d("size",methods.size()+"");
                                                                for(int i=0;i<methods.size();i++){
                                                                    Log.d("method i",methods.get(i).getDate()+"");
                                                                }

                                                                Log.d("methodd size",methods.size()+"");

                                                                int max=1000;
                                                                for(int k =1;k<methods.size();k++){
                                                                    if(methods.get(0).getDate().equals(methods.get(k).getDate()) && methods.get(0).getHour().equals(methods.get(k).getHour()) && methods.get(0).getMinute().equals(methods.get(k).getMinute()) &&
                                                                            methods.get(0).getEmailOfTrainer().equals(methods.get(k).getEmailOfTrainer())){
                                                                        if(k<max){
                                                                            max=k;
                                                                        }
                                                                    }
                                                                }
                                                                Log.d("max",max+"");
                                                                for(int i=0;i<methods.size();i++){
                                                                    Log.d("method i",methods.get(i).getDate()+"");
                                                                }

                                                                Log.d("foreal",what.size()+"");
                                                                if(methods.size()!=0 && max < 1000) {
                                                                    if(what.size()!=max){
                                                                        for(int o =0;o<max;o++){
                                                                            what.add(methods.get(o));
                                                                        }
                                                                    } else {
                                                                        groupAdapter adapter = new groupAdapter(getBaseContext(), what);
                                                                        lvMethods.setAdapter(adapter);
                                                                    }

                                                                }
                                                                Log.d("size of what`1",""+what.size());
                                                            }
                                                        });
                                                        Log.d("size of what",""+what.size());

                                                        //audioColl = db.collection("Groups" + emails.get(i));
                                                        //Log.d("email",""+emails.get(i));
                                                        //String dat = "";
                                                        //audioColl.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            //@Override
                                                            //public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                //for( QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                                                    //group g = documentSnapshot.toObject(group.class);
                                                                  //  Log.d("g",g.getDate());
                                                                //    methods.add(g);
                                                              //      Log.d("","method size" + methods.size());
                                                            //    }
                                                          //  }
                                                        //});
                                                    }

                                                }

                                            } else {
                                                Log.d("", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                            Log.d("size of what", what.size()+"");
                        }
                        else
                        {
                            Log.d("exercise","No data found in query");
                            Toast.makeText(ShowGroups.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowGroups.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    //in charge of what happens when pressing a button.
    @Override
    public void onClick(View v) {
        if(v==join){
            loadGamesToListView2();
            text.setText("CHOOSE A GROUP:");
        }
        if(v == go){
            Intent intent = new Intent(this,alarmActivity.class);
            startActivity(intent);
        }
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
                    Intent intent = new Intent(ShowGroups.this, LogInActivity.class);
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