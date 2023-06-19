package com.example.hgym;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
//intent for creating an adapter for an object equipment exercise.
public class makeAdapterPos extends ArrayAdapter <equipmentExercise> {

    //creating the adapter.
    public makeAdapterPos (@NonNull Context context, @NonNull ArrayList<equipmentExercise> objects) {
        super(context, 0, objects);
    }


    //in charge of how the item is being shown in the list view.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.make, parent, false);
        }

        exercise game = getItem(position);

        TextView tvName = (TextView) listItemView.findViewById(R.id.tvName);
        TextView tvExplanation = (TextView) listItemView.findViewById(R.id.tvExplanation);
        String gameId= game.getName();
        tvName.setText(game.getName());
        tvExplanation.setText(game.getExplanation());


        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), showExercises.class);
                intent.putExtra("ID", gameId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                Toast.makeText(getContext(), "work", Toast.LENGTH_SHORT).show();

            }
        });


        return listItemView;
    }

}