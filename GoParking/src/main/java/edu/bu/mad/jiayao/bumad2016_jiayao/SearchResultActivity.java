package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity implements OnMapReadyCallback, OnInfoWindowClickListener, OnInfoWindowLongClickListener{

    private String username = "";
    private String startTime, endTime;
    private double longitude, latitude;
    private GoogleMap mMap;
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private Map<Marker, String> allMarkersMap = new HashMap<Marker, String>();
    private ArrayList<Listing> parkingSpots = new ArrayList<Listing>();
    private String listing_id;
    public static AmazonClientManager clientManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            username = extras.getString("username");
            longitude = extras.getDouble("longitude");
            latitude = extras.getDouble("latitude");
            startTime = extras.getString("startTime");
            endTime = extras.getString("endTime");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        setTitle("Search Results");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        clientManager = new AmazonClientManager(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        new DynamoDBManagerTask().execute(DynamoDBManagerType.SEARCH_LISTING);
        // Add a marker in the request location, and move the camera.
        LatLng requestLoc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(requestLoc)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Request Location"));
        builder.include(requestLoc);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(requestLoc));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowLongClickListener(this);
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        if (!marker.getTitle().equals("Request Location")){
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchResultActivity.this);
            alertDialog.setTitle("Reservation");
            alertDialog.setMessage("Make a reservation request? Your email will be made visible to the owner of this parking spot.");
            alertDialog.setIcon(R.drawable.edit);
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    listing_id = allMarkersMap.get(marker);
                    new DynamoDBManagerTask().execute(DynamoDBManagerType.RESERVE_LISTING);
                }
            });

            alertDialog.create().show();
        }
    }

    @Override
    public void onInfoWindowLongClick(final Marker marker) {
        if (!marker.getTitle().equals("Request Location")){
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchResultActivity.this);
            alertDialog.setTitle("Bookmark");
            alertDialog.setMessage("Add a bookmark for this listing?");
            alertDialog.setIcon(R.drawable.bookmark);
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    listing_id = allMarkersMap.get(marker);
                    new DynamoDBManagerTask().execute(DynamoDBManagerType.BOOKMARK_LISTING);
                }
            });

            alertDialog.create().show();
        }
    }

    /* Database */
    private enum DynamoDBManagerType {
        SEARCH_LISTING, RESERVE_LISTING, BOOKMARK_LISTING
    }

    private class DynamoDBManagerTaskResult {
        private DynamoDBManagerType taskType;
        private String tableStatus;
        private ArrayList<Listing> resultArray;
        private int resultReserveStatus;
        private int resultBookmarkStatus;

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

        public ArrayList<Listing> getResultArray(){
            return resultArray;
        }

        public void setResultArray(ArrayList<Listing> resultArray){
            this.resultArray = resultArray;
        }

        public int getResultReserveStatus(){
            return resultReserveStatus;
        }

        public void setResultReserveStatus(int resultReserveStatus){
            this.resultReserveStatus = resultReserveStatus;
        }

        public int getResultBookmarkStatus(){
            return resultBookmarkStatus;
        }

        public void setResultBookmarkStatus(int resultBookmarkStatus){
            this.resultBookmarkStatus = resultBookmarkStatus;
        }
    }

    private class DynamoDBManagerTask extends AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {

        protected DynamoDBManagerTaskResult doInBackground(DynamoDBManagerType... types) {

            String status1 = DynamoDBManager.getListingsTableStatus("search");
            String status2 = DynamoDBManager.getUsersTableStatus("search");
            String tableStatus = (status1.equalsIgnoreCase("ACTIVE") && status2.equalsIgnoreCase("ACTIVE")) ? "ACTIVE" : "";
            DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);

            if (types[0] == DynamoDBManagerType.SEARCH_LISTING) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultArray(DynamoDBManager.searchListing(username, longitude, latitude, startTime, endTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (types[0] == DynamoDBManagerType.RESERVE_LISTING) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultReserveStatus(DynamoDBManager.reserveListing(username, listing_id, startTime, endTime, "search"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (types[0] == DynamoDBManagerType.BOOKMARK_LISTING) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultBookmarkStatus(DynamoDBManager.bookmarkListing(username, listing_id, startTime, endTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return result;
        }

        protected void onPostExecute(DynamoDBManagerTaskResult result) {
            if (!result.getTableStatus().equalsIgnoreCase("ACTIVE")) {
                Toast.makeText(getApplicationContext(), "Database is not ready yet", Toast.LENGTH_SHORT).show();
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.SEARCH_LISTING) {
                ArrayList<Listing> ls = result.getResultArray();
                if (ls.size() == 0){
                    Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_SHORT).show();
                } else {
                    parkingSpots = ls;
                    for (Listing l : ls){
                        // Add a marker for each parking space
                        LatLng parkingLoc = new LatLng(l.getLatitude(), l.getLongitude());
                        Marker m = mMap.addMarker(new MarkerOptions()
                                .position(parkingLoc)
                                .title("$"+l.getPrice()+"/hr").snippet(l.getAddress()));
                        allMarkersMap.put(m, l.getId());
                        builder.include(parkingLoc);
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = 100; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cu);
                }
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.RESERVE_LISTING) {
                switch (result.getResultReserveStatus()){
                    case 0:
                        Toast.makeText(getApplicationContext(), "Successfully made a request", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "Failed to make a request", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.BOOKMARK_LISTING) {
                switch (result.getResultBookmarkStatus()){
                    case 0:
                        Toast.makeText(getApplicationContext(), "Successfully added a bookmark", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "Failed to add a bookmark", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
