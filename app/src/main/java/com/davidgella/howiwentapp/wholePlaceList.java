package com.davidgella.howiwentapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class wholePlaceList extends ArrayAdapter<wholePlace> {

    private Activity context;
    private List<wholePlace>  wholePlaceList;

    public wholePlaceList(Activity context, List<wholePlace>wholePlaceList){
        super(context, R.layout.profilepage,wholePlaceList);
        this.context = context;
        this.wholePlaceList = wholePlaceList;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.profilepage,null,true);

        TextView tvFrom = (TextView) listViewItem.findViewById(R.id.tvFrom);
        TextView tvTo = (TextView) listViewItem.findViewById(R.id.tvTo);

        wholePlace wholeplace = wholePlaceList.get(position);
        tvFrom.setText(wholeplace.getFrom());
        tvTo.setText(wholeplace.getTo());

        return listViewItem;
    }
}


