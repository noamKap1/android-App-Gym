
package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
//intent for logging in to the app.
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Noam";
    private FirebaseAuth mAuth;
    Button login, reg ;
    String password, email;
    EditText emailEdit, passwordEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.btnLogIn);
        reg = findViewById(R.id.btnReg);

        login.setOnClickListener(this);
        reg.setOnClickListener(this);
        emailEdit = findViewById(R.id.Email);
        passwordEdit =findViewById(R.id.password);

    }


    //starting the app.
    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null)
        {
            moveToMenu();
        }

    }
    //updating the ui.
    private void updateUI(FirebaseUser user) {
    }
    //reloading the intent.
    private void reload() {

    }
    //moving to the menu.
    private void moveToMenu()
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
        finish();
    }
//    private void moveToSaveUser()
//    {
//        Intent intent = new Intent(this, SaveUsersActivity.class);
//        startActivity(intent);
//        finish();
//    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //in charge of what happens when clicking on a button.
    public void onClick(View view) {
        if (view == reg) {
            email = emailEdit.getText().toString();
            password = passwordEdit.getText().toString();
            Log.d(TAG,"Create User Email:" + email + " Password: " + password);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LogInActivity.this, SaveUser.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
        if (view == login)
        {
            email = emailEdit.getText().toString();
            password = passwordEdit.getText().toString();
            Log.d(TAG, "login ");
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                moveToMenu();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }

    }
    //going to login.
    public void goToLogin(){
        email = emailEdit.getText().toString();
        password = passwordEdit.getText().toString();
        Log.d(TAG, "login ");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            moveToMenu();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
}

