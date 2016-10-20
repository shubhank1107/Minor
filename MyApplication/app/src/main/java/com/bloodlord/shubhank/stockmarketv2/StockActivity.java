package com.bloodlord.shubhank.stockmarketv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class StockActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String DEBUG_TAG = "REQUEST";

    private AutoCompleteTextView input;
    private boolean flag = false;
    private int total_1 = 0;

    private List<String> favoriteDetails = new ArrayList<>();

    public void refresh(){

        Toast.makeText(getApplicationContext(),"Updating...",Toast.LENGTH_SHORT).show();
        String name;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        try{
            name = preferences.getString("Name", "");
        }catch (Exception e){
            name = "";
        }


        if(!name.matches("")) {
            Log.i("FAV_DATA",name);
            String[] favourites = name.split(" ");
            total_1 = favourites.length;
            for (int a = 0; a < favourites.length; a++) {
                String stringUrl = "http://cs571homework8-env.us-west-1.elasticbeanstalk.com/?symbol=" + favourites[a];
                Log.i("GENERATED_URL",stringUrl);
                ConnectivityManager connManager = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadWebpageTask().execute(stringUrl);
                } else {
                    //t1.setText("No network connection available.");
                }

            }

            String data_1;
            try{
                data_1 = preferences.getString("Data", "");
            }catch (Exception e){
                data_1 = "";
            }
            String[] dataFavourite = new String[favourites.length];
            for(int i = 0; i<favourites.length;i++) {
                dataFavourite[i] = preferences.getString(favourites[i],"");

                Log.i("DATA", dataFavourite[i]);
            }

            ListAdapter currAdapter = new Favourite_Adapter(this, dataFavourite);
            ListView currListView = (ListView) findViewById(R.id.faView);
            currListView.setAdapter(currAdapter);

            currListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                            String name = (String) arg0.getItemAtPosition(position);
                            name = name.split("###")[1];
                            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                            callGetQuote(name);

                        }
                    }
            );

        }
    }

    public void callGetQuote(String name){
        String stringUrl = "http://cs571homework8-env.us-west-1.elasticbeanstalk.com/?symbol=" + name;
        flag = true;
        ConnectivityManager connManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(this, "Getting Latest Exchange...", Toast.LENGTH_SHORT).show();
            new DownloadWebpageTasknew().execute(stringUrl);
        } else {
            //t1.setText("No network connection available.");
        }
    }

    public void Refresh_once(View view){
        refresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        //t1 = (TextView) findViewById(R.id.test1);
        //t2 = (TextView) findViewById(R.id.test2);

        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        mHandler = new Handler();

        refresh();

        input = (AutoCompleteTextView) findViewById(R.id.Company);
        input.setAdapter(new SymbolAutocompleteAdapter(this, R.layout.list_item_view));
        input.setOnItemClickListener(this);
    }

    public void clearStuff(View v) {
        input.setText("");
    }

    public void gotoActivity(View v){
        Intent j = new Intent(getApplicationContext(), Stock_Details.class);
        startActivity(j);
    }

    public void getInfo(View v) {

        String value_a =   input.getText().toString();

        if (value_a.matches("")) {
            Log.i("ERROR","empty field");
            Toast.makeText(getApplicationContext(),"Input field cannot be empty",Toast.LENGTH_SHORT).show();

        }

        else{
            Log.i("value_a",value_a);
            String stringUrl = "http://cs571homework8-env.us-west-1.elasticbeanstalk.com/?symbol=" + value_a;
            flag = true;
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                //t1.setText("Connection available");
                Toast.makeText(this, "Getting Data...", Toast.LENGTH_SHORT).show();
                new DownloadWebpageTasknew().execute(stringUrl);
            } else {
                //t1.setText("No network connection available.");
            }
        }
    }

    public static ArrayList autocomplete(String input1) {
        ArrayList resultList = null;
        HttpURLConnection connHttp = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url = new URL("http://cs571homework8-env.us-west-1.elasticbeanstalk.com/?find="+input1);
            connHttp = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(connHttp.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            //Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (connHttp != null) {
                connHttp.disconnect();
            }
        }

        try {
            Log.i("JSON",jsonResults.toString());
            // Create a JSON object hierarchy from the results
            JSONArray jsonArray = new JSONArray(jsonResults.toString());
            resultList = new ArrayList(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.i("Name",jsonArray.getJSONObject(i).getString("Name"));
                resultList.add(jsonArray.getJSONObject(i).getString("Symbol")+" - "+jsonArray.getJSONObject(i).getString("Name")+" ( "+jsonArray.getJSONObject(i).getString("Exchange")+" )");

            }
        } catch (JSONException e) {
            //Log.e(LOG_TAG, "Cannot process JSON results", e);
        }
        return resultList;
    }




    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        String[] strnew = str.split(" ");
        input.setText(strnew[0]);
    }


    class SymbolAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;
        public SymbolAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }
        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                        //p.setVisibility(View.INVISIBLE);
                    }
                    return filterResults;

                }
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                Log.i("UrlFetch",urls[0]);
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("result",result);
            try {
                //total_1 = total_1+1;

                JSONObject jobject = new JSONObject(result);
                String Name = jobject.getString("Name");

                //if(!flag){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String temp;
                try{
                    temp = preferences.getString(jobject.getString("Symbol"), "");
                }catch (Exception e){
                    temp = "";
                }
                SharedPreferences.Editor editor = preferences.edit();

                temp =  Name + "###" + jobject.getString("Symbol") + "###" + jobject.getString("LastPrice") + "###" + jobject.getString("ChangePercent") + "###" + jobject.getString("MarketCap");
                editor.putString(jobject.getString("Symbol"), temp);
                editor.apply();

                Log.i("DATA_EDITOR", preferences.getString(jobject.getString("Symbol"),""));

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(),"Please Enter a Valid Entry",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            //String contentAsString = readIt(is, len);
            //return contentAsString;

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            String responseData = "";
            while((inputLine = in.readLine())!= null)
                responseData += inputLine;
            in.close();

            return responseData;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private int mInterval = 10000; // 5 seconds by default, can be changed later
    private Handler mHandler;



    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                refresh(); //this function can change value of mInterval.
            } finally {

                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask(View v) {
        Switch s = (Switch) v.findViewById(R.id.autorefresh);
        if (s.isChecked()) {
            mStatusChecker.run();
        }
        else{
            stopRepeatingTask();
        }

    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private class DownloadWebpageTasknew extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                Log.i("UrlFetch",urls[0]);
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.i("result",result);
            try {
                //total_1 = total_1+1;

                JSONObject jobject = new JSONObject(result);
                String Name = jobject.getString("Name");
                Intent i = new Intent(getApplicationContext(), Stock_Details.class);
                i.putExtra("quote_data",result);
                i.putExtra("symbol",input.getText().toString());
                if (jobject.getString("Status").matches("SUCCESS")) {
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "The Status of the reply is not SUCCESS", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Please Enter a Valid Entry",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            //GOOGOt1.setText(result);


        }
    }
    private String downloadUrlnew(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            String responseData = "";
            while((inputLine = in.readLine())!= null)
                responseData += inputLine;
            in.close();

            return responseData;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    public String readItnew(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
