package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.tntkhang.fullscreenimageview.library.FullScreenImageViewActivity;
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

public class QuestionProfessionalItselfFromSimple extends AppCompatActivity {
    String id_question = "";
    String myToken = "";
    String hisId = "";

    ArrayList<String> _texts = new ArrayList<String>();
    ArrayList<String> _answers = new ArrayList<String>();

    TextView sual;
    TextView cavab;

    ImageView img;




    String imgS = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_professional_itself_from_simple);

        String id = getIntent().getStringExtra("ID_QUESTION");
        id_question += id;
        sual = findViewById(R.id.sual);
        cavab = findViewById(R.id.cavab);

        img = findViewById(R.id.img);
        String token = getIntent().getStringExtra("TOKEN");
        myToken+=token;

        String id_U = getIntent().getStringExtra("ID");
        hisId+= id_U;

        runInBackround(id_question, token);
    }


    private void runInBackround(final String str1, final String str2) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //me    thod containing process logic.
                makeNetworkRequest("http://10.10.10.54/isAnswer/", str1, str2);


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
                        if(!imgS.equals(null)) {

                         Picasso.get().load("http://10.10.10.54/uploads/images/store/" + imgS).into(img);
                         //  img.setImageUrl("http://10.10.10.54/uploads/images/store/" + imgS);





                        }

                        cavab.setText(finalAsnwer);
                    }
                });

            }



        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openImage(View view) {
        Intent fullImageIntent = new Intent(QuestionProfessionalItselfFromSimple.this, FullScreenImageViewActivity.class);
        // uriString is an ArrayList<String> of URI of all images
        ArrayList<String> uriString = new ArrayList<String>();
        uriString.add("http://10.10.10.54/uploads/images/store/" + imgS);
        fullImageIntent.putExtra(FullScreenImageViewActivity.URI_LIST_DATA, uriString);
        // pos is the position of image will be showned when open
        fullImageIntent.putExtra(FullScreenImageViewActivity.IMAGE_FULL_SCREEN_CURRENT_POS, 1);
        startActivity(fullImageIntent);
    }
}
