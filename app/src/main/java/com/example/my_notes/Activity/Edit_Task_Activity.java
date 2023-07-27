package com.example.my_notes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.databinding.ActivityEditTaskBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_Task_Activity extends AppCompatActivity {
ActivityEditTaskBinding binding;
    Activity activity;

    String taskId,title,subject,desc;
    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                    if(tag.equalsIgnoreCase("updateTask")){
                        if(response.getBoolean("status")){
                            Intent intent=new Intent(Edit_Task_Activity.this, MainActivity.class);
                            intent.putExtra("activity","Edit_Task_Activity");
                            startActivity(intent);
                        }else{
                            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle=getIntent().getExtras();

        binding.edtTitle.setText(bundle.getString("title"));
        binding.edtSubject.setText(bundle.getString("subject"));
        binding.edtTask.setText(bundle.getString("description"));


        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskId=bundle.getString("taskid");
                title = binding.edtTitle.getText().toString();
                subject = binding.edtSubject.getText().toString();
                desc = binding.edtTask.getText().toString();
                callapi();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Edit_Task_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void callapi() {
        Map<String, String> map = new HashMap<>();
        map.put("taskid", taskId);
        map.put("Title", title);
        map.put("Subject", subject);
        map.put("Desc", desc);
        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL + "updateTask", map, "updateTask", new Bundle(), apiCallbacks);
    }
}