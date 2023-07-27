package com.example.my_notes;

import static android.media.MediaRecorder.VideoSource.CAMERA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.Activity.Add_Notes_Activity;
import com.example.my_notes.Activity.Add_Task_Activity;
import com.example.my_notes.Activity.Edit_Profile_Activity;
import com.example.my_notes.Activity.Login_Activity;
import com.example.my_notes.Fragment.Notes_Fragment;
import com.example.my_notes.Fragment.Task_Fragment;

import com.example.my_notes.Utils.DialogUtils;
import com.example.my_notes.Utils.NetworkController;

import com.example.my_notes.databinding.ActivityMainBinding;
import com.example.my_notes.databinding.HeaderMenuBinding;
import com.example.my_notes.databinding.PopupSelectionBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Activity activity;
    SharedPreferences insertdata;
    SharedPreferences.Editor editor;
    String userID = "";
    String notesCount = "";

    HeaderMenuBinding headerMenuBinding;
    String userid, name, mail, userregId;

    Notes_Fragment notes_fragment = new Notes_Fragment();
    Task_Fragment task_fragment = new Task_Fragment();

    PopupSelectionBinding popupBinding;

    //    SharedPreferences sharedPreferences;
    AlertDialog popup;


    APICallbacks apiCallbacks = new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
            try {
                if (tag.equalsIgnoreCase("viewProfile")) {
                    if (response.getBoolean("status")) {
                        JSONObject object = response.getJSONObject("msg");
                        name = object.getString("username");
                        mail = object.getString("useremail");
                        userregId = object.getString("Regid");

                        //for showing name in menu_nav
                        MenuItem allnotes1 = binding.navigatonView.getMenu().findItem(R.id.nav_mail);
                        allnotes1.setTitle(mail);

                        MenuItem allnotes2 = binding.navigatonView.getMenu().findItem(R.id.nav_id);
                        allnotes2.setTitle("UserID:" + userregId);

                        //for showing header username
                        TextView textView = binding.navigatonView.getHeaderView(0).findViewById(R.id.nav_header_name);
                        textView.setText(name);

                    } else {
                        Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                if (tag.equalsIgnoreCase("notesList")) {
                    if (response.getBoolean("status")) {
                        notesCount = String.valueOf(response.getInt("notescount"));
                        MenuItem allnotes = binding.navigatonView.getMenu().findItem(R.id.nav_notes);
                        allnotes.setTitle("All Notes :    " + notesCount);

                    } else {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;


        //for popup view
        popupBinding = PopupSelectionBinding.inflate(getLayoutInflater());
        popup = DialogUtils.getCustomAlertDialog(activity, popupBinding.getRoot());
        popup.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(popupBinding.getRoot().getContext(), R.drawable.background));
        popup.getWindow().setGravity(Gravity.BOTTOM);

        headerMenuBinding = HeaderMenuBinding.inflate(getLayoutInflater());

//  Shared preference
        insertdata = getSharedPreferences("img", MODE_PRIVATE);
        insertdata = getSharedPreferences("userID", Context.MODE_PRIVATE);
        editor = insertdata.edit();

        userid = String.valueOf(insertdata.getInt("userid", 0));

        ImageView image = binding.navigatonView.getHeaderView(0).findViewById(R.id.profileimg);
        Uri uri = Uri.parse(insertdata.getString("propic", ""));
        image.setImageURI(uri);

//        Toast.makeText(activity, "userid"+userid, Toast.LENGTH_SHORT).show();


        callapi();
        notesCountapi();


//        sharedPreferences = getSharedPreferences("sharedpref", Context.MODE_PRIVATE);


        //to call fragments
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, notes_fragment).commit();


        //on-clicklistener for click action
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerlayout.openDrawer(GravityCompat.START); // Open the side navigation drawer
                binding.navigatonView.refreshDrawableState();
            }
        });

        binding.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Edit_Profile_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        popupBinding.popNotesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Add_Notes_Activity.class);
                startActivity(intent);
                popup.dismiss();

            }
        });


        popupBinding.popTaskTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Add_Task_Activity.class);
                startActivity(intent);
                popup.dismiss();
            }
        });

        popupBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        binding.bottomNavigation.getMenu().getItem(0).setChecked(true);

        //for barrom navigationbar function

        Intent intent = getIntent();
        if (intent.getStringExtra("activity") != null) {
            if (intent.getStringExtra("activity").equals("Add_Task_Activity")
                    || intent.getStringExtra("activity").equals("Edit_Task_Activity")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, task_fragment).commit();
                binding.bottomNavigation.getMenu().getItem(2).setChecked(true);
                binding.title.setText("MY Tasks");
            }
        }


        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bot_nav_notes) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, notes_fragment).commit();
                    binding.title.setText("My Notes");

                } else if (item.getItemId() == R.id.bot_nav_tasks) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, task_fragment).commit();
                    binding.title.setText("My Tasks");

                } else if (item.getItemId() == R.id.bot_nav_add) {
                    popup.show();

                }

                return true;
            }
        });


        binding.navigatonView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.profileimg) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(MainActivity.this);
                    return true;
                } else if (item.getItemId() == R.id.nav_notes) {
                    return true;
                } else if (item.getItemId() == R.id.logout) {
                    deleteSharedPreferences("userID");
                    Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });


    }

    public void callapi() {
        Map<String, String> map = new HashMap<>();

        map.put("userid", "11");

        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL + "viewProfile", map, "viewProfile", new Bundle(), apiCallbacks);
    }

    public void notesCountapi() {
        Map<String, String> map = new HashMap<>();
        map.put("userid", "11");

        NetworkController.getInstance().callApiPost(activity, APPConstants.MAIN_URL + "notesList", map, "notesList", new Bundle(), apiCallbacks);
    }
}