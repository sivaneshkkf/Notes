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
import com.example.my_notes.databinding.ActivityLoginBinding;
import com.example.my_notes.databinding.ActivitySplashBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {
ActivityLoginBinding binding;
String mail,password;
SharedPreferences insertdata;
SharedPreferences.Editor editor;
Activity activity;
    APICallbacks apiCallbacks=new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                    if(tag.equalsIgnoreCase("login")){
                        if(response.getBoolean("status")){
                            JSONObject object=response.getJSONObject("userDetails");
                            int userid=object.getInt("data");

                            editor.putInt("userid",userid);
                            editor.commit();

                            Toast.makeText(activity, "userid: "+insertdata.getInt("userid",0), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity, "tag error", Toast.LENGTH_SHORT).show();
                    }
            } catch (JSONException e) {
                Toast.makeText(activity, "catch error", Toast.LENGTH_SHORT).show();
              //  throw new RuntimeException(e);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;

        insertdata= getSharedPreferences("userID",MODE_PRIVATE);
        editor=insertdata.edit();

        int userid1=insertdata.getInt("userid",0);
       /* Toast.makeText(activity, "userid: "+userid1, Toast.LENGTH_SHORT).show();*/
        if(userid1!=0){
            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(intent);
        }

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);

            }
        };

        binding.loginbtn.setOnClickListener(onClickListener);
        binding.siginup.setOnClickListener(onClickListener);


    }

    public void click(View v) {
        if (v.getId() == R.id.loginbtn) {
            mail=binding.username.getText().toString();
            password=binding.password.getText().toString();
            callapi();
        } else if (v.getId() == R.id.siginup) {
            Intent intent = new Intent(Login_Activity.this, SiginUp_Activity.class);
            startActivity(intent);
        }
    }
    private void callapi() {
        Map<String, String> map = new HashMap<>();
        map.put("mail", mail);
        map.put("password", password);
        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL + "login", map, "login", new Bundle(), apiCallbacks);
    }

}