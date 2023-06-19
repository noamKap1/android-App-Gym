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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
//intent for showing the menu for a trainee.
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
//AudioRecordActivity

    ImageButton createUser, userDetailsByRoll, goRecord, groupBtn;
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manu);
        groupBtn = findViewById(R.id.goGroups);
        groupBtn.setOnClickListener(this);
        createUser = findViewById(R.id.createUserBtn);
        createUser.setOnClickListener(this);
        userDetailsByRoll = findViewById(R.id.userByRollBtn);
        userDetailsByRoll.setOnClickListener(this);
        goRecord = findViewById(R.id.recorder);
        goRecord.setOnClickListener(this);
    }
    //in charge of what heppens when clicking a button.
    @Override
    public void onClick(View v) {
        Log.d("o", "1: ");
        Intent S = new Intent(this, MyService.class);
        if (v == createUser) {
            Intent intent = new Intent(this, createUserActivity.class);
            startActivity(intent);
        }
        else if (v == userDetailsByRoll) {
            Log.d("o", "onClick: ");
            Intent intent = new Intent(this, listViewRollActivity.class);
            startActivity(intent);
        }
        else if( v == goRecord){
            Intent intent = new Intent(this, AudioRecordActivity.class);
            startActivity(intent);
        } else if(v==groupBtn){
            Intent intent = new Intent(this, ShowGroups.class);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("want to show the exercises?")
                    .setTitle("show exercises");

            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    goToListVIew();
                }
            });
            builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(MenuActivity.this, "canceled", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();

        }
        if(id == R.id.menu_backToMenu){

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
                    Intent intent = new Intent(MenuActivity.this, LogInActivity.class);
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
    //showing the list view of the personal with the same roll.
    public void goToListVIew(){
        Intent intent = new Intent(this, listViewActivity.class);
        startActivity(intent);
    }

}


