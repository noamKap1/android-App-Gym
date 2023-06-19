package com.example.hgym;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

//adapter for records for the list view.
public class audioAdapter extends ArrayAdapter <Record> {

    //builder for audio adapter.
    public audioAdapter (@NonNull Context context, @NonNull ArrayList<Record> objects) {
        super(context, 0, objects);
    }


    //how the adapter adapts the record for the list view item.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.make, parent, false);
        }

        Record game = getItem(position);

        TextView tvName = (TextView) listItemView.findViewById(R.id.tvName);
        TextView tvExplanation = (TextView) listItemView.findViewById(R.id.tvExplanation);

        String gameId = game.getPathName();
        boolean flag=true;
        String str = "",date ="";
        int index=0,note=0;
        for(int i=0;i<gameId.length();i++){
            if(flag){
                if((gameId.charAt(i) == '.') && (gameId.charAt(i+1)=='3') && (gameId.charAt(i+2) == 'g') && (gameId.charAt(i+3) =='p')) {
                    str += ".3gp";
                    flag=false;
                } else {
                    str += gameId.charAt(i);
                }
                note++;
            } else {
                date += gameId.charAt(i);
            }
            if(gameId.charAt(i)=='s' && gameId.charAt(i+1)=='t' && gameId.charAt(i+2)=='o' && gameId.charAt(i+3)=='p'){
                index=i;
            }
        }
        Log.d("note:"+note +",index"+index, ""+gameId.length());
        String forRealDate = gameId.substring(note);
        int len = forRealDate.length();
        Log.d("length", ""+len);
        forRealDate = forRealDate.substring(0,len-8);
        forRealDate =forRealDate.substring(3);
        tvName.setText(forRealDate);
        tvExplanation.setText(""+game.getDuration() +"ms");


        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), Player.class);
                intent.putExtra("ID", gameId);
                Log.d(gameId,"dsa");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                Toast.makeText(getContext(), "work", Toast.LENGTH_SHORT).show();

            }
        });


        return listItemView;
    }
}