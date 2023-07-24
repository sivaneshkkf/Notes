package com.example.my_notes.Fragment;

import android.os.Bundle;

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
import com.example.my_notes.Utils.NetworkController;
import com.example.my_notes.databinding.FragmentNotesBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Notes_Fragment extends Fragment {

  FragmentNotesBinding binding;

  Notes_Adapter notes_adapter;
  ArrayList<String> list;

  APICallbacks apiCallbacks=new APICallbacks() {
    @Override
    public void taskProgress(String tag, int progress, Bundle bundle) {

    }

    @Override
    public void taskFinish(APIStatus apiStatus, String tag, JSONObject response, String message, Bundle bundle) {
      try {
          if (apiStatus==APIStatus.SUCCESS){
              if(tag.equalsIgnoreCase("notesList")){
                  if (response.getBoolean("status")){
                  JSONObject object=response.getJSONObject("");
                }else{
                  Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
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
      list = new ArrayList<>();
      list.add("Home documents");
      list.add("Work documents");
      list.add("playing file");
      list.add("things buying");
      list.add("Work documents");
      RecyclerView recyclerView = binding.notesRecyclerview;
      notes_adapter = new Notes_Adapter(getActivity(),list);
      recyclerView.setAdapter(notes_adapter);
      return binding.getRoot();
    }

    public void callapi(){
      Map<String,String> map=new HashMap<>();
      map.put("userid","");

      NetworkController.getInstance().callApiPost(getActivity(), APPConstants.MAIN_URL+"notesList",map,"notesList",new Bundle(),apiCallbacks);
    }
}