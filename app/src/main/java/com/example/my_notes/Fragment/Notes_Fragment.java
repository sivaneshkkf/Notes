package com.example.my_notes.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.my_notes.API.APICallbacks;
import com.example.my_notes.API.APIStatus;
import com.example.my_notes.API.APPConstants;
import com.example.my_notes.Adapter.Notes_Adapter;
import com.example.my_notes.R;
import com.example.my_notes.Utils.CommonFunctions;
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.Utils.OnItemViewClickListener;
import com.example.my_notes.databinding.FragmentNotesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Notes_Fragment extends Fragment {

  FragmentNotesBinding binding;

  String userID="";

  SharedPreferences insertdata;
  SharedPreferences.Editor editor;

  Notes_Adapter notes_adapter;
 /* ArrayList<String> list;*/

  ArrayList<JSONObject> list=new ArrayList<>();

  APICallbacks apiCallbacks=new APICallbacks() {
    @Override
    public void taskProgress(String tag, int progress, Bundle bundle) {

    }

    @Override
    public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
      try {
              if(tag.equalsIgnoreCase("notesList")){
                  if (response.getBoolean("status")){
                    list.clear();
                    CommonFunctions.setJSONArray(response,"msg",list,notes_adapter);
                }else{
                  Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding = FragmentNotesBinding.inflate(inflater,container,false);

      return binding.getRoot();
    }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    insertdata= getContext().getSharedPreferences("userID",Context.MODE_PRIVATE);
    editor=insertdata.edit();
    userID=String.valueOf(insertdata.getInt("userid",0));
    Toast.makeText(getActivity(), "userid: "+userID, Toast.LENGTH_SHORT).show();

    callapi();
    notes_adapter = new Notes_Adapter(getActivity(), list, new OnItemViewClickListener() {
      @Override
      public void onClick(View v, int i) throws JSONException {
          if(v.getId()==R.id.delete){
            callapi();
            notes_adapter.notifyDataSetChanged();
          }
      }
    });
    binding.notesRecyclerview.setAdapter(notes_adapter);


  }

  public void callapi(){
      Map<String,String> map=new HashMap<>();
      map.put("userid",userID);

      NetworkController.getInstance().callApiPost(getActivity(), APPConstants.MAIN_URL+"notesList",map,"notesList",new Bundle(),apiCallbacks);
    }

}