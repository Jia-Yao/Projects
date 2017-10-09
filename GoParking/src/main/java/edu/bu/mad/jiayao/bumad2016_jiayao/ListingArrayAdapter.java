package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListingArrayAdapter extends ArrayAdapter<Listing> {
    private final Context context;
    private final ArrayList<Listing> values;
    private String username = "";
    private int removePosition = -1;

    public ListingArrayAdapter(Context context, ArrayList<Listing> values, String username) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.username = username;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Listing l = values.get(position);

        View rowView = inflater.inflate(R.layout.row_listing, parent, false);
        TextView mAddress = (TextView) rowView.findViewById(R.id.address);
        TextView mPrice = (TextView) rowView.findViewById(R.id.price);
        TextView mStartTime = (TextView) rowView.findViewById(R.id.startTime);
        TextView mEndTime = (TextView) rowView.findViewById(R.id.endTime);

        mAddress.setText(l.getAddress());
        mPrice.setText("$"+l.getPrice()+"/hr");
        mStartTime.setText(l.getAvailableStarting());
        mEndTime.setText(l.getAvailableUntil());

        Button deleteBtn = (Button)rowView.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (removePosition == -1){
                    removePosition = position;
                    new DynamoDBManagerTask().execute(DynamoDBManagerType.REMOVE_LISTING);
                }
            }
        });

        LinearLayout layout = (LinearLayout)rowView.findViewById(R.id.info);
        layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Guests approved by you:");
                CharSequence userList = "";
                for (String u : values.get(position).getApproved()){
                    userList = userList + u + "\n";
                }
                alertDialog.setMessage(userList);
                alertDialog.setIcon(R.drawable.ok);
                alertDialog.create().show();
            }
        });

        return rowView;
    }

    /* Database */
    private enum DynamoDBManagerType {
        REMOVE_LISTING
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

            String status1 = DynamoDBManager.getListingsTableStatus("listing");
            String status2 = DynamoDBManager.getUsersTableStatus("listing");
            String tableStatus = (status1.equalsIgnoreCase("ACTIVE") && status2.equalsIgnoreCase("ACTIVE")) ? "ACTIVE" : "";
            DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);

            if (types[0] == DynamoDBManagerType.REMOVE_LISTING) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        String listingid = values.get(removePosition).getId();
                        result.setResultStatus(DynamoDBManager.removeListing(username, listingid));
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
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.REMOVE_LISTING) {
                switch (result.getResultStatus()){
                    case 0:
                        Toast.makeText(context, "Successfully deleted a listing", Toast.LENGTH_SHORT).show();
                        values.remove(removePosition);
                        removePosition = -1;
                        notifyDataSetChanged();
                        break;
                    case 1:
                        Toast.makeText(context, "Cannot delete this listing", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }
    }
}
