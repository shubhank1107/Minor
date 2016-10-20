package com.bloodlord.shubhank.stockmarketv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shubhank on 16-10-2016.
 */

public class Fragment_Current extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String json_data = getArguments().getString("json");

        View view = inflater.inflate(R.layout.fragment_current, container, false);
        //TextView temp = (TextView) view.findViewById(R.id.currentText);
        try {
            JSONObject jobject = new JSONObject(json_data);
            String[] data = {jobject.getString("Name"),jobject.getString("Symbol"),jobject.getString("LastPrice"),jobject.getString("Change")+" ( "+jobject.getString("ChangePercent")+" %)",jobject.getString("Timestamp"),jobject.getString("MarketCap"),jobject.getString("Volume"),jobject.getString("ChangeYTD")+" ( "+jobject.getString("ChangePercentYTD")+" %) ",jobject.getString("High"),jobject.getString("Low"),jobject.getString("Open"),jobject.getString("Symbol")};
            Log.i("JSON Data",json_data);
            //ListAdapter currentAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,data);
            ListAdapter currentAdapter = new CustomArrAdapter(getActivity(),data);
            ListView currentListView = (ListView) view.findViewById(R.id.currentView);
            currentListView.setAdapter(currentAdapter);
        } catch (JSONException e) {
            Log.i("JSON","ERROR");
            e.printStackTrace();
            //temp.setText(json_data);

        }
        return view;
    }
}

