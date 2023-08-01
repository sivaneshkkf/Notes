package com.example.my_notes.Activity;

import static com.example.my_notes.Utils.InputValidator.isValidEmail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_Profile_Activity extends AppCompatActivity {
ActivityEditProfileBinding binding;
    Activity activity;

    SharedPreferences insertdata;
    ImagePicker imagePicker;

    String email,password1;
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

        insertdata= getSharedPreferences("img",MODE_PRIVATE);
        insertdata = getSharedPreferences("userID", Context.MODE_PRIVATE);
        editor=insertdata.edit();
        userid = String.valueOf(insertdata.getInt("userid", 0));
        Uri uri=Uri.parse(insertdata.getString("propic",""));
        binding.profileimg.setImageURI(uri);

        callapi();

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker = new ImagePicker(activity,null, imageUri ->{

                    editor.putString("propic", imageUri.toString());
                    editor.commit();
                    Uri uri = Uri.parse(insertdata.getString("propic", ""));
                    binding.profileimg.setImageURI(uri);
                }).setWithImageCrop(1,1 );
                imagePicker.choosePicture(true );
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


        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(binding.username.getText().toString().isEmpty()){
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
                }*/

                if(binding.username.getText().toString().isEmpty()){
                    binding.username.setError("Username Field should not be empty");
                }else if (binding.mail.getText().toString().isEmpty() || isValidEmail(email)) {
                    binding.mail.setError("Invalid Mail ID");
                }else if(binding.password.getText().toString().length()<6){
                    binding.pass.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    binding.password.setError("Password should contains 6 characters");
                }else{
                    mail = binding.mail.getText().toString();
                    name = binding.username.getText().toString();
                    password = binding.password.getText().toString();
                    updateprofilecallapi();
                }

            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, MainActivity.class);
                startActivity(intent);
                finish();
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
   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                editor.putString("propic", resultUri.toString());
                editor.commit();
                Uri uri = Uri.parse(insertdata.getString("propic", ""));
                binding.profileimg.setImageURI(uri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(activity,MainActivity.class);
        startActivity(intent);
        finish();
    }
}