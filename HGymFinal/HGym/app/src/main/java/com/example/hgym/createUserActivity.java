package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//intent for creating a user.
public class createUserActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //creates the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_activiy);
        nameEdit = findViewById(R.id.firstName);
        lastNameEdit = findViewById(R.id.LastName);
        emailEdit =findViewById(R.id.Email);
        rollEdit = findViewById(R.id.Roll);
        passwordEdit = findViewById(R.id.Password);
        dayOfBirth = findViewById(R.id.DayOfBirth);
        monthOfBirth = findViewById(R.id.MonthOfBirth);
        yearOfBirth = findViewById(R.id.YearOfBirth);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnReturn = findViewById(R.id.btnReturn);
        btnSave.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

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
    Button btnSave, btnReturn, btnDelete, btnUpdate;
    String email, password, name, lastName, roll;
    String day, month, year;
    boolean isSaved = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    //in charge of what happens when pressing a button.
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
                    Toast.makeText(createUserActivity.this, "please enter a correct roll, trains/trainer", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,createUserActivity.class);
                    startActivity(intent);
                }
                user user = new user(name, lastName, Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year), email, roll, password);

                db.collection("users").document(mAuth.getCurrentUser().getEmail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(createUserActivity.this, "done", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(createUserActivity.this, "failed: "+e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if(roll.equals("trains")) {
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            } else if(roll.equals("trainer")){
                Intent intent = new Intent(this,MenuActivity.class);
                startActivity(intent);
            }
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
            db.collection("users").document(name+"noam19012022").delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Success", "DocumentSnapshot successfully deleted!");
                            Toast.makeText(createUserActivity.this, "deleting succeeded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error", "Error deleting document", e);
                            Toast.makeText(createUserActivity.this, "deleting failed", Toast.LENGTH_SHORT).show();
                        }
                    });
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
        if(v == btnUpdate)
        {
            name = nameEdit.getText().toString();
            lastName = lastNameEdit.getText().toString();
            roll = rollEdit.getText().toString();

            db.collection("users").document(mAuth.getCurrentUser().getEmail())
                    .update("name", name)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Success", "DocumentSnapshot successfully updated!");
                            Toast.makeText(createUserActivity.this, "name updated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error", "Error updating document", e);
                        }
                    });

            db.collection("users").document(mAuth.getCurrentUser().getEmail())
                    .update("lastName", lastName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Success", "DocumentSnapshot successfully updated!");
                            Toast.makeText(createUserActivity.this, "last name updated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error", "Error updating document", e);
                        }
                    });

            db.collection("users").document(mAuth.getCurrentUser().getEmail())
                    .update("roll", roll)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Success", "DocumentSnapshot successfully updated!");
                            Toast.makeText(createUserActivity.this, "roll updated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error", "Error updating document", e);
                        }
                    });
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
    }
}