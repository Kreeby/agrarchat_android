package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QuestionsOfProfessionalFromSimple extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ss";
    ArrayList<String> dates;
    String newArray = "";
    ArrayList<JSONObject> _times = new ArrayList<JSONObject>();
    ArrayList<String> _ids = new ArrayList<String>();

    int cnt = 0;
    String hisId = "";
    String view = "listQuestions";
    ListView list;
    String myToken = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quesitons_of_professional_from_simple);

        String id = getIntent().getStringExtra("ID");
        hisId += id;


        String token = getIntent().getStringExtra("TOKEN");

        myToken+= token;

        Log.d("WANTTOKNOW", hisId);

        list = (ListView) findViewById(R.id.listUsers);
        runInBackround(hisId);
    }




    private void runInBackround(final String str1) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //me    thod containing process logic.
                makeNetworkRequest("http://192.168.99.64:8000/listByWhom/", str1);


            }
        }).start();
    }

    private void makeNetworkRequest(String reqUrl, String str1) {
//        Log.d(TAG, "Booking started: ");
        OkHttpClient httpClient = new OkHttpClient();
        String responseString = "";

        try {
            RequestBody body = new FormBody.Builder()
                    .add("added_to", str1)
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
            JSONArray array = booked.getJSONArray("message");

            dates = new ArrayList<String>();
            for(int i = 0; i < array.length(); i++) {

                newArray+=array.getString(i);

                String id = array.getJSONObject(i).getString("id");
                _ids.add(id);

                dates.add(newArray);

                newArray = "";

                _times.add(array.getJSONObject(i));

                cnt++;

//                Log.d("MAY", "name" + name);
            }





            Log.d("MAY", "added_by" + dates);







            runOnUiThread(new Runnable() {
                public void run() {

                    if(dates.isEmpty()) {

                    }

                    //display in short period of time
                    try {
                        MyListAdapter adapter = new MyListAdapter(QuestionsOfProfessionalFromSimple.this, _times, view);
                        list.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String id=_ids.get(i);

                            Intent changeToQuestion = new Intent(QuestionsOfProfessionalFromSimple.this, QuestionProfessionalItselfFromSimple.class);
                            changeToQuestion.putExtra("ID_QUESTION", id);
                            changeToQuestion.putExtra("TOKEN", myToken);
                            changeToQuestion.putExtra("ID", hisId);
//                            for(int j = 0; j < adapter.getTitle().size(); j++) {
//                                titleText.setText(adapter.getItem(j));
//                            }
//                            changeToQuestion.putExtra("GRANTED", myToken);
                            startActivity(changeToQuestion);
                        }
                    });
                }
            });


        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());

        }
    }


    @Override
    public void onClick(View v) {
        Intent changeToRegister = new Intent(QuestionsOfProfessionalFromSimple.this, AddQuestionActivity.class);
        changeToRegister.putExtra("TOKEN", myToken);
        changeToRegister.putExtra("ID", hisId);
        startActivity(changeToRegister);
    }
}




