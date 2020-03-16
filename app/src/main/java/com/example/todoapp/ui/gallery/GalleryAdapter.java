package com.example.todoapp.ui.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;

import java.util.ArrayList;

class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
    ArrayList<String> names = new ArrayList<String>(){{
        add("Bob");
        add("John");
        add("Scarlett");
        add("Annie");
        add("Josh");
        add("Alex");
    }};


    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_gal_viewholder,parent,false);
        return new GalleryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        holder.onBind(names.get(position));

    }

    @Override
    public int getItemCount() {
        return names.size();
    }
}
