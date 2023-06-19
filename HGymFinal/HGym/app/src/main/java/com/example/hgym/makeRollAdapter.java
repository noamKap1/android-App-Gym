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
//adapter for a trainer.
public class makeRollAdapter extends ArrayAdapter <trainerForListView> {

    //creating the adapter.
    public makeRollAdapter(@NonNull Context context, @NonNull ArrayList<trainerForListView> objects) {
        super(context, 0, objects);
    }

    //in charge of how the item looks in a list view.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.makeroll, parent, false);
        }

        trainerForListView game = getItem(position);
        TextView firstName = (TextView) listItemView.findViewById(R.id.firstName);
        TextView lastName = (TextView) listItemView.findViewById(R.id.lastName);
        TextView gmail = (TextView) listItemView.findViewById(R.id.gmail);
        firstName.setText(game.getFirstName());
        lastName.setText(game.getLastName());
        gmail.setText(game.getEmail());



        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("Do you wish to continue?")
                        .setTitle("Participant profile");

                builder.setPositiveButton("yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Worked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), showParticipent.class);
                        String gameId = game.getEmail();
                        intent.putExtra("ID", gameId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                        Toast.makeText(getContext(), "work", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();

            }
        });


        return listItemView;
    }
}
