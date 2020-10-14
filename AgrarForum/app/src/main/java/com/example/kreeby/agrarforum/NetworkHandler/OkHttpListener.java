package com.example.kreeby.agrarforum.NetworkHandler;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public interface OkHttpListener {
    void onFailure(Request request, IOException e);
    void onResponse(Response response) throws IOException, JSONException;
}