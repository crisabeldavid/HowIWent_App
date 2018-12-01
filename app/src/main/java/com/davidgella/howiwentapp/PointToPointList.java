package com.davidgella.howiwentapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PointToPointList extends ArrayAdapter<PointToPoint> {

    private Activity context;
    private List<PointToPoint> PointToPointList;

    public PointToPointList(Activity context, List<PointToPoint>PointToPointList){
        super(context, R.layout.wholeplace_layout,PointToPointList);
        this.context = context;
        this.PointToPointList = PointToPointList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.seelatest_layout,null,true);

        TextView tvFrom = (TextView) listViewItem.findViewById(R.id.tvFrom);
        TextView tvTo = (TextView) listViewItem.findViewById(R.id.tvTo);


        PointToPoint p2p = PointToPointList.get(position);
        tvFrom.setText(p2p.getFrom());
        tvTo.setText(p2p.getTo());

        return listViewItem;
    }

}
