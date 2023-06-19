package com.example.hgym;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

//activity for recording audio and hearing and uploading audio.
public class AudioRecordActivity extends AppCompatActivity {
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private ListView lvMethods;
    private ArrayList<Record> method;
    FirebaseStorage storage;
    StorageReference storageRef;
    Task<ListResult> recordRef;
    private RecordButton recordButton = null;
    private MediaRecorder recorder = null;
    private CollectionReference audioColl = db.collection("Audio" + currentUser.getEmail());
    long duration;
    private PlayButton   playButton = null;
    private MediaPlayer   player = null;
    private Date date1,date2;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private StorageReference mStorage;
    private ProgressDialog mProgress;

    //requesting permission for recording audio.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    //recording audio.
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }
    //playing the previous audio.
    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
    //starting to play the audio.
    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            Log.d("sssdadadsadasdasd", fileName);
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }
    //stop playing the audio.
    private void stopPlaying() {
        player.release();
        player = null;
    }
    //start recording the audio.
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        date1 = new Date();
        recorder.start();
    }
    //creating the intent.
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        method = new ArrayList<>();
        mProgress = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest"+itemPath() + ".3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        LinearLayout ll = new LinearLayout(this);
        recordButton = new RecordButton(this);
        ll.addView(recordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        playButton = new PlayButton(this);
        ll.addView(playButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        lvMethods = new ListView(this);
        ll.addView(lvMethods,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0
                ));
        loadGamesToListView();
        Log.d("size:" ,"" + method.size());
        setContentView(ll);
    }
    //showing the list view for the recordings.
    private void loadGamesToListView() {
        String dat = "";
        ArrayList<Record> methods = new ArrayList<>();
        audioColl.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for( QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Record record = documentSnapshot.toObject(Record.class);
                    Log.d("", record.getPathName());
                    methods.add(record);
                    String title = record.getDuration() + record.getPathName();
                    Log.d("","method size" + method.size());
                }
                if(methods.size()!=0) {
                    audioAdapter adapter = new audioAdapter(getBaseContext(), methods);
                    lvMethods.setAdapter(adapter);
                }
            }
        });

        if(methods.size()!=0) {
            audioAdapter adapter = new audioAdapter(getParent().getBaseContext(), methods);
            lvMethods.setAdapter(adapter);
        }
        Log.d("", "Error getting documents: " + method.size());
    }
    //stopping the recording of the audio.
    private void stopRecording() {
        date2 = new Date();
        duration = date2.getTime()-date1.getTime();
        recorder.stop();
        recorder.release();
        recorder = null;
        uploadAudio();
    }
    //giving a random path to the item.
    public String itemPath() {
        String path = "";
        for (int i = 0; i < 20; i++) {
            path = path + String.valueOf(Math.round(Math.random() * (10 - 1) + 1));
        }
        Log.d("PATH", path);
        return path;
    }
    //uploading the audio to firebase.
    private void uploadAudio(){
        mProgress.setMessage("Uploading Audio");
        mProgress.show();
        String itemPath = itemPath();
        itemPath += "new_audio.3gp";
        String fileNameTime = fileName;
        fileNameTime +=  (new Date()).toString();
        fileNameTime += ("stop" + String.valueOf(duration));
        StorageReference filePath = mStorage.child("Audio" + currentUser.getEmail()).child(itemPath);
        Record record = new Record(fileNameTime, duration);
        audioColl.add(record);
        Uri uri = Uri.fromFile(new File(fileName));
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();

            }
        });
    }
    //a class for the record button.
    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }
    // a class for the play button.
    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }


    //stopping the recording of an audio.
    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
    //in charge of showing the menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    //in charge of whats happens when clicking on items from the menu.

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
                    Toast.makeText(AudioRecordActivity.this, "canceled", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();

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
                    Intent intent = new Intent(AudioRecordActivity.this, LogInActivity.class);
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
    public void goToListVIew(){
        Intent intent = new Intent(this, listViewActivity.class);
        startActivity(intent);
    }
}