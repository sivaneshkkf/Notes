package com.example.my_notes.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_notes.Adapter.Notes_Adapter;
import com.example.my_notes.Adapter.Task_Adapter;
import com.example.my_notes.R;
import com.example.my_notes.databinding.FragmentNotesBinding;
import com.example.my_notes.databinding.FragmentTaskBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Task_Fragment extends Fragment {

  FragmentTaskBinding binding;

  Task_Adapter task_adapter;
 /* ArrayList<String>list;*/

    List<JSONObject> list = new ArrayList<>();

    public Task_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater,container,false);
/*        list = new ArrayList<>();
        list.add("cricket");
        list.add("Field Hockey");
        list.add("Badminton");
        list.add("Football ");
        list.add("volleyball");*/
        RecyclerView recyclerView = binding.taskRecyclerview;
        task_adapter = new Task_Adapter(getActivity(),list);
        recyclerView.setAdapter(task_adapter);
        return binding.getRoot();
    }
}