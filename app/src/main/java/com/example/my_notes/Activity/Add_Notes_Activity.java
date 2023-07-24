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
import com.example.my_notes.databinding.ActivityAddNotesBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Add_Notes_Activity extends AppCompatActivity {
ActivityAddNotesBinding binding;
Activity activity;

    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                if (apiStatus==APIStatus.SUCCESS){
                    if(tag.equalsIgnoreCase("createNotes")){
                        if (response.getBoolean("status")){
                            JSONObject object=response.getJSONObject("");
                        }else{
                            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                        }
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