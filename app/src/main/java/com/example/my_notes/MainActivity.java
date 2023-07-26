package com.example.my_notes;

import static android.media.MediaRecorder.VideoSource.CAMERA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.app.Activity;
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

import com.example.my_notes.Activity.Add_Notes_Activity;
import com.example.my_notes.Activity.Add_Task_Activity;
import com.example.my_notes.Activity.Login_Activity;
import com.example.my_notes.Fragment.Notes_Fragment;
import com.example.my_notes.Fragment.Task_Fragment;

import com.example.my_notes.Utils.DialogUtils;
import com.example.my_notes.databinding.ActivityMainBinding;
import com.example.my_notes.databinding.HeaderMenuBinding;
import com.example.my_notes.databinding.PopupSelectionBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Activity activity;

    HeaderMenuBinding headerMenuBinding;

    Notes_Fragment notes_fragment = new Notes_Fragment();
    Task_Fragment task_fragment = new Task_Fragment();

    PopupSelectionBinding popupBinding;

    SharedPreferences insertdata;
    SharedPreferences.Editor editor;
    AlertDialog popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;

        //for popup view
        popupBinding = PopupSelectionBinding.inflate(getLayoutInflater());
        popup= DialogUtils.getCustomAlertDialog(activity,popupBinding.getRoot());
        popup.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(popupBinding.getRoot().getContext(),R.drawable.background));
        popup.getWindow().setGravity(Gravity.BOTTOM);

        headerMenuBinding = HeaderMenuBinding.inflate(getLayoutInflater());

//  Shared preference
        insertdata= getSharedPreferences("img",MODE_PRIVATE);
        editor=insertdata.edit();



        //to call fragments
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, notes_fragment).commit();


        //on-clicklistener for click action
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerlayout.openDrawer(GravityCompat.START); // Open the side navigation drawer

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

        Intent intent=getIntent();
        if(intent.getStringExtra("activity")!=null){
            if(intent.getStringExtra("activity").equals("Add_Task_Activity")
                    || intent.getStringExtra("activity").equals("Edit_Task_Activity")){
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, task_fragment).commit();
            }
        }


        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bot_nav_notes) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, notes_fragment).commit();

                } else if (item.getItemId() == R.id.bot_nav_tasks) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, task_fragment).commit();

                } else if (item.getItemId() == R.id.bot_nav_add) {
                    popup.show();

                }

                return true;
            }
        });


        ImageView image = binding.navigatonView.getHeaderView(0).findViewById(R.id.edit);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(MainActivity.this);
            }
        });

        MenuItem allnotes=binding.navigatonView.getMenu().findItem(R.id.nav_notes);
        allnotes.setTitle("All Notes :    "+0);


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
                }else if(item.getItemId()==R.id.logout){
                    deleteSharedPreferences("userID");
                    Intent intent=new Intent(MainActivity.this, Login_Activity.class);
                    startActivity(intent);
                }
                return true;
            }
        });


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                ImageView image = binding.navigatonView.getHeaderView(0).findViewById(R.id.profileimg);

                editor.putString("propic",resultUri.toString());
                editor.commit();
                Uri uri=Uri.parse(insertdata.getString("propic",""));
                image.setImageURI(uri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}