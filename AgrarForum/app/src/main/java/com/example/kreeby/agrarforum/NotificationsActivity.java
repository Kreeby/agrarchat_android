package com.example.kreeby.agrarforum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> texts;
    private List<String> urls;
    private HashMap<String, String> listHash;
    ArrayList<String> _urls = new ArrayList<String>();
    ArrayList<String> _txts = new ArrayList<String>();
//    private
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        runInBackround();

        listView = (ExpandableListView)findViewById(R.id.lvExp);


//        listView.setAdapter(listAdapter);
    }

    private void initData() {
        urls = new ArrayList<>();
        texts = new ArrayList<>();
        listHash = new HashMap<String, String>();

        for(int i = 0; i < _urls.size(); i++) {
            urls.add(_urls.get(i));
        }

        List<String> edmtDev = new ArrayList<>();
        edmtDev.add("THIS IS EXPANDABLE LIST");

        List<String> txts = new ArrayList<>();
        for(int i = 0; i < _txts.size(); i++) {
            texts.add(_txts.get(i));
        }

        for(int i = 0; i < urls.size(); i++) {
            listHash.put(urls.get(i),  texts.get(i));
        }


//        listDataHeader()

    }

    private void runInBackround() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //me    thod containing process logic.
                makeNetworkRequest("http://192.168.99.64:8000/getNotifications/");
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
    }

    private void makeNetworkRequest(String reqUrl) {
        OkHttpClient httpClient = new OkHttpClient();
        String responseString = "";

        try {
            Request request = new Request.Builder()
                    .url(reqUrl)
                    .get()
                    .build();

            Response response = httpClient
                    .newCall(request)
                    .execute();
            responseString = response.body().string();
            response.body().close();

            JSONObject notifications = new JSONObject(responseString);
            JSONArray notification = notifications.getJSONArray("notifications");

            for(int i = 0; i < notification.length(); i++) {
                _txts.add(notification.getJSONObject(i).get("url").toString());
                _urls.add(notification.getJSONObject(i).get("text").toString());
            }



            runOnUiThread(new Runnable() {
                public void run() {
                    initData();
                    listAdapter = new ExpandableListAdapter(NotificationsActivity.this, texts, listHash, urls);
                    listView.setAdapter(listAdapter);

                }

            });//
//            setToArray(url);
//            setToArray(text);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setToArray(String str) {

    }
}
