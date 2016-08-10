package com.example.roberto.thefinderandroid.CustomAdapter;

/**
 * Created by roberto on 7/6/16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.roberto.thefinderandroid.DataModel.Location;
import com.example.roberto.thefinderandroid.R;
import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<Location> {

    public CustomAdapter(Context context, ArrayList<Location> places) {
        super(context, R.layout.custom_row, places);
    }
    @Override
    public View getView(int i, View v, ViewGroup parent){
        LayoutInflater b = LayoutInflater.from(getContext());
        View CustumView = b.inflate(R.layout.custom_row, parent, false);
        String place = getItem(i).place;
        TextView tView = (TextView) CustumView.findViewById(R.id.textView);
        tView.setText(place);
        return CustumView;
    }
}
