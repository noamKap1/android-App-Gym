package com.example.hgym;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
//intent for showing the user details.
public class userDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGoBack;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView year,month,day,email,roll,firstName,LastName;
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        year = (TextView) findViewById(R.id.year);
        month = (TextView) findViewById(R.id.month);
        day = (TextView) findViewById(R.id.day);
        email = (TextView) findViewById(R.id.Email);
        roll = (TextView) findViewById(R.id.roll);
        firstName = (TextView) findViewById(R.id.fName);
        LastName = (TextView) findViewById(R.id.lName);


        btnGoBack = findViewById(R.id.manuDetail);

        btnGoBack.setOnClickListener(this);

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
                                day.setText(" day: " + User.getDayOfBirth());
                                roll.setText("roll: " + User.getRoll());
                                email.setText("email: " + User.getEmail());
                                firstName.setText("first name: " + User.getFirstname());
                                LastName.setText(" ,last name: " + User.getLastname());
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
    }
}