package com.example.my_notes.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.Adapter.Notes_Adapter;
import com.example.my_notes.Adapter.Task_Adapter;
import com.example.my_notes.R;
import com.example.my_notes.Utils.CommonFunctions;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.databinding.FragmentNotesBinding;
import com.example.my_notes.databinding.FragmentTaskBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Task_Fragment extends Fragment {

  FragmentTaskBinding binding;

  Task_Adapter task_adapter;
 /* ArrayList<String>list;*/
 SharedPreferences insertdata;

    SharedPreferences.Editor editor;

    String userId;
    List<JSONObject> list = new ArrayList<>();
    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                if(tag.equalsIgnoreCase("taskList")){
                    if (response.getBoolean("status")){
                        list.clear();
                        CommonFunctions.setJSONArray(response,"msg",list,task_adapter);
                    }else{
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater,container,false);


        insertdata= getActivity().getSharedPreferences("userID", Context.MODE_PRIVATE);
        editor=insertdata.edit();
        userId=String.valueOf(insertdata.getInt("userid",0));
        callapi();
        RecyclerView recyclerView = binding.taskRecyclerview;
        task_adapter = new Task_Adapter(getActivity(),list);
        recyclerView.setAdapter(task_adapter);
        return binding.getRoot();
    }

    public void callapi(){
        Map<String,String> map=new HashMap<>();
        map.put("userid",userId);
        NetworkController.getInstance().callApiPost(getActivity(), APPConstants.MAIN_URL+"taskList",map,"taskList",new Bundle(),apiCallbacks);
    }
}