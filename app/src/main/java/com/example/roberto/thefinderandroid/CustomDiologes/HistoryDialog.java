package com.example.roberto.thefinderandroid.CustomDiologes;

/**
 * Created by roberto on 7/6/16.
 */
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.roberto.thefinderandroid.R;


public class HistoryDialog extends DialogFragment {

    private TextView place;
    private View view;
    private String Location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_diolog, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        place = (TextView)view.findViewById(R.id.place);
        place.setText(Location);
        return view;
    }

    public void onCreate(String location) {
        Location = location;
    }
}
