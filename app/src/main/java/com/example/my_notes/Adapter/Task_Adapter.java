package com.example.my_notes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_notes.databinding.RecyclerNotesBinding;
import com.example.my_notes.databinding.RecyclerTaskBinding;

import java.util.ArrayList;

public class Task_Adapter extends RecyclerView.Adapter<Task_Adapter.AppViewHolder> {
    Context context;
    ArrayList<String> list;

    public Task_Adapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        RecyclerTaskBinding binding=RecyclerTaskBinding.inflate(inflater,parent,false);
        return new Task_Adapter.AppViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {

        holder.binding.titleTxt.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder{

        RecyclerTaskBinding binding;

        public AppViewHolder(@NonNull View itemView, RecyclerTaskBinding binding) {
            super(itemView);
            this.binding=binding;
        }
    }
}
