package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toolbar;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

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

public class SearchMutexessises extends AppCompatActivity {

    private static final String TAG = "";
    ArrayList<String> dates;
    ArrayList<User> names;
    ListView listUsers;
    ArrayList<JSONObject> _times = new ArrayList<JSONObject>();
    ArrayList<String> _ids = new ArrayList<String>();
    String newArray = "";
    String view = "listUsers";
    MyListAdapter adapterSuggest;
    MaterialSearchView searchView;
    UserAdapter userAdapter;
    String myToken = "";

    Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_mutexessises);
//

//        search.setSuggestionsAdapter(adapterSuggest);

        listUsers = findViewById(R.id.listUsers);
        String token = getIntent().getStringExtra("TOKEN");

        myToken+= token;


        Log.d("JUSTCHECKIN:", myToken);
        runInBackround();




    }






    private void runInBackround() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //method containing process logic.
                makeNetworkRequest("http://10.10.10.54/listUsers/");


            }
        }).start();
    }



    private void makeNetworkRequest(String reqUrl) {
//        Log.d(TAG, "Booking started: ");
        OkHttpClient httpClient = new OkHttpClient();
        String responseString = "";

        try {
            RequestBody body = new FormBody.Builder()
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
            final JSONArray array = booked.getJSONArray("message");

            dates = new ArrayList<String>();
            names = new ArrayList<User>();
            for(int i = 0; i < array.length(); i++) {

                newArray+=array.getString(i);
                String id = array.getJSONObject(i).getString("id");
                _ids.add(id);
                dates.add(newArray);
                _times.add(array.getJSONObject(i));


                newArray = "";


//                Log.d("MAY", "name" + name);
            }


            runOnUiThread(new Runnable() {
                public void run() {

                    try {
                        MyListAdapter adapter = new MyListAdapter(SearchMutexessises.this, _times, view);
                        listUsers.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    Log.d("LIQUID", "added_by " + dates);

                    AutoCompleteTextView editText = findViewById(R.id.search_people);

                    userAdapter = new UserAdapter(SearchMutexessises.this, R.layout.custom_row, names);

                    editText.setAdapter(userAdapter);


                    editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            User fruit = (User) adapterView.getItemAtPosition(i);
                            Log.d("SELECTED: ", Integer.toString(fruit.getId()));

                            Intent changeToRegister = new Intent(SearchMutexessises.this, ProfessionalProfileFromSimple.class);
                            changeToRegister.putExtra("ID", Integer.toString(fruit.getId()));
                            changeToRegister.putExtra("GRANTED", myToken);
                            changeToRegister.putExtra("TOKEN", myToken);
                            startActivity(changeToRegister);
                        }
                    });


                    listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String id=_ids.get(i);

                            Intent changeToQuestion = new Intent(SearchMutexessises.this, ProfessionalProfileFromSimple.class);
                            changeToQuestion.putExtra("TOKEN", myToken);
                            changeToQuestion.putExtra("ID", id);
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



}
