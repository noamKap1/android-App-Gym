package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
//intent for creating a group
public class CreateGroup extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        dateE = findViewById(R.id.date);
        hourE = findViewById(R.id.hour);
//        minuteE =findViewById(R.id.minute);
        expE = findViewById(R.id.exp);
//        gmailE = findViewById(R.id.gmail);
        durationE = findViewById(R.id.duration);
        btnDate = findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);
        btnTime = findViewById(R.id.btnHour);
        btnTime.setOnClickListener(this);
        btnSave = findViewById(R.id.btnSave);
        btnReturn = findViewById(R.id.btnReturn);
        btnSave.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        mStorage = FirebaseStorage.getInstance().getReference();
    }
    private CollectionReference audioColl;
    EditText dateE, hourE,minuteE, gmailE, durationE, expE;
    MaterialDatePicker materialDatePicker;
    Button btnSave, btnReturn;
    ImageView btnDate, btnTime;
    private Calendar calendar;
    int dayI,monthI,yearI;
    private MaterialTimePicker picker;
    private StorageReference mStorage;
    String hour,date, minute, gmail, duration, exp;
    boolean isSaved = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    //giving a random item path.
    public String itemPath() {
        String path = "";
        for (int i = 0; i < 20; i++) {
            path = path + String.valueOf(Math.round(Math.random() * (10 - 1) + 1));
        }
        Log.d("PATH", path);
        return path;
    }
    //show the date picker dialog.
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    //setting the date after the picking.
    public void onDateSet(DatePicker view,int year,int month,int dayOfMonth){
        dateE.setText(dayOfMonth + "/" + month + "/" + year);
        dayI = dayOfMonth;
        monthI = month;
        yearI = year;
    }
    //show the time picker.
    private void showTimePicker(){
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select hour and minute for your workout")
                .build();
        picker.show(getSupportFragmentManager(),"nhksd");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(picker.getHour()>12){
                    //25.05.2005 08 : 45 PM
                    int hour = picker.getHour();
                    int minute = picker.getMinute();
                    Log.d("minute",""+minute);
                    if(picker.getMinute()<10){
                        hourE.setText(hour+" : 0"+ picker.getMinute());

                    }else {
                        hourE.setText(hour + " : " + picker.getMinute());
                    }
                } else {
                    int hour = picker.getHour();
                    int minute = picker.getMinute();
                    Log.d("minute",""+minute);
                    if(picker.getMinute()<10){
                        hourE.setText(""+hour+" : 0"+ picker.getMinute());

                    }else {
                        hourE.setText("" + hour + " : " + picker.getMinute());
                    }
                }
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar.set(Calendar.MINUTE,picker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
            }
        });
    }
    //in charge of what happens when clicking a button.
    @Override
    public void onClick(View v) {
        if(v == btnDate){
            showDatePickerDialog();
        }
        if(v == btnTime){
            showTimePicker();
        }
        if(v == btnSave) {

            gmail = currentUser.getEmail();
            audioColl = db.collection("Groups" + gmail);
            int hour1 = calendar.getTime().getHours();
            int minute1 = calendar.getTime().getMinutes();
            String minute2 = ""+minute1;
            if(minute1<10){
                minute2 = "0"+minute2;
            }
            String hour2 = ""+hour1;
            if(hour1<10){
                hour2 = "0"+hour1;
            }
            Log.d("hour,minute",hour1+","+minute1);
            date = dateE.getText().toString();
            Log.d("date",date);
            String date1="",date2="";
            date1 = date.substring(0,2);
//            if(Integer.parseInt(date1.substring(0,1))<10){
//                date1 = "0" +date.substring(0,2);
//            }
            String c1 = date1.substring(0,1);
            if(c1.equals("/")){
                date1 = "0" + date1.substring(1,2);
            }
            date2= date.substring(2);
            Log.d("date2",date2);
            String c2 = date2.substring(2,3);
            if(c2.equals("/")){
                date2 = date2.substring(0,1) + "0" + date2.substring(1);
            }
//            if(Integer.parseInt(date2.substring(1,3))<10){
//                date2 = "0" +date.substring(2);
//            }
            Log.d("date1",date1);
            Log.d("date2",date2);
            Log.d("date1,date2",date1+date2);
            date = date1+date2;
            Log.d("foreealdate",date);
            date.replace('/','.');
            date.replaceAll("/",".");
            date = date.substring(0,2)+'.'+date.substring(3,5)+'.'+date.substring(6);
            Log.d("yes",date);
            duration = durationE.getText().toString();

            date1 = date.substring(0,5);
            date2 = date.substring(6);
            Log.d("date1,date2",date1+date2);

            int year = yearI;
            int month = monthI;
            int day = dayI;
            exp = expE.getText().toString();
            Date currentTime = Calendar.getInstance().getTime();
            int ref = Integer.parseInt((currentTime).toString().substring(8,10));
            if(year >= (currentTime).getYear()){
                if(month >= (currentTime).getMonth() || year > (currentTime).getYear()){
                    if(day >= ref || month > (currentTime).getMonth() || year > (currentTime).getYear()){
                        Log.d((date.substring(0,2)),"now:"+ref);
                        if(hour1 >= (currentTime).getHours() || day > ref || month > (currentTime).getMonth() || year > (currentTime).getYear()){
                            if(minute1 >= (currentTime).getMinutes() || hour1 > (currentTime).getHours() || day>ref || month>(currentTime).getMonth() || year > (currentTime).getYear()){
                                Log.d("date",currentTime.toString());
                                if(!duration.isEmpty() && !duration.equals(null)){
                                    group g = new group(date,exp,hour2,minute2,Integer.parseInt(duration),gmail);
                                    db.collection("Groups"+gmail).document(gmail+date+hour2+minute2).set(g);
                                    Intent intent = new Intent(this, ShowGroupsTrainer.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(CreateGroup.this, "please enter a correct duration", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this,CreateGroup.class);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(CreateGroup.this, "please enter a correct date", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this,CreateGroup.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(CreateGroup.this, "please enter a correct date", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this,CreateGroup.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(CreateGroup.this, "please enter a correct date", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this,CreateGroup.class);
                        startActivity(intent);
                    }
                } else{
                    Toast.makeText(CreateGroup.this, "please enter a correct date", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,CreateGroup.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(CreateGroup.this, "please enter a correct date", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,CreateGroup.class);
                startActivity(intent);
            }
        }
        if(v == btnReturn){
            Intent intent = new Intent(this, ShowGroupsTrainer.class);
            startActivity(intent);
        }
    }
}