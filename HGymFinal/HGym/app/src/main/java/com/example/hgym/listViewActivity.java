package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
//intent for the place the fragments is being shown.
public class listViewActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView lvMethods;
    ArrayList<nonEquipmentExercise> alMethods;
    Button firstFragmentBtn, secondFragmentBtn;
    TextView textView;


    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        textView = findViewById(R.id.choose);
        firstFragmentBtn = findViewById(R.id.fragment1btn);
        secondFragmentBtn = findViewById(R.id.fragment2btn);
        firstFragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.INVISIBLE);
                replaceFragment(new BlankFragmentNon());
            }
        });
        secondFragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.INVISIBLE);
                replaceFragment(new BlankFragment());
            }
        });



    }
    //replacing the fragment.
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    //loading the exercises for the list view.
    private void loadGamesToListView() {
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
                                alMethods = data.getNonEquipmentExercise();
                                System.out.println(alMethods.size());
                                for(int i=0;i<alMethods.size();i++){
                                    System.out.println(alMethods.get(i).getName());
                                }
                                makeAdapter adapter = new makeAdapter(listViewActivity.this, alMethods);
                                lvMethods.setAdapter(adapter);
                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
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
        if(id == R.id.menu_logOut)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("are you sure you want to log out?")
                    .setTitle("log out");

            builder.setPositiveButton("yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(listViewActivity.this, LogInActivity.class);
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