package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnswerQuestion extends AppCompatActivity {


    String id_question = "";
    String myToken = "";
    String myId = "";
    Button post;
    EditText answer;
    String answerString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_question);

        String id = getIntent().getStringExtra("ID_QUESTION");
        id_question += id;

        String token = getIntent().getStringExtra("TOKEN");
        myToken+=token;


        String id_U = getIntent().getStringExtra("ID");
        myId+=id_U;
        post = findViewById(R.id.post_question);
        answer = findViewById(R.id.answer_itself);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerString = answer.getText().toString();

                runInBackround(myToken, id_question, answerString);
            }
        });

    }

    private void runInBackround(final String str1, final String str2, final String str3) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //me    thod containing process logic.
                makeNetworkRequest("http://10.10.10.54/giveAnswer/", str1, str2, str3);


            }
        }).start();
    }

    private void makeNetworkRequest(String reqUrl, String str1, String str2, String str3) {
//        Log.d(TAG, "Booking started: ");
        OkHttpClient httpClient = new OkHttpClient();
        String responseString = "";

        try {
            RequestBody body = new FormBody.Builder()
                    .add("api_token", str1)
                    .add("id", str2)
                    .add("text", str3)
                    .build();

            Request request = new Request.Builder()
                    .url(reqUrl)
                    .post(body)
                    .build();

            Response response = httpClient
                    .newCall(request)
                    .execute();
            responseString = response.body().string();
            response.body().close();
            Log.d("HATUBA", "Booking done: " + str2);

            // Response node is JSON Object
            JSONObject booked = new JSONObject(responseString);
//            JSONArray array = booked.getJSONArray("answer");


            final String okNo = booked.getString("status");

            runOnUiThread(new Runnable() {
                public void run() {
                    if (okNo.equals("success")) {
                        //display in short period of time
                        Toast.makeText(getApplicationContext(), "Answer added Successfully", Toast.LENGTH_LONG).show();
                        Intent changeToQuestions = new Intent(AnswerQuestion.this, QuestionsOfProfessional.class);
                        changeToQuestions.putExtra("ID", myId);
                        changeToQuestions.putExtra("TOKEN", myToken);
                        startActivity(changeToQuestions);

                    } else {
                        //display in short period of time
                        Toast.makeText(getApplicationContext(), "Answer didn't uploaded", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
