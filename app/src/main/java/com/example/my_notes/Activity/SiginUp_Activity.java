package com.example.my_notes.Activity;

import static com.example.my_notes.Utils.InputValidator.isValidEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.databinding.ActivitySiginUpBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SiginUp_Activity extends AppCompatActivity {
ActivitySiginUpBinding binding;
Activity activity;
String email,password1;

String name,mail,password;

    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            binding.progressCircular.setVisibility(View.GONE);
            try {
                    if(tag.equalsIgnoreCase("Registration")){
                        if(response.getBoolean("status")){
                            binding.tick.setVisibility(View.VISIBLE);
                            Intent intent = new Intent( SiginUp_Activity.this, Login_Activity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(SiginUp_Activity.this, "error", Toast.LENGTH_SHORT).show();
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



        binding.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               hideKeyboard(SiginUp_Activity.this);


            }
        });


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
                }else{
                    password1=s.toString();
                }
            }
        });


        binding.mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString().trim();
                if (!email.isEmpty() && !isValidEmail(email)) {
                    binding.mail.setError("Invalid email address");
                } else {
                    binding.mail.setError(null);
                    email=s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.createaccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.username.getText().toString().isEmpty()){
                    binding.username.setError("Username Field should not be empty");
                }else if (binding.mail.getText().toString().isEmpty() || isValidEmail(email)) {
                    binding.mail.setError("Invalid Mail ID");
                }else if(binding.phone.getText().toString().length()<10 || binding.phone.getText().toString().length()>10){
                    binding.phone.setError("Invalid phone number");
                }else if(binding.password.getText().toString().length()<6){
                    binding.pass.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    binding.password.setError("Password should contains 6 characters");
                }else{
                    mail = binding.mail.getText().toString();
                    name = binding.username.getText().toString();
                    password = binding.password.getText().toString();
                    binding.progressCircular.setVisibility(View.VISIBLE);
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
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

   /* @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        // Close the keyboard when scrolling starts
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            hideKeyboard(this, focusedView);
        }
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }*/

}