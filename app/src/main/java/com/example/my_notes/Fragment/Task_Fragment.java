package com.example.my_notes.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.Activity.Add_Task_Activity;
import com.example.my_notes.Activity.Edit_Task_Activity;
import com.example.my_notes.Adapter.Notes_Adapter;
import com.example.my_notes.Adapter.Task_Adapter;
import com.example.my_notes.R;
import com.example.my_notes.Utils.CommonFunctions;
import com.example.my_notes.Utils.DialogUtils;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.Utils.OnItemViewClickListener;
import com.example.my_notes.databinding.FragmentNotesBinding;
import com.example.my_notes.databinding.FragmentTaskBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Task_Fragment extends Fragment {

  FragmentTaskBinding binding;

  Task_Adapter task_adapter;
    AlertDialog dialogLoading;
    String taskid,title,subject,tasks;

    String userID = "";


    SharedPreferences insertdata;
    SharedPreferences.Editor editor;


    ArrayList<JSONObject> list = new ArrayList<>();

    APICallbacks apiCallbacks = new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            DialogUtils.dismissLoading(dialogLoading, null, binding.swiperefresh);
            try {
                if (tag.equalsIgnoreCase("taskList")) {
                    if (response.getBoolean("status")) {
                        list.clear();
                        CommonFunctions.setJSONArray(response, "msg", list, task_adapter);
                        task_adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            if (tag.equalsIgnoreCase("deleteTask")) {
                try {
                    if (response.getBoolean("status")) {
                        task_adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    public Task_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        insertdata = getContext().getSharedPreferences("userID", Context.MODE_PRIVATE);
        editor = insertdata.edit();
        userID = String.valueOf(insertdata.getInt("userid", 0));
        /* Toast.makeText(getActivity(), "userid: " + userID, Toast.LENGTH_SHORT).show();*/

        callapi(userID);

        task_adapter = new Task_Adapter(getActivity(), list, new OnItemViewClickListener() {
            @Override
            public void onClick(View v, int i) throws JSONException {
                JSONObject object=list.get(i);
                taskid=object.getString("taskid");
                title=object.getString("title");
                subject=object.getString("subject");
                tasks=object.getString("description");

                if (v.getId() == R.id.delete) {
                    binding.swiperefresh.setRefreshing(true);
                    deletetaskApi(taskid);
                    Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                    task_adapter.notifyDataSetChanged();
                    callapi(userID);
                }

                if(v.getId()==R.id.edit){
                    Bundle bundle=new Bundle();
                    Intent intent=new Intent(getActivity(), Edit_Task_Activity.class);
                    bundle.putString("taskid",taskid);
                    bundle.putString("title",title);
                    bundle.putString("subject",subject);
                    bundle.putString("description",tasks);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                callapi(userID);
            }
        });
        binding.taskRecyclerview.setAdapter(task_adapter);

        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callapi(userID);
            }
        });


    }

    public void callapi(String userID) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", userID);

        NetworkController.getInstance().callApiPost(getActivity(), APPConstants.MAIN_URL + "taskList", map, "taskList", new Bundle(), apiCallbacks);
    }

    public void deletetaskApi(String taskid) {
        Map<String, String> map = new HashMap<>();
        map.put("taskid", taskid);

        NetworkController.getInstance().callApiPost(getActivity(), APPConstants.MAIN_URL + "deleteTask", map, "deleteTask", new Bundle(), apiCallbacks);
    }
}