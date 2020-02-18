package com.example.kreeby.agrarforum;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<JSONObject> {

    private final Activity context;
    private final ArrayList<String> maintitle = new ArrayList<String>();
    private final ArrayList<String> maintitle1 = new ArrayList<String>();
    private final ArrayList<String> subtitle = new ArrayList<>();

    public ArrayList<String> selectedItems = new ArrayList<>();

    public ArrayList<String> selectedItems1 = new ArrayList<>();
    private final ArrayList<String> subtitle1 = new ArrayList<>();
    String date = "";
    String view = "";
    int cnt = 0;

    public MyListAdapter(Activity context, ArrayList<JSONObject> maintitle, String view) throws JSONException {
        super(context, R.layout.listview, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        for(int i = 0; i  < maintitle.size(); i++){
            if(view.equals("listQuestions")) {
                this.maintitle.add(maintitle.get(i).getString("created_at"));
                this.maintitle1.add(maintitle.get(i).getString("text"));
            }
            else if(view.equals("listUsers")) {
                this.maintitle.add(maintitle.get(i).getString("username"));
                this.maintitle1.add(maintitle.get(i).getString("category"));
            }
        }

        this.view += view;

    }

    public View getView(int position, View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();



        View rowView = inflater.inflate(R.layout.listview, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView subtitleText = rowView.findViewById(R.id.title);

        if (this.view.equals("listQuestions")) {

            Log.d("sdasda", maintitle.get(position));
            titleText.setText(maintitle.get(position));


            subtitleText.setText(maintitle1.get(position));

        }

        if(this.view.equals("listUsers")) {

            titleText.setText(maintitle1.get(position));


            subtitleText.setText(maintitle.get(position));
        }
//







        return rowView;

        }


}
