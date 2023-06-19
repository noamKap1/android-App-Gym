package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hgym.databinding.ActivityAlarmBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
//intent that in charge of creating setting and canceling the notification.
public class alarmActivity extends AppCompatActivity implements View.OnClickListener {
    private Button selectedTimeBtn,cancelAlarmBtn,setAlarmBtn,returnMenu;
    private MaterialTimePicker picker;
    private TextView textView;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    //creating the activity and layout.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        selectedTimeBtn = findViewById(R.id.selectedTimeBtn);
        selectedTimeBtn.setOnClickListener(this);
        cancelAlarmBtn = findViewById(R.id.CancelAlarmBtn);
        cancelAlarmBtn.setOnClickListener(this);
        setAlarmBtn = findViewById(R.id.setAlarmBtn);
        setAlarmBtn.setOnClickListener(this);
        returnMenu = findViewById(R.id.returnToMenu);
        returnMenu.setOnClickListener(this);
        createNotificationChannel();
        textView = findViewById(R.id.selectedTime);
    }

    //creating notification channel for the notification.
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "remainder for a workout";
            String description = "you have a workout soon!";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("nhksd", name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.d("notification","created");
        }
    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    //in charge of whats happens when pressing on a button.
    @Override
    public void onClick(View v){
        if(v == selectedTimeBtn){
            showTimePicker();

        }
        if(v == setAlarmBtn){
            setAlarm();
        }
        if(v==cancelAlarmBtn){
            cancelAlarm();
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
                                        Intent intent = new Intent(getBaseContext(), ShowGroups.class);
                                        startActivity(intent);

                                    } else if(roll.equals("trainer")){
                                        Intent intent = new Intent(getBaseContext(),ShowGroupsTrainer.class);
                                        startActivity(intent);
                                    }
                                }
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        if(v==returnMenu){
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

    //canceling the alarm.
    public void cancelAlarm(){
        Intent intent = new Intent(this,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this,"Alarm Cancelled",Toast.LENGTH_SHORT).show();
        Log.d("alarm","cancelled");
    }
    //setting the alarm.
    public void setAlarm(){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this,"Alarm set Successfully",Toast.LENGTH_SHORT).show();
        Log.d("alarm","set");
    }
    String year,day,month;
    //showing the time for the alarm, the user picks.
    private void showTimePicker(){
        //day = intent.getStringExtra("day");
        //month = intent.getStringExtra("month");
        //year = intent.getStringExtra("year");

        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm time (set to the day of the workout)")
                .build();
        picker.show(getSupportFragmentManager(),"nhksd");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(picker.getHour()>12){
                    //25.05.2005 08 : 45 PM
                    int hour = picker.getHour();
                    Log.d("hour",""+hour);
                    textView.setText(hour+" : "+picker.getMinute()+ " PM");
                } else {
                    int hour = picker.getHour();
                    int minute = picker.getMinute();
                    Log.d("minute",""+minute);
                    if(picker.getMinute()<10){
                        textView.setText(""+hour+" : 0"+ picker.getMinute() + " AM");

                    }else {
                        textView.setText("" + hour + " : " + picker.getMinute() + " AM");
                    }
                }
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar.set(Calendar.MINUTE,picker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                //calander.set(Calendar.YEAR,year);
                //calendar.set(Calendar.MONTH,month);
                //calendar.set(Calendar.DAY_OF_MONTH,day);
            }
        });
    }
}