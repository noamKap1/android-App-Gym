package com.example.hgym;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//intent for a fragment which shows to non weight exercises.
public class BlankFragmentNon extends Fragment {
    View view;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView lvMethods;
    ArrayList<nonEquipmentExercise> alMethods;
    //in charge of how to fragment looks.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blank_non, container, false);
        db = FirebaseFirestore.getInstance();
        lvMethods = view.findViewById(R.id.NonEqEx);
        alMethods = new ArrayList<>();

        loadGamesToListView();
        return view;
    }
    //loading the exercises to the list view.
    private void loadGamesToListView() {
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
                                alMethods = data.getNonEquipmentExercise();
                                System.out.println(alMethods.size());
                                for(int i=0;i<alMethods.size();i++){
                                    System.out.println(alMethods.get(i).getName());
                                }
                                makeAdapter adapter = new makeAdapter(getActivity().getBaseContext(), alMethods);
                                lvMethods.setAdapter(adapter);
                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}