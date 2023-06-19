package com.example.hgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//activity for the main,not being used.
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn;
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);

        btn.setOnClickListener(this);

    }
    //in charge of what happens when clicking a button.
    @Override
    public void onClick(View v) {
        if (v== btn)
        {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        }
    }

}