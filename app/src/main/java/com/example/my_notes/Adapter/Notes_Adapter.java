package com.example.my_notes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_notes.databinding.RecyclerNotesBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Notes_Adapter extends RecyclerView.Adapter<Notes_Adapter.AppViewHolder> {

    Context context;
//    ArrayList<String> lists;
    List<JSONObject> list;

    public Notes_Adapter(Context context, List<JSONObject> list) {
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

//        holder.binding.titleTxt.setText(lists.get(position));


        JSONObject object = list.get(position);

        try {
            holder.binding.titleTxt.setText(object.getString("Title"));
            // holder.binding.tvMail.setText(object.getString("CustomerEmail"));
            holder.binding.subjectTxt.setText(object.getString("Subject"));
            holder.binding.notes.setText(object.getString("Desc"));


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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
