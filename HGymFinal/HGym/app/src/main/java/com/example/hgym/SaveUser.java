package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//intent for saving the user.
public class SaveUser extends AppCompatActivity implements View.OnClickListener  {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_user);
        nameEdit = findViewById(R.id.firstName);
        lastNameEdit = findViewById(R.id.LastName);
        emailEdit =findViewById(R.id.Email);
        rollEdit = findViewById(R.id.Roll);
        passwordEdit = findViewById(R.id.Password);
        dayOfBirth = findViewById(R.id.DayOfBirth);
        monthOfBirth = findViewById(R.id.MonthOfBirth);
        yearOfBirth = findViewById(R.id.YearOfBirth);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        emailEdit.setText(currentUser.getEmail());


        db.collection("users")
                .whereEqualTo("email", currentUser.getEmail())//find how to get the specific name of the last method
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());
                                user user = document.toObject(user.class);
                                passwordEdit.setText(user.getPassword());
                                nameEdit.setText(user.getFirstname());
                                lastNameEdit.setText(user.getLastname());
                                rollEdit.setText(user.getRoll());
                                dayOfBirth.setText(""+user.getDayOfBirth());
                                monthOfBirth.setText(""+user.getMonthOfBirth());
                                yearOfBirth.setText(""+user.getYearOfBirth());
                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    EditText nameEdit, lastNameEdit,emailEdit, rollEdit, passwordEdit, dayOfBirth, monthOfBirth, yearOfBirth;
    Button btnSave;
    String email, password, name, lastName, roll;
    String day, month, year;
    boolean isSaved = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    //in charge of what happens when clicking on a button.
    @Override
    public void onClick(View v) {
        isSaved = false;
        if(v == btnSave) {
            db.collection("users")
                    .whereEqualTo("email", currentUser.getEmail())//find how to get the specific name of the last method
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("Success", document.getId() + " => " + document.getData());
                                    isSaved = true;
                                }
                            } else {
                                Log.d("Error", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            if(!(isSaved)) {
                email = emailEdit.getText().toString();
                password = passwordEdit.getText().toString();
                name = nameEdit.getText().toString();
                lastName = lastNameEdit.getText().toString();
                roll = rollEdit.getText().toString();
                day = dayOfBirth.getText().toString();
                month = monthOfBirth.getText().toString();
                year = yearOfBirth.getText().toString();
                if(!roll.equals("trains") && !roll.equals("trainer")){
                    Context context = getApplicationContext();
                    CharSequence text = "please enter a correct roll, trains/trainer";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(this, text, duration);
                    Log.d("text","do toast");
                    toast.show();
                    Intent intent = new Intent(getBaseContext(),SaveUser.class);
                    startActivity(intent);

                }
                user user = new user(name, lastName, Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year), email, roll, password);
//                db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//
//                                Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
//                                Toast.makeText(createUserActivity.this, "Saving Succeeded", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w("Error", "Error adding document", e);
//                                Toast.makeText(createUserActivity.this, "Saving failed", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                db.collection("users").document(mAuth.getCurrentUser().getEmail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SaveUser.this, "done", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SaveUser.this, "failed: "+e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if(roll.equals("trains")) {
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            } else if(roll.equals("trainer")){
                Intent intent = new Intent(this,MainActivityTrainer.class);
                startActivity(intent);
            }
        }


    }
}