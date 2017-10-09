package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NotificationArrayAdapter extends ArrayAdapter<ArrayList<String>> {
    private final Context context;
    private final ArrayList<ArrayList<String>> values;
    private String username = "";
    private int removePosition = -1;
    private int approvePosition = -1;

    public NotificationArrayAdapter(Context context, ArrayList<ArrayList<String>> values, String username) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.username = username;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<String> info = values.get(position);

        View rowView = inflater.inflate(R.layout.row_notification, parent, false);
        TextView mMessage = (TextView) rowView.findViewById(R.id.message);
        mMessage.setText(info.get(4));

        Button rejectBtn = (Button)rowView.findViewById(R.id.reject_btn);
        rejectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (removePosition == -1){
                    removePosition = position;
                    new DynamoDBManagerTask().execute(DynamoDBManagerType.REMOVE_NOTIFICATION);
                }
            }
        });

        Button approveBtn = (Button)rowView.findViewById(R.id.approve_btn);
        approveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (approvePosition == -1){
                    approvePosition = position;
                    new DynamoDBManagerTask().execute(DynamoDBManagerType.APPROVE_REQUEST);
                }
            }
        });

        return rowView;
    }

    /* Database */
    private enum DynamoDBManagerType {
        REMOVE_NOTIFICATION, APPROVE_REQUEST
    }

    private class DynamoDBManagerTaskResult {
        private DynamoDBManagerType taskType;
        private String tableStatus;
        private int resultRemoveStatus;
        private int resultApproveStatus;

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

        public int getResultApproveStatus(){
            return resultApproveStatus;
        }

        public void setResultApproveStatus(int resultApproveStatus){
            this.resultApproveStatus = resultApproveStatus;
        }
    }

    private class DynamoDBManagerTask extends AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {

        protected DynamoDBManagerTaskResult doInBackground(DynamoDBManagerType... types) {

            String status1 = DynamoDBManager.getListingsTableStatus("notification");
            String status2 = DynamoDBManager.getUsersTableStatus("notification");
            String tableStatus = (status1.equalsIgnoreCase("ACTIVE") && status2.equalsIgnoreCase("ACTIVE")) ? "ACTIVE" : "";
            DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);

            if (types[0] == DynamoDBManagerType.REMOVE_NOTIFICATION) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultRemoveStatus(DynamoDBManager.removeNotification(username, removePosition,
                                values.get(removePosition).get(0),
                                values.get(removePosition).get(1),
                                values.get(removePosition).get(2),
                                values.get(removePosition).get(3)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else if (types[0] == DynamoDBManagerType.APPROVE_REQUEST) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultApproveStatus(DynamoDBManager.approveRequest(username, approvePosition,
                                values.get(approvePosition).get(0),
                                values.get(approvePosition).get(1),
                                values.get(approvePosition).get(2),
                                values.get(approvePosition).get(3)));
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
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.REMOVE_NOTIFICATION) {
                switch (result.getResultRemoveStatus()){
                    case 0:
                        Toast.makeText(context, "Successfully rejected a request", Toast.LENGTH_SHORT).show();
                        values.remove(removePosition);
                        removePosition = -1;
                        notifyDataSetChanged();
                        break;
                    case 1:
                        Toast.makeText(context, "Cannot reject this request", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.APPROVE_REQUEST) {
                switch (result.getResultApproveStatus()){
                    case 0:
                        Toast.makeText(context, "Successfully approved a request", Toast.LENGTH_SHORT).show();
                        values.remove(approvePosition);
                        approvePosition = -1;
                        notifyDataSetChanged();
                        break;
                    case 1:
                        Toast.makeText(context, "Cannot approve this request", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
