package com.example.kreeby.agrarforum;

import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class NotificationHandler implements OneSignal.NotificationReceivedHandler {
    // This fires when a notification is opened by tapping on it.
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String customKey;
        try {
            String url = data.getString("url");
            String image = data.getString("text");
            runInBackround(url, image);
            System.out.println(url + " " + image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (data != null) {
            customKey = data.optString("customkey", null);


            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }
    }

    private void makeNetworkRequest(String url, String dataUrl, String dataImage) {
        OkHttpClient httpClient = new OkHttpClient();
        String responseString = "";
        try {
            RequestBody body = new FormBody.Builder()
                    .add("url", dataUrl)
                    .add("text", dataImage)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = httpClient
                    .newCall(request)
                    .execute();
            responseString = response.body().string();
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runInBackround( final String str1, final String str2){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //me    thod containing process logic.
                    makeNetworkRequest("http://10.10.10.54/notificationToDB/", str1, str2);
                }
            }).start();
    }

}