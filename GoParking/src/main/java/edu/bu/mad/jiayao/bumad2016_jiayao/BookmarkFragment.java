package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class BookmarkFragment extends Fragment {

    public static final String ARG_USERNAME = "username";
    private String username = "";
    private ListView listview;
    public static AmazonClientManager clientManager = null;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment newInstance(String param1) {
        BookmarkFragment fragment = new BookmarkFragment();
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
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        listview = (ListView) view.findViewById(R.id.listView);
        clientManager = new AmazonClientManager(getActivity());
        new DynamoDBManagerTask().execute(DynamoDBManagerType.RETRIEVE_BOOKMARK);
        return view;
    }

    /* Database */
    private enum DynamoDBManagerType {
        RETRIEVE_BOOKMARK
    }

    private class DynamoDBManagerTaskResult {
        private DynamoDBManagerType taskType;
        private String tableStatus;
        private ArrayList<ArrayList<String>> resultArray;

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

        public ArrayList<ArrayList<String>> getResultArray(){
            return resultArray;
        }

        public void setResultArray(ArrayList<ArrayList<String>> resultArray){
            this.resultArray = resultArray;
        }
    }

    private class DynamoDBManagerTask extends AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {

        protected DynamoDBManagerTaskResult doInBackground(DynamoDBManagerType... types) {

            String status1 = DynamoDBManager.getListingsTableStatus("bookmark");
            String status2 = DynamoDBManager.getUsersTableStatus("bookmark");
            String tableStatus = (status1.equalsIgnoreCase("ACTIVE") && status2.equalsIgnoreCase("ACTIVE")) ? "ACTIVE" : "";
            DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);

            if (types[0] == DynamoDBManagerType.RETRIEVE_BOOKMARK) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultArray(DynamoDBManager.retrieveBookmark(username));
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
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.RETRIEVE_BOOKMARK) {
                ArrayList<ArrayList<String>> info = result.getResultArray();
                if (info!=null){
                    BookmarkArrayAdapter adapter = new BookmarkArrayAdapter(getActivity(), info, username);
                    listview.setAdapter(adapter);
                }
            }
        }
    }

}