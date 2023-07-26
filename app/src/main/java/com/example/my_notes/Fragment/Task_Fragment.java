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
    ArrayList<Integer> taskidList=new ArrayList<>();
    ArrayList<String> titleList=new ArrayList<>();
    ArrayList<String> subjectList=new ArrayList<>();
    ArrayList<String> notesList=new ArrayList<>();
    private String taskid;

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
            try {
                if (tag.equalsIgnoreCase("taskList")) {
                    if (response.getBoolean("status")) {
                        DialogUtils.dismissLoading(dialogLoading, null, binding.swiperefresh);
                        list.clear();
                        taskidList.clear();
                        titleList.clear();
                        subjectList.clear();
                        notesList.clear();
                        JSONArray array=response.getJSONArray("msg");

                        for(int i=0;i<array.length();i++){
                            JSONObject object=array.getJSONObject(i);
                            taskidList.add(object.getInt("taskid"));
                            titleList.add(object.getString("title"));
                            subjectList.add(object.getString("subject"));
                            notesList.add(object.getString("description"));
                        }
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

        callapi();

        task_adapter = new Task_Adapter(getActivity(), list, new OnItemViewClickListener() {
            @Override
            public void onClick(View v, int i) throws JSONException {
                if (v.getId() == R.id.delete) {
                    binding.swiperefresh.setRefreshing(true);
                    Toast.makeText(getActivity(), "array: " + taskidList.toString(), Toast.LENGTH_SHORT).show();
                    taskid = String.valueOf(taskidList.get(i));
                    Toast.makeText(getActivity(), "taskid: " + taskid, Toast.LENGTH_SHORT).show();
                    deletetaskApi();
                    task_adapter.notifyDataSetChanged();
                    callapi();
                }

                if(v.getId()==R.id.edit){
                    Bundle bundle=new Bundle();
                    Intent intent=new Intent(getActivity(), Edit_Task_Activity.class);
                    bundle.putString("activity","taskFragment");
                    bundle.putInt("taskid",taskidList.get(i));
                    bundle.putString("title",titleList.get(i));
                    bundle.putString("subject",subjectList.get(i));
                    bundle.putString("description",notesList.get(i));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        binding.taskRecyclerview.setAdapter(task_adapter);

        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callapi();
            }
        });


    }

    public void callapi() {
        Map<String, String> map = new HashMap<>();
        map.put("userid", userID);

        NetworkController.getInstance().callApiPost(getActivity(), APPConstants.MAIN_URL + "taskList", map, "taskList", new Bundle(), apiCallbacks);
    }

    public void deletetaskApi() {
        Map<String, String> map = new HashMap<>();
        map.put("taskid", taskid);

        NetworkController.getInstance().callApiPost(getActivity(), APPConstants.MAIN_URL + "deleteTask", map, "deleteTask", new Bundle(), apiCallbacks);
    }
}