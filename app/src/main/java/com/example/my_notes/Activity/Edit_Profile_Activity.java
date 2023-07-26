package com.example.my_notes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.databinding.ActivityEditProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_Profile_Activity extends AppCompatActivity {
ActivityEditProfileBinding binding;
    Activity activity;

    SharedPreferences insertdata;
    SharedPreferences.Editor editor;
    String userid,name,mail,userregId,password;

    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                if(tag.equalsIgnoreCase("viewProfile")){
                    if (response.getBoolean("status")){
                        JSONObject object=response.getJSONObject("msg");
                        name = object.getString("username");
                        mail = object.getString("useremail");
                        userregId = object.getString("Regid");
                        password =object.getString("userpassword");

                        binding.username.setText(name);
                        binding.id.setText("UserId:"+userregId);
                        binding.mail.setText(mail);
                        binding.password.setText(password);
                        binding.confirmPassword.setText(password);


                    }else{
                        Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (tag.equalsIgnoreCase("UpdateProfile")) {
                try {
                    if (response.getBoolean("status")) {
                        Intent intent=new Intent(Edit_Profile_Activity.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(activity, "updateprofile error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;

        insertdata = getSharedPreferences("userID", Context.MODE_PRIVATE);
        editor=insertdata.edit();
        userid = String.valueOf(insertdata.getInt("userid", 0));

        callapi();


        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.username.getText().toString().isEmpty()){
                    binding.username.setError("Username Field should not be empty");
                }else if(binding.mail.getText().toString().isEmpty()){
                    binding.mail.setError("Invalid Password");
                }else if(binding.password.getText().toString().length()<6){
                    binding.password.setError("Password should contains 6 characters");
                }else if(binding.confirmPassword.getText().toString().length()<6 && binding.confirmPassword.getText().toString().equals(password)){
                    binding.confirmPassword.setError("Confirm Password and password must be same");
                }else{
                    mail = binding.mail.getText().toString();
                    name = binding.username.getText().toString();
                    password = binding.password.getText().toString();

                    updateprofilecallapi();
                }

            }
        });

    }

    public void callapi(){
        Map<String,String> map=new HashMap<>();

        map.put("userid",userid);

        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL+"viewProfile",map,"viewProfile",new Bundle(),apiCallbacks);
    }
    public void updateprofilecallapi(){
        Map<String,String> map=new HashMap<>();

        map.put("userid",userid);
        map.put("name",name);
        map.put("email",mail);
        map.put("password",password);
        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL+"UpdateProfile",map,"UpdateProfile",new Bundle(),apiCallbacks);
    }
}