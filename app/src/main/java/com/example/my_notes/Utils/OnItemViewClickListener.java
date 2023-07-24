package com.example.my_notes.Utils;

import android.view.View;

import org.json.JSONException;

public interface OnItemViewClickListener {
    void onClick(View v, int i) throws JSONException;

}
