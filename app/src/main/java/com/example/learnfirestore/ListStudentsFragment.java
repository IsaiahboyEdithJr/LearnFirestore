package com.example.learnfirestore;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ListStudentsFragment extends Fragment {

    private ImageView developerIV;
    private RecyclerView studentsRV;

    public ListStudentsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_students, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        developerIV = view.findViewById(R.id.developers_iv);
        developerIV.setBackgroundResource(R.drawable.card_background);
        studentsRV = view.findViewById(R.id.students_rv);
        AnimationDrawable animation = (AnimationDrawable) developerIV.getBackground();
        animation.start();
    }
}
