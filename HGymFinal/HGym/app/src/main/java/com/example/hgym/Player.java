package com.example.hgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
//intent for showing a recording.
public class Player extends AppCompatActivity implements View.OnClickListener{
    private TextView name,time,duration;
    private Button returnBut, playBtn;
    String path;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fDB = FirebaseFirestore.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private MediaPlayer player;
    FirebaseStorage storage;
    StorageReference storageRef, recordRef;
    Intent intent;
    String gameId;
    //creating the intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        intent = getIntent();
        gameId = intent.getStringExtra("ID");
        String str = "",date = "";
        boolean flag = true;
        int index=0;
        for(int i=0;i<gameId.length();i++){
            if(flag){
                if((gameId.charAt(i) == '.') && (gameId.charAt(i+1)=='3') && (gameId.charAt(i+2) == 'g') && (gameId.charAt(i+3) =='p')) {
                    str += ".3gp";
                    flag=false;
                } else {
                    str += gameId.charAt(i);
                }
            } else {
                date += gameId.charAt(i);
            }
            if(gameId.charAt(i)=='s' && gameId.charAt(i+1)=='t' && gameId.charAt(i+2)=='o' && gameId.charAt(i+3)=='p'){
                index=i;
            }
        }
        path = str;
        Log.d(gameId,"nice");
        Log.d(date,"date");
        String forRealDate;
        index -= gameId.length();
        index +=date.length();
        String durationOfRecord;
        Log.d(""+index, ""+gameId.length());
        forRealDate = date.substring(3,index);
        durationOfRecord = date.substring(index);
        durationOfRecord = durationOfRecord.substring(4);
        name = (TextView) findViewById(R.id.pathName);
        name.setText("path in storage: "+path);
        duration = (TextView) findViewById(R.id.duration);
        time = (TextView)findViewById(R.id.time);
        time.setText(forRealDate);
        duration.setText(durationOfRecord + "ms");
        returnBut = findViewById(R.id.ret);
        returnBut.setOnClickListener(this);
        playBtn = findViewById(R.id.play);
        playBtn.setOnClickListener(this);
//        recordRef = storageRef.child(path);
        //Log.d("tag2", path);

        //Glide.with(pro)
           //     .load(imgRef)
         //       .diskCacheStrategy(DiskCacheStrategy.NONE)
        //        .into(photo);
        //storageRef.child(path).downloadUrl.addOnSuccessListener({
           //     val mediaPlayer = MediaPlayer()
           //     mediaPlayer.setDataSource(it.toString())
          //      mediaPlayer.setOnPreparedListener { player ->
         //       player.start()
        //}
       //         mediaPlayer.prepareAsync()
        //});
    }
    //start playing the record.
    private void startPlaying() {
        player = new MediaPlayer();
        try {
            Log.d("sssdadadsadasdasd1", gameId);
            Log.d("damsoidaio", storageRef.child(path).getDownloadUrl().toString());
            player.setDataSource(path);
            player.prepare();
            player.start();
            Log.d("sssdadadsadasdasd", "ss");
        } catch (IOException e) {
           // Log.e(LOG_TAG, "prepare() failed");
        }
    }
    //in charge of what happens when clicking on a button.
    @Override
    public void onClick(View v) {
        if (v == returnBut) {

            Intent intent = new Intent(this, AudioRecordActivity.class);
            startActivity(intent);

        }
        if( v==playBtn){
            startPlaying();
        }
    }
}