package com.example.my_notes.Utils;

public interface AlertYesNoListener {

    void onYesClick( String requestCode);
    void onNoClick(String requestCode);
    void onNeutralClick(String requestCode);
}
