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
import com.example.my_notes.databinding.ActivityAddNotesBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Add_Notes_Activity extends AppCompatActivity {
ActivityAddNotesBinding binding;
Activity activity;
SharedPreferences insertdata;
SharedPreferences.Editor editor;

    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                    if(tag.equalsIgnoreCase("createNotes")){
                        if (response.getBoolean("status")){
                            Intent intent=new Intent(Add_Notes_Activity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };
    String userid,Title,Subject,Desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;

        insertdata= getSharedPreferences("userID",MODE_PRIVATE);
        editor=insertdata.edit();

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid=String.valueOf(insertdata.getInt("userid",0));
                Title=binding.edtTitle.getText().toString();
                Subject=binding.edtSubject.getText().toString();
                Desc=binding.edtNotes.getText().toString();
                callapi();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void callapi(){
        Map<String,String> map=new HashMap<>();

        map.put("userid",userid);
        map.put("Title",Title);
        map.put("Subject",Subject);
        map.put("Desc",Desc);

        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL+"createNotes",map,"createNotes",new Bundle(),apiCallbacks);
    }
}