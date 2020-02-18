package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QuestionProfessionalItself extends AppCompatActivity {
    String id_question = "";
    String myToken = "";
    String myId = "";
    ArrayList<String> _texts = new ArrayList<String>();
    ArrayList<String> _answers = new ArrayList<String>();

    String text1 = "";

    TextView sual;
    TextView cavab;
    Button cavabVer;
    ImageView img;
    String imgS = "";

    boolean isAnswer = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_professional_itself);

        String id = getIntent().getStringExtra("ID_QUESTION");
        id_question += id;
        sual = findViewById(R.id.textView13);
        cavab = findViewById(R.id.textView15);
        cavabVer = findViewById(R.id.post_question);
        img = findViewById(R.id.img);
        String token = getIntent().getStringExtra("TOKEN");
        myToken+=token;

        String id_U = getIntent().getStringExtra("ID");
        myId+= id_U;

        runInBackround(id_question, myToken);
    }


    private void runInBackround(final String str1, final String str2) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //me    thod containing process logic.
                makeNetworkRequest("http://192.168.99.64:8000/isAnswer/", str1, str2);


            }
        }).start();
    }

    private void makeNetworkRequest(String reqUrl, String str1, String str2) {
//        Log.d(TAG, "Booking started: ");
        OkHttpClient httpClient = new OkHttpClient();
        String responseString = "";

        try {
            RequestBody body = new FormBody.Builder()
                    .add("id", str1)
                    .add("api_token", str2)
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
//            Log.d(TAG, "Booking done: " + responseString);

            // Response node is JSON Object
            JSONObject booked = new JSONObject(responseString);
            JSONArray array = booked.getJSONArray("question");
            JSONArray array1 = booked.getJSONArray("answer");
            String answer = "";
            Log.d("MYARRAY", array1 + "HELLO");
            for (int i = 0; i < array1.length(); i++) {
                answer = array1.getJSONObject(i).getString("text");
                _answers.add(answer);
                isAnswer = true;


            }


            for(int i = 0; i < array.length(); i++) {
                String text = array.getJSONObject(i).getString("text");
                _texts.add(text);

                imgS = array.getJSONObject(i).getString("image");


                final String finalText = text;

                final String finalAsnwer = answer;

                runOnUiThread(new Runnable() {
                    public void run() {
                        sual.setText(finalText);
                        if(!imgS.equals(null))
                            Picasso.get().load("http://192.168.99.64:8000/uploads/images/store/" + imgS).into(img);

                        cavab.setText(finalAsnwer);
                        if(isAnswer == true) {
                            cavabVer.setVisibility(View.GONE);
                        }
                        else {
                            cavabVer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent changeToAnswer = new Intent(QuestionProfessionalItself.this, AnswerQuestion.class);
                                    changeToAnswer.putExtra("ID_QUESTION", id_question);
                                    changeToAnswer.putExtra("TOKEN", myToken);
                                    changeToAnswer.putExtra("ID", myId);
                                    startActivity(changeToAnswer);
                                }
                            });
                        }
                    }
                });

            }



        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
