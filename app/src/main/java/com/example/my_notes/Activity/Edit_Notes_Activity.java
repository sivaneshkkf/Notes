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
import com.example.my_notes.databinding.ActivityEditNotesBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_Notes_Activity extends AppCompatActivity {
ActivityEditNotesBinding binding;
    Activity activity;

    String notesId,title,subject,desc;
    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {

                    if(tag.equalsIgnoreCase("updateNotes")){
                        if(response.getBoolean("status")){
                            Intent intent=new Intent(Edit_Notes_Activity.this, MainActivity.class);
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
        binding = ActivityEditNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        binding.edtTitle.setText(intent.getStringExtra("title"));
        binding.edtSubject.setText(intent.getStringExtra("subject"));
        binding.edtNotes.setText(intent.getStringExtra("description"));
        notesId = intent.getStringExtra("notesid");


        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtTitle.getText().toString().isEmpty()){
                    binding.edtTitle.setError("the required field should not be empty");
                } else if (binding.edtSubject.getText().toString().isEmpty()) {
                    binding.edtSubject.setError("the required field should not be empty");
                }else if (binding.edtNotes.getText().toString().isEmpty()) {
                    binding.edtNotes.setError("the required field should not be empty");
                }else {
                    title = binding.edtTitle.getText().toString();
                    subject = binding.edtSubject.getText().toString();
                    desc = binding.edtNotes.getText().toString();
                    callapi();
                    finish();
                }

            }
        });



        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callapi() {
        Map<String, String> map = new HashMap<>();
        map.put("notesid", notesId);
        map.put("Title", title);
        map.put("Subject", subject);
        map.put("Desc", desc);
        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL + "updateNotes", map, "updateNotes", new Bundle(), apiCallbacks);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}