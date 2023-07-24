package com.example.my_notes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_notes.databinding.RecyclerNotesBinding;

import java.util.ArrayList;

public class Notes_Adapter extends RecyclerView.Adapter<Notes_Adapter.AppViewHolder> {

    Context context;
    ArrayList<String> list;

    public Notes_Adapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        RecyclerNotesBinding binding=RecyclerNotesBinding.inflate(inflater,parent,false);
        return new Notes_Adapter.AppViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        int pos =position;

        holder.binding.titleTxt.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder{

        RecyclerNotesBinding binding;

        public AppViewHolder(@NonNull View itemView, RecyclerNotesBinding binding) {
            super(itemView);
            this.binding=binding;
        }
    }
}
