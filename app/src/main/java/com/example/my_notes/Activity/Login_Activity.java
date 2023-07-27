package com.example.my_notes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.databinding.ActivityLoginBinding;
import com.example.my_notes.databinding.ActivitySplashBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {
    ActivityLoginBinding binding;
    String mail, password;
    SharedPreferences insertdata;
    SharedPreferences.Editor editor;
    Activity activity;
    APICallbacks apiCallbacks = new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            binding.progressCircular.setVisibility(View.GONE);
            try {
                if (tag.equalsIgnoreCase("login")) {
                    if (response.getBoolean("status")) {

                        binding.tick.setVisibility(View.VISIBLE);
                        JSONObject object = response.getJSONObject("userDetails");
                        int userid = object.getInt("userid");

                        editor.putInt("userid", userid);
                        editor.commit();
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },1500);

                    } else {
                        Toast.makeText(activity, ""+response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(activity, "Please enter valid uername and password", Toast.LENGTH_SHORT).show();
                //  throw new RuntimeException(e);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        insertdata = getSharedPreferences("userID", MODE_PRIVATE);
        editor = insertdata.edit();

        int userid1 = insertdata.getInt("userid", 0);
        /* Toast.makeText(activity, "userid: "+userid1, Toast.LENGTH_SHORT).show();*/
        if (userid1 != 0) {
            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()!=0){
                    binding.pass.setPasswordVisibilityToggleEnabled(true);
                }
            }
        });

        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.username.getText().toString().isEmpty()){
                    binding.username.setError("Invalid Username");
                }else if(binding.password.getText().toString().isEmpty()){
                    binding.pass.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    binding.password.setError("Invalid Password");

                }else {
                    mail = binding.username.getText().toString();
                    password = binding.password.getText().toString();
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    callapi();
                }
            }
        });

        binding.siginup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, SiginUp_Activity.class);
                startActivity(intent);
            }
        });


    }

    private void callapi() {
        Map<String, String> map = new HashMap<>();
        map.put("mail", mail);
        map.put("password", password);
        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL + "login", map, "login", new Bundle(), apiCallbacks);
    }

}