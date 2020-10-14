package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class QuestionsOfProfessional extends AppCompatActivity implements View.OnClickListener {
    ArrayList<String> _ids = new ArrayList<String>();
    ArrayList<JSONObject> _times = new ArrayList<JSONObject>();
    private static final String TAG = "ss";
    public ListView listView;
    public JSONObject object;
    ArrayList<String> dates;
    ArrayList<String> names;
    ListView list;
    String newArray = "";
    int cnt = 0;
    public String myID = "";
    String myGranted = "";
    TextView num;
    String view = "listQuestions";
    String id_question = "";
    String myToken = "";
    Button profilim;
    Button haqqimizda;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar1;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView titleText;
    TextView subtitleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_of_professional);

        String id = getIntent().getStringExtra("ID");
        String granted = getIntent().getStringExtra("GRANTED");
        String token = getIntent().getStringExtra("TOKEN");

        myToken+=token;
        myID += id;
        myGranted += granted;
        titleText = (TextView) findViewById(R.id.subtitle);
        subtitleText = findViewById(R.id.title);
        num = findViewById(R.id.num_of_questions);

        list = (ListView) findViewById(R.id.listUsers);


        profilim = findViewById(R.id.profilim);
        haqqimizda = findViewById(R.id.haqqimizda);




        Log.d("LOGIN", "hey" + myID);

        runInBackround(myID);

        setUpToolbar();




    }



    private void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawer_layout);

        toolbar1 = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar1);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar1, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {

                                                  @Override
                                                  public void onClick(View v) {

                                                      drawerLayout.openDrawer(Gravity.RIGHT);
                                                      haqqimizda.setOnClickListener(new View.OnClickListener() {
                                                          public void onClick(View v) {
                                                              Intent changeToProfile = new Intent(QuestionsOfProfessional.this, HaqqimizdaActivity.class);
                                                              changeToProfile.putExtra("ID", myID);
                                                              changeToProfile.putExtra("GRANTED", myGranted);
                                                              startActivity(changeToProfile);
                                                          }
                                                      });
                                                  }
                                              });

        actionBarDrawerToggle.syncState();
    }

    private void runInBackround(final String str1) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //me    thod containing process logic.
                makeNetworkRequest("http://10.10.10.54/listByWhom/", str1);


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





            Log.d("IAMID", id_question + "IDWKA");
            dates = new ArrayList<String>();
            for(int i = 0; i < array.length(); i++) {

                String id = array.getJSONObject(i).getString("id");
                _ids.add(id);
                newArray+=array.getString(i);

//                String time = array.getJSONObject(i).getString("created_at");
//                String text = array.getJSONObject(i).getString("text");
                _times.add(array.getJSONObject(i));
//                _times.add(text);



                cnt++;
            }





            Log.d("MAY", "added_by" + dates);







            runOnUiThread(new Runnable() {
                public void run() {

                    if(dates.isEmpty()) {

                    }
                    final String str = "Sayı: " + Integer.toString(cnt);

                    num.setText(str);
                    //display in short period of time
                    final MyListAdapter adapter;
                    try {
                        adapter = new MyListAdapter(QuestionsOfProfessional.this, _times, view);

                        list.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String id=_ids.get(i);

                            Intent changeToQuestion = new Intent(QuestionsOfProfessional.this, QuestionProfessionalItself.class);
                            changeToQuestion.putExtra("ID_QUESTION", id);
                            changeToQuestion.putExtra("TOKEN", myToken);
                            changeToQuestion.putExtra("ID", myID);
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
//        if(myGranted == "1" || myGranted == "15") {
            Intent changeToProfile = new Intent(QuestionsOfProfessional.this, ProfessionalProfile.class);
            changeToProfile.putExtra("ID", myID);
            changeToProfile.putExtra("GRANTED", myGranted);
            startActivity(changeToProfile);
//        }
    }
}