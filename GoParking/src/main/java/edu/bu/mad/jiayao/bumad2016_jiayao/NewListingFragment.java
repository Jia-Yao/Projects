package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;


public class NewListingFragment extends Fragment {

    public static final String ARG_USERNAME = "username";
    public ArrayList<String> suggest;
    private String username = "", address, numSpots, price, startTime, endTime, data="";
    public AutoCompleteTextView mAddress;
    public ArrayAdapter<String> aAdapter;
    private EditText mNumSpots, mPrice;
    private TextView mStartTime, mEndTime;
    private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
    private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;
    private double lng, lat;
    public static AmazonClientManager clientManager = null;

    public NewListingFragment() {
        // Required empty public constructor
    }

    public static NewListingFragment newInstance(String param1) {
        NewListingFragment fragment = new NewListingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_listing, container, false);
        suggest = new ArrayList<String>();
        mAddress = (AutoCompleteTextView) view.findViewById(R.id.addressField);
        mNumSpots = (EditText) view.findViewById(R.id.numSpotsField);
        mPrice = (EditText) view.findViewById(R.id.priceField);
        mStartTime = (TextView) view.findViewById(R.id.startTime);
        mEndTime = (TextView) view.findViewById(R.id.endTime);
        clientManager = new AmazonClientManager(getActivity());

        Button btn = (Button)view.findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker("start");
            }
        });

        btn = (Button)view.findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker("end");
            }
        });

        btn = (Button)view.findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnterPressed();
            }
        });

        btn = (Button)view.findViewById(R.id.button4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelPressed();
            }
        });

        mAddress.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                new getJson().execute(newText);
            }
        });

        return view;
    }

    class getJson extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... key) {
            String newText = key[0];
            newText = newText.trim().replace(" ", "+");
            data = "";
            suggest = new ArrayList<String>();
            try{
                String API_KEY = "AIzaSyBut8wuiXxvA-FK5RmFE6_rCqp0VJ-rlrI";
                String requestURL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=" + API_KEY + "&input=" + newText;
                URL url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(2000);
                conn.setConnectTimeout(2000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        data += line;
                    }
                }
                System.out.println(data);
                JSONObject jsonObj = new JSONObject(data.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
                for(int i=0;i<predsJsonArray.length();i++){
                    String SuggestKey = predsJsonArray.getJSONObject(i).getString("description");
                    suggest.add(SuggestKey);
                }
            }catch(Exception e){
                Log.w("Error", e.getMessage());
            }

            getActivity().runOnUiThread(new Runnable(){
                public void run(){
                    aAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.autocomplete_list_item,suggest);
                    mAddress.setAdapter(aAdapter);
                    aAdapter.notifyDataSetChanged();
                }
            });
            return null;
        }

    }

    private void datePicker(String flag){
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog;
        switch (flag){
            case "start":
                mStartYear = c.get(Calendar.YEAR);
                mStartMonth = c.get(Calendar.MONTH);
                mStartDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mStartYear = year;
                        mStartMonth = monthOfYear+1;
                        mStartDay = dayOfMonth;
                        //*************Then Call Time Picker********************
                        timePicker("start");
                    }
                }, mStartYear, mStartMonth, mStartDay);
                datePickerDialog.show();
                break;
            case "end":
                mEndYear = c.get(Calendar.YEAR);
                mEndMonth = c.get(Calendar.MONTH);
                mEndDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mEndYear = year;
                        mEndMonth = monthOfYear+1;
                        mEndDay = dayOfMonth;
                        //*************Then Call Time Picker********************
                        timePicker("end");
                    }
                }, mEndYear, mEndMonth, mEndDay);
                datePickerDialog.show();
                break;
            default:
                break;
        }
    }

    private void timePicker(String flag){
        final Calendar c = Calendar.getInstance();
        TimePickerDialog timePickerDialog;
        switch (flag){
            case "start":
                mStartHour = c.get(Calendar.HOUR_OF_DAY);
                mStartMinute = c.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mStartHour = hourOfDay;
                        mStartMinute = minute;
                        mStartTime.setText(String.format("%02d", mStartMonth)+"/"+String.format("%02d", mStartDay)+"/"+mStartYear
                                +" "+String.format("%02d", mStartHour)+":"+String.format("%02d", mStartMinute));
                    }
                }, mStartHour, mStartMinute, false);
                timePickerDialog.show();
                break;
            case "end":
                mEndHour = c.get(Calendar.HOUR_OF_DAY);
                mEndMinute = c.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mEndHour = hourOfDay;
                        mEndMinute = minute;
                        mEndTime.setText(String.format("%02d", mEndMonth)+"/"+String.format("%02d", mEndDay)+"/"+mEndYear
                                +" "+String.format("%02d", mEndHour)+":"+String.format("%02d", mEndMinute));
                    }
                }, mEndHour, mEndMinute, false);
                timePickerDialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        CharSequence addressText = mAddress.getText();
        CharSequence numSpotsText = mNumSpots.getText();
        CharSequence priceText = mPrice.getText();
        CharSequence startTimeText = mStartTime.getText();
        CharSequence endTimeText = mEndTime.getText();
        outState.putCharSequence("addressText", addressText);
        outState.putCharSequence("numSpotsText", numSpotsText);
        outState.putCharSequence("priceText", priceText);
        outState.putCharSequence("startTimeText", startTimeText);
        outState.putCharSequence("endTimeText", endTimeText);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            CharSequence addressText = savedInstanceState.getCharSequence("addressText");
            mAddress.setText(addressText);
            CharSequence numSpotsText = savedInstanceState.getCharSequence("numSpotsText");
            mNumSpots.setText(numSpotsText);
            CharSequence priceText = savedInstanceState.getCharSequence("priceText");
            mPrice.setText(priceText);
            CharSequence startTimeText = savedInstanceState.getCharSequence("startTimeText");
            mStartTime.setText(startTimeText);
            CharSequence endTimeText = savedInstanceState.getCharSequence("endTimeText");
            mEndTime.setText(endTimeText);
        }
    }

    public void onEnterPressed() {
        address = mAddress.getText().toString();
        if (address.trim().equals("")){
            Toast.makeText(getActivity().getApplicationContext(), "Location is required", Toast.LENGTH_SHORT).show();
            return;
        }
        numSpots = mNumSpots.getText().toString();
        if (numSpots.trim().equals("")){
            Toast.makeText(getActivity().getApplicationContext(), "Number of spots is required", Toast.LENGTH_SHORT).show();
            return;
        }
        price = mPrice.getText().toString();
        if (price.trim().equals("")){
            Toast.makeText(getActivity().getApplicationContext(), "Price is required", Toast.LENGTH_SHORT).show();
            return;
        }
        startTime = mStartTime.getText().toString();
        if (startTime.trim().equals("")){
            Toast.makeText(getActivity().getApplicationContext(), "Start time is required", Toast.LENGTH_SHORT).show();
            return;
        }
        endTime = mEndTime.getText().toString();
        if (endTime.trim().equals("")){
            Toast.makeText(getActivity().getApplicationContext(), "End time is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // All fields have been filled
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            if(start.after(end)){
                Toast.makeText(getActivity().getApplicationContext(), "Invalid start/end time combination", Toast.LENGTH_SHORT).show();
                return;
            } else {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mAddress.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mNumSpots.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mPrice.getWindowToken(), 0);
                new GetLatLongTask().execute();
            }
        }catch(ParseException ex){
            ex.printStackTrace();
        }
    }

    public void onCancelPressed(){
        // switching back to listing fragment
        ((HomeActivity) getActivity()).replaceFragments(ListingFragment.class);
    }

    /* Database */
    private enum DynamoDBManagerType {
        INSERT_LISTING
    }

    private class DynamoDBManagerTaskResult {
        private DynamoDBManagerType taskType;
        private String tableStatus;
        private int resultStatus;

        public DynamoDBManagerType getTaskType() {
            return taskType;
        }

        public void setTaskType(DynamoDBManagerType taskType) {
            this.taskType = taskType;
        }

        public String getTableStatus() {
            return tableStatus;
        }

        public void setTableStatus(String tableStatus) {
            this.tableStatus = tableStatus;
        }

        public int getResultStatus(){
            return resultStatus;
        }

        public void setResultStatus(int resultStatus){
            this.resultStatus = resultStatus;
        }
    }

    private class DynamoDBManagerTask extends AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {

        protected DynamoDBManagerTaskResult doInBackground(DynamoDBManagerType... types) {

            String status1 = DynamoDBManager.getListingsTableStatus("newListing");
            String status2 = DynamoDBManager.getUsersTableStatus("newListing");
            String tableStatus = (status1.equalsIgnoreCase("ACTIVE") && status2.equalsIgnoreCase("ACTIVE")) ? "ACTIVE" : "";
            DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);

            if (types[0] == DynamoDBManagerType.INSERT_LISTING) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultStatus(DynamoDBManager.insertListing(username, address, lng, lat, numSpots, price, startTime, endTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            return result;
        }

        protected void onPostExecute(DynamoDBManagerTaskResult result) {

            if (!result.getTableStatus().equalsIgnoreCase("ACTIVE")) {
                Toast.makeText(getActivity().getApplicationContext(), "Database is not ready yet", Toast.LENGTH_SHORT).show();
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.INSERT_LISTING) {
                switch (result.getResultStatus()){
                    case 0:
                        Toast.makeText(getActivity().getApplicationContext(), "Successfully added a listing", Toast.LENGTH_SHORT).show();
                        // switch back to listing fragment
                        ((HomeActivity) getActivity()).replaceFragments(ListingFragment.class);
                        break;
                    case 1:
                        Toast.makeText(getActivity().getApplicationContext(), "Failed to add a listing", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }
    }

    private class GetLatLongTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {
                String formattedAddress = address.trim().replaceAll(" ", "+");
                System.out.println(formattedAddress);
                response = getLatLongByURL("https://maps.googleapis.com/maps/api/geocode/json?address="+formattedAddress+"&key=AIzaSyBut8wuiXxvA-FK5RmFE6_rCqp0VJ-rlrI");
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);
                if (jsonObject.get("results").toString().equals("[]")){
                    Toast.makeText(getActivity().getApplicationContext(), "Not a valid street address", Toast.LENGTH_SHORT).show();

                    return;
                } else {
                    lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    new DynamoDBManagerTask().execute(DynamoDBManagerType.INSERT_LISTING);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                System.out.println(response);
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}