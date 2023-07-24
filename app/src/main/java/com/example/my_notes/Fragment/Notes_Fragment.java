package com.example.my_notes.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_notes.Adapter.Notes_Adapter;
import com.example.my_notes.R;
import com.example.my_notes.databinding.FragmentNotesBinding;

import java.util.ArrayList;


public class Notes_Fragment extends Fragment {

  FragmentNotesBinding binding;

  Notes_Adapter notes_adapter;
  ArrayList<String> list;

    public Notes_Fragment() {
        // Required empty public constructor
    }


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
}