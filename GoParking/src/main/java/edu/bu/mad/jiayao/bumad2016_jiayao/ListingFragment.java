package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ListingFragment extends Fragment {

    public static final String ARG_USERNAME = "username";
    private String username = "";
    private ListView listview;
    public static AmazonClientManager clientManager = null;

    public ListingFragment() {
        // Required empty public constructor
    }

    public static ListingFragment newInstance(String param1) {
        ListingFragment fragment = new ListingFragment();
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
        View view = inflater.inflate(R.layout.fragment_listing, container, false);
        Button btn = (Button)view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).replaceFragments(NewListingFragment.class);
            }
        });

        listview = (ListView) view.findViewById(R.id.listView);
        clientManager = new AmazonClientManager(getActivity());
        new DynamoDBManagerTask().execute(DynamoDBManagerType.RETRIEVE_LISTING);
        return view;
    }

    /* Database */
    private enum DynamoDBManagerType {
        RETRIEVE_LISTING
    }

    private class DynamoDBManagerTaskResult {
        private DynamoDBManagerType taskType;
        private String tableStatus;
        private ArrayList<Listing> resultArray;

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
    }

    private class DynamoDBManagerTask extends AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {

        protected DynamoDBManagerTaskResult doInBackground(DynamoDBManagerType... types) {

            String status1 = DynamoDBManager.getListingsTableStatus("listing");
            String status2 = DynamoDBManager.getUsersTableStatus("listing");
            String tableStatus = (status1.equalsIgnoreCase("ACTIVE") && status2.equalsIgnoreCase("ACTIVE")) ? "ACTIVE" : "";
            DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);

            if (types[0] == DynamoDBManagerType.RETRIEVE_LISTING) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultArray(DynamoDBManager.retrieveListing(username));
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
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.RETRIEVE_LISTING) {
                ArrayList<Listing> l = result.getResultArray();
                if (l!=null){
                    ListingArrayAdapter adapter = new ListingArrayAdapter(getActivity(), l, username);
                    listview.setAdapter(adapter);
                }
            }
        }
    }
}