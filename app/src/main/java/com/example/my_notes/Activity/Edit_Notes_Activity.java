package com.example.my_notes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.R;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.databinding.ActivityEditNotesBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_Notes_Activity extends AppCompatActivity {
ActivityEditNotesBinding binding;
    Activity activity;

    String taskId,title,subject,desc;
    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                if(apiStatus==APIStatus.SUCCESS){
                    if(tag.equalsIgnoreCase("updateNotes")){
                        if(response.getBoolean("status")){
                            JSONObject object=response.getJSONObject("");

                        }else{
                            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                        }
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
        binding = ActivityEditNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = binding.edtTitle.getText().toString();
        subject = binding.edtSubject.getText().toString();
        desc = binding.edtNotes.getText().toString();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL + "updateNotes", map, "updateNotes", new Bundle(), apiCallbacks);
    }
}