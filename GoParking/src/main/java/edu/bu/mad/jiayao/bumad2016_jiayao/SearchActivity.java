package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class SearchActivity extends AppCompatActivity {

    private String username = "";
    private static final int PLACE_PICKER_REQUEST = 1;
    private TextView mName, mAddress, mAttributions, mStartTime, mEndTime;
    private double mLongitude = 0, mLatitude = 0;
    private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
    private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            username = extras.getString("username");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        setTitle("Search");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mName = (TextView) findViewById(R.id.locationName);
        mAddress = (TextView) findViewById(R.id.locationAddress);
        mAttributions = (TextView) findViewById(R.id.locationAttribution);
        mStartTime = (TextView) findViewById(R.id.startTime);
        mEndTime = (TextView) findViewById(R.id.endTime);
    }

    public void onPickButtonPressed(View v) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);
            final LatLng latLng = place.getLatLng();
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            mLongitude = latLng.longitude;
            mLatitude = latLng.latitude;
            mName.setText(name);
            mAddress.setText(address);
            mAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onPickStartPressed(View v) {
        datePicker("start");
    }

    public void onPickEndPressed(View v) {
        datePicker("end");
    }

    private void datePicker(String flag){
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog;
        switch (flag){
            case "start":
                mStartYear = c.get(Calendar.YEAR);
                mStartMonth = c.get(Calendar.MONTH);
                mStartDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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
                timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        CharSequence nameText = mName.getText();
        CharSequence addressText = mAddress.getText();
        CharSequence attributionsText = mAttributions.getText();
        CharSequence startTimeText = mStartTime.getText();
        CharSequence endTimeText = mEndTime.getText();
        outState.putCharSequence("nameText", nameText);
        outState.putCharSequence("addressText", addressText);
        outState.putCharSequence("attributionsText", attributionsText);
        outState.putCharSequence("startTimeText", startTimeText);
        outState.putCharSequence("endTimeText", endTimeText);
        outState.putDouble("longitude", mLongitude);
        outState.putDouble("latitude", mLatitude);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence nameText = savedInstanceState.getCharSequence("nameText");
        mName.setText(nameText);
        CharSequence addressText = savedInstanceState.getCharSequence("addressText");
        mAddress.setText(addressText);
        CharSequence attributionsText = savedInstanceState.getCharSequence("attributionsText");
        mAttributions.setText(attributionsText);
        CharSequence startTimeText = savedInstanceState.getCharSequence("startTimeText");
        mStartTime.setText(startTimeText);
        CharSequence endTimeText = savedInstanceState.getCharSequence("endTimeText");
        mEndTime.setText(endTimeText);
        double longitude = savedInstanceState.getDouble("longitude");
        mLongitude = longitude;
        double latitude = savedInstanceState.getDouble("latitude");
        mLatitude = latitude;
    }

    public void onSearchPressed(View v) {
        String address, startTime, endTime;
        address = mAddress.getText().toString();
        if (address.trim().equals("")){
            Toast.makeText(getApplicationContext(), "Location is required", Toast.LENGTH_SHORT).show();
            return;
        }
        startTime = mStartTime.getText().toString();
        if (startTime.trim().equals("")){
            Toast.makeText(getApplicationContext(), "Start time is required", Toast.LENGTH_SHORT).show();
            return;
        }
        endTime = mEndTime.getText().toString();
        if (endTime.trim().equals("")){
            Toast.makeText(getApplicationContext(), "End time is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // All fields have been filled
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            if(start.after(end)){
                Toast.makeText(getApplicationContext(), "Invalid start/end time combination", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // Construct an intent for the search result
                Intent i = new Intent(this, SearchResultActivity.class);
                i.putExtra("username", username);
                i.putExtra("longitude", mLongitude);
                i.putExtra("latitude", mLatitude);
                i.putExtra("startTime", startTime);
                i.putExtra("endTime", endTime);
                startActivity(i);
            }
        }catch(ParseException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent data = new Intent();
                data.putExtra("username", username);
                setResult(Activity.RESULT_OK, data);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
