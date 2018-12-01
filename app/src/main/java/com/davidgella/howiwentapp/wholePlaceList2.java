package com.davidgella.howiwentapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class wholePlaceList2 extends ArrayAdapter<wholePlace> {

    private Activity context;
    private List<wholePlace>  wholePlaceList; //store all the wholePlace

    public wholePlaceList2(Activity context, List<wholePlace>wholePlaceList){
        super(context, R.layout.wholeplace_layout,wholePlaceList);
        this.context = context;
        this.wholePlaceList = wholePlaceList;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.seelatest_layout,null,true);

        TextView tvFrom = (TextView) listViewItem.findViewById(R.id.tvFrom);
        TextView tvTo = (TextView) listViewItem.findViewById(R.id.tvTo);

        wholePlace wholeplace = wholePlaceList.get(position);
        tvFrom.setText(wholeplace.getFrom());
        tvTo.setText(wholeplace.getTo());


        return listViewItem;
    }
}


