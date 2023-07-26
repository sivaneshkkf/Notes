package com.example.my_notes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_notes.Utils.OnItemViewClickListener;
import com.example.my_notes.databinding.RecyclerNotesBinding;
import com.example.my_notes.databinding.RecyclerTaskBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Task_Adapter extends RecyclerView.Adapter<Task_Adapter.AppViewHolder> {
    Context context;
//    ArrayList<String> list;
    List<JSONObject> list;

    OnItemViewClickListener onItemViewClickListener;

    public Task_Adapter(Context context, List<JSONObject> list, OnItemViewClickListener onItemViewClickListener) {
        this.context = context;
        this.list = list;
        this.onItemViewClickListener=onItemViewClickListener;
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

//        holder.binding.titleTxt.setText(list.get(position));

        JSONObject object = list.get(position);

        try {
            holder.binding.titleTxt.setText(object.getString("Title"));
            // holder.binding.tvMail.setText(object.getString("CustomerEmail"));
            holder.binding.subjectTxt.setText(object.getString("subject"));
            holder.binding.task.setText(object.getString("description"));



        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onItemViewClickListener.onClick(v,holder.getAdapterPosition());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


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
