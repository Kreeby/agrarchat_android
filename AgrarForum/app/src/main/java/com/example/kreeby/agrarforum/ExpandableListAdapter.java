package com.example.kreeby.agrarforum;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final Activity context;
    private List<String> text;
    private HashMap<String, String> listHashMap;
    private List<String> urls;


    public ExpandableListAdapter(Activity context, List<String> text, HashMap<String, String> listHashMap, List<String> urls) {
        this.context = context;
        this.text = text;
        this.urls = urls;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return urls.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return urls.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(urls.get(i));
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group, null);
        }
        TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String)getChild(i, i1);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView)view.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
