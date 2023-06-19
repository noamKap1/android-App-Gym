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
//adapter for a group.
public class groupAdapter extends ArrayAdapter <group> {

    //a builder for the group adapter.
    public groupAdapter (@NonNull Context context, @NonNull ArrayList<group> objects) {
        super(context, 0, objects);
    }


    //in charge if the view of the list view item.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.make, parent, false);
        }
        group game = getItem(position);
        TextView tvName = (TextView) listItemView.findViewById(R.id.tvName);
        TextView tvExplanation = (TextView) listItemView.findViewById(R.id.tvExplanation);
        tvName.setText(game.getDate() + " " + game.getHour() + ":" + game.getMinute() + ", duration: " + game.getDuration());
        tvExplanation.setText(game.getEmailOfTrainer());
        String gameId = game.getEmailOfTrainer();
        String hour = game.getHour();
        String date = game.getDate();
        String minute = game.getMinute();
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowOneGroup.class);
                intent.putExtra("gameId", gameId);
                Log.d(gameId,"dsa");
                intent.putExtra("date", date);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                Toast.makeText(getContext(), "work", Toast.LENGTH_SHORT).show();
            }
        });


        return listItemView;
    }
}