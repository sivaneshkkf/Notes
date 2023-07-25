package com.example.my_notes.API;

import android.content.Intent;

public interface APPConstants {
    public static String HOST = "http://aknotes.akprojects.co/";//local
    public static String API_URL = "api/";
    public static String MAIN_URL = HOST + API_URL;
    public static String CUSTOMER_LAST_MODIFIED = "2023-01-01 00:00:00";

    String playUrl = "";
    int clearActivities = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK;
}
