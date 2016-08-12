package com.thefinder.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.thefinder.DataModel.Location;
import com.thefinder.R;
import java.util.ArrayList;

/**
 *The HistoryAdapter class is responsible for the customization of custom_row.
 * This class displays each row with the location name from the Location ArrayList.
 *
 * @author: Roberto Aguilar
 */

public class HistoryAdapter extends ArrayAdapter<Location> {

    public HistoryAdapter(Context context, ArrayList<Location> places) {
        super(context, R.layout.custom_row, places);
    }

    //@desc: sets the text view of each row
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
