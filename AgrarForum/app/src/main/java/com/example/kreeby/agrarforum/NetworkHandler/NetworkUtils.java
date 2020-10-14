package com.example.kreeby.agrarforum.NetworkHandler;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {


    private static final String BASE_URl = "http://10.10.10.54/";

    public static void getData(String url, RequestBody body,  final OkHttpListener listener){
        OkHttpClient client = new OkHttpClient();
        // GET request
        Request request = new Request.Builder()
                .url(BASE_URl + url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(call.request(), e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    listener.onResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

    });
    }
}