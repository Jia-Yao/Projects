package edu.bu.mad.jiayao.bumad2016_jiayao;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookmarkArrayAdapter extends ArrayAdapter<ArrayList<String>> {
    private final Context context;
    private final ArrayList<ArrayList<String>> values;
    private String username = "";
    private int removePosition = -1;
    private int requestPosition = -1;

    public BookmarkArrayAdapter(Context context, ArrayList<ArrayList<String>> values, String username) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.username = username;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ArrayList<String> info = values.get(position);

        View rowView = inflater.inflate(R.layout.row_bookmark, parent, false);
        TextView mAddress = (TextView) rowView.findViewById(R.id.address);
        TextView mPrice = (TextView) rowView.findViewById(R.id.price);
        TextView mStartTime = (TextView) rowView.findViewById(R.id.startTime);
        TextView mEndTime = (TextView) rowView.findViewById(R.id.endTime);

        mAddress.setText(info.get(0));
        mPrice.setText("$"+info.get(1)+"/hr");
        mStartTime.setText(info.get(2));
        mEndTime.setText(info.get(3));

        Button deleteBtn = (Button)rowView.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (removePosition == -1){
                    removePosition = position;
                    new DynamoDBManagerTask().execute(DynamoDBManagerType.REMOVE_BOOKMARK);
                }
            }
        });

        Button requestBtn = (Button)rowView.findViewById(R.id.request_btn);
        requestBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (requestPosition == -1){
                    requestPosition = position;
                    new DynamoDBManagerTask().execute(DynamoDBManagerType.RESERVE_LISTING);
                }
            }
        });

        LinearLayout layout = (LinearLayout)rowView.findViewById(R.id.info);
        layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+info.get(0).replaceAll(" ", "+"));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                }
            }
        });
        return rowView;
    }

    /* Database */
    private enum DynamoDBManagerType {
        REMOVE_BOOKMARK, RESERVE_LISTING
    }

    private class DynamoDBManagerTaskResult {
        private DynamoDBManagerType taskType;
        private String tableStatus;
        private int resultRemoveStatus;
        private int resultRequestStatus;

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

        public int getResultRemoveStatus(){
            return resultRemoveStatus;
        }

        public void setResultRemoveStatus(int resultRemoveStatus){
            this.resultRemoveStatus = resultRemoveStatus;
        }

        public int getResultRequestStatus(){
            return resultRequestStatus;
        }

        public void setResultRequestStatus(int resultRequestStatus){
            this.resultRequestStatus = resultRequestStatus;
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

            if (types[0] == DynamoDBManagerType.REMOVE_BOOKMARK) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultRemoveStatus(DynamoDBManager.removeBookmark(username, removePosition));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (types[0] == DynamoDBManagerType.RESERVE_LISTING) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultRequestStatus(DynamoDBManager.reserveListing(username,
                                values.get(requestPosition).get(4),
                                values.get(requestPosition).get(2),
                                values.get(requestPosition).get(3),
                                "bookmark"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            return result;
        }

        protected void onPostExecute(DynamoDBManagerTaskResult result) {

            if (!result.getTableStatus().equalsIgnoreCase("ACTIVE")) {
                Toast.makeText(context, "Database is not ready yet", Toast.LENGTH_SHORT).show();
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.REMOVE_BOOKMARK) {
                switch (result.getResultRemoveStatus()){
                    case 0:
                        Toast.makeText(context, "Successfully deleted a bookmark", Toast.LENGTH_SHORT).show();
                        values.remove(removePosition);
                        removePosition = -1;
                        notifyDataSetChanged();
                        break;
                    case 1:
                        Toast.makeText(context, "Failed to delete this bookmark", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.RESERVE_LISTING) {
                switch (result.getResultRequestStatus()){
                    case 0:
                        Toast.makeText(context, "Successfully made a request", Toast.LENGTH_SHORT).show();
                        if (removePosition == -1){
                            removePosition = requestPosition;
                            new DynamoDBManagerTask().execute(DynamoDBManagerType.REMOVE_BOOKMARK);
                        }
                        requestPosition = -1;
                        break;
                    case 1:
                        Toast.makeText(context, "Failed to make a request", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
