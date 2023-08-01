package com.example.my_notes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.databinding.ActivityAddTaskBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Add_Task_Activity extends AppCompatActivity {
ActivityAddTaskBinding binding;
    Activity activity;
    SharedPreferences insertData;
    SharedPreferences.Editor editor;
    String userId,title,subject,desc;
    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                    if(tag.equalsIgnoreCase("createTask")){
                        if(response.getBoolean("status")){
                            Intent intent=new Intent(Add_Task_Activity.this, MainActivity.class);
                            intent.putExtra("activity","Add_Task_Activity");
                            startActivity(intent);
                            finish();

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
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        insertData=getSharedPreferences("userID",MODE_PRIVATE);
        editor=insertData.edit();

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId=String.valueOf(insertData.getInt("userid",0));
                title = binding.edtTitle.getText().toString();
                subject = binding.edtSubject.getText().toString();
                desc = binding.edtTask.getText().toString();
                callapi();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                finish();
            }
        });
    }

    private void callapi() {
        Map<String, String> map = new HashMap<>();
        map.put("userid", userId);
        map.put("Title", title);
        map.put("Subject", subject);
        map.put("Desc", desc);
        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL + "createTask", map, "createTask", new Bundle(), apiCallbacks);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}