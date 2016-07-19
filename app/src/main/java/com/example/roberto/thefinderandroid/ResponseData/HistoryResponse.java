package com.example.roberto.thefinderandroid.ResponseData;

import com.example.roberto.thefinderandroid.DataModel.Location;
import java.util.ArrayList;

/**
 * Created by roberto on 7/15/16.
 */
public class HistoryResponse extends Response{

    public ArrayList<Location> result;

    public HistoryResponse(ArrayList<Location> l, String s){

        super(s);
        result = l;
    }
}
