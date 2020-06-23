package com.example.kreeby.agrarforum;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.HashMap;

public class NotifCountSession implements OneSignal.NotificationReceivedHandler {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpreference file name
    private static final String PREF_NAME = "notif";

    // User name (make variable public to access from outside)
    public static final String KEY_COUNT = "count";

    // Constructor
    public NotifCountSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveCount(int count){

        // Storing value in pref
        editor.putInt(KEY_COUNT, count);

        editor.commit();
    }

    public HashMap<String, Integer> getCount(){
        HashMap<String, Integer> notif = new HashMap<>();
        // user name
        notif.put(KEY_COUNT, pref.getInt(KEY_COUNT, 0));

        return notif;
    }

    public void resetCount(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        Log.d("NOTIFICATIONSALAMA", data.toString());
    }
}
