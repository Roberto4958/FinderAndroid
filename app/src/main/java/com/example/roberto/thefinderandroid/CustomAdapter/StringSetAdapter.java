package com.example.roberto.thefinderandroid.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.roberto.thefinderandroid.DataModel.Location;
import com.example.roberto.thefinderandroid.R;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by roberto on 7/8/16.
 */
public class StringSetAdapter extends ArrayAdapter<String> {

    public StringSetAdapter(Context context, ArrayList<String> places) {
        super(context, R.layout.custom_row, places);
    }
    public View getView(int i, View v, ViewGroup parent){
        LayoutInflater b = LayoutInflater.from(getContext());
         View CustumView = b.inflate(R.layout.custom_row, parent, false);

        String[] Location = getItem(i).split(" ");
        String nameOfPlace =Location[3];
        for(int r =4; r< Location.length; r++){
            nameOfPlace += " "+Location[r];
        }
        TextView tView = (TextView) CustumView.findViewById(R.id.textView);
        tView.setText(nameOfPlace);
        return CustumView;
    }
}
