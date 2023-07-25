package com.example.my_notes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.Utils.OnItemViewClickListener;
import com.example.my_notes.databinding.RecyclerNotesBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notes_Adapter extends RecyclerView.Adapter<Notes_Adapter.AppViewHolder> {

    Activity activity;
//    ArrayList<String> lists;
    List<JSONObject> list;
    ArrayList<Integer> notesidlist=new ArrayList<>();
    private String notesid;

    OnItemViewClickListener onItemViewClickListener;

    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                if(tag.equalsIgnoreCase("deleteNotes")){
                    if(response.getBoolean("status")){

                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public Notes_Adapter(Activity activity, List<JSONObject> list,OnItemViewClickListener onItemViewClickListener) {
        this.activity = activity;
        this.list = list;
        this.onItemViewClickListener=onItemViewClickListener;
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
            holder.binding.titleTxt.setText(object.getString("title"));
            // holder.binding.tvMail.setText(object.getString("CustomerEmail"));
            holder.binding.subjectTxt.setText(object.getString("subject"));
            holder.binding.notes.setText(object.getString("description"));
            notesidlist.add(object.getInt("notesid"));

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
                notesid=String.valueOf(notesidlist.get(pos));
                callapi();
                notifyDataSetChanged();
            }
        });
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

    public void callapi(){
        Map<String,String> map=new HashMap<>();
        map.put("notesid",notesid);

        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL+"deleteNotes",map,"deleteNotes",new Bundle(),apiCallbacks);
    }
}
