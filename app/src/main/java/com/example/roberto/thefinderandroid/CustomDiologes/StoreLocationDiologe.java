package com.example.roberto.thefinderandroid.CustomDiologes;

/**
 * Created by roberto on 7/6/16.
 */
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import com.example.roberto.thefinderandroid.R;


public class StoreLocationDiologe extends DialogFragment implements View.OnClickListener {

    private Button save;
    private EditText place;
    private Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator =(Communicator) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.add_location_diolog, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener((View.OnClickListener) this);
        place = (EditText) view.findViewById(R.id.locationName);
        return view;
    }

    public void showDialog(View view){
        FragmentManager manager = getFragmentManager();
    }

    @Override
    public void onClick(View view) {
        String location = place.getText().toString();

        if(location.length()>0){
            communicator.onDiologMessege(location);
            place.setText("");
            dismiss();

        }
    }
    public interface Communicator{
        public void onDiologMessege(String place);
    }
}
