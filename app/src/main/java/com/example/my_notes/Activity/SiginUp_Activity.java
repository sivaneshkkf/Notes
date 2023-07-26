package com.example.my_notes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.databinding.ActivitySiginUpBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SiginUp_Activity extends AppCompatActivity {
ActivitySiginUpBinding binding;
Activity activity;

String name,mail,password;

    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                    if(tag.equalsIgnoreCase("Registration")){
                        if(response.getBoolean("status")){
                            /*JSONObject object=response.getJSONObject("");*/
                            Intent intent = new Intent( SiginUp_Activity.this, MainActivity.class);
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
        binding = ActivitySiginUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





        binding.createaccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.mail.getText().toString();
                String password=binding.password.getText().toString();

                if(binding.username.getText().toString().isEmpty()){
                    binding.username.setError("Username Field should not be empty");
                }else if(binding.mail.getText().toString().isEmpty() && !isValidEmail(email)){
                    binding.mail.setError("Invalid Password");
                }else if(binding.phone.getText().toString().length()<10){
                    binding.phone.setError("Invalid phone number");
                }else if(binding.password.getText().toString().length()<6){
                    binding.password.setError("Password should contains 6 characters");
                }else if(binding.confirmPassword.getText().toString().length()<6 && binding.confirmPassword.getText().toString().equals(password)){
                    binding.confirmPassword.setError("Confirm Password and password must be same");
                }else{
                    mail = binding.mail.getText().toString();
                    name = binding.username.getText().toString();
                    password = binding.password.getText().toString();
                    callapi();
                }

            }
        });

        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callapi() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("email",mail);
        map.put("password",password);
        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL + "Registration", map, "Registration", new Bundle(), apiCallbacks);
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}