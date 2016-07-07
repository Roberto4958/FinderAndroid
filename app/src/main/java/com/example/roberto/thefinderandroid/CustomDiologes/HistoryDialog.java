package com.example.roberto.thefinderandroid.CustomDiologes;

/**
 * Created by roberto on 7/6/16.
 */
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.example.roberto.thefinderandroid.R;


public class HistoryDialog extends DialogFragment {

    private TextView place;
    private String LocationPlace;

    public HistoryDialog(String LocationPlace){
        this.LocationPlace = LocationPlace;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_diolog, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        place = (TextView) view.findViewById(R.id.place);
        place.setText(LocationPlace);
        return view;
    }

}
