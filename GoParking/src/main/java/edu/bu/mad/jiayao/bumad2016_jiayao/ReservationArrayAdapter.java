package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ReservationArrayAdapter extends ArrayAdapter<ArrayList<String>> {
    private final Context context;
    private final ArrayList<ArrayList<String>> values;

    public ReservationArrayAdapter(Context context, ArrayList<ArrayList<String>> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ArrayList<String> info = values.get(position);

        View rowView = inflater.inflate(R.layout.row_reservation, parent, false);
        TextView mAddress = (TextView) rowView.findViewById(R.id.address);
        TextView mPrice = (TextView) rowView.findViewById(R.id.price);
        TextView mStartTime = (TextView) rowView.findViewById(R.id.startTime);
        TextView mEndTime = (TextView) rowView.findViewById(R.id.endTime);
        TextView mStatus = (TextView) rowView.findViewById(R.id.status);

        mAddress.setText(info.get(0));
        mPrice.setText("$"+info.get(1)+"/hr");
        mStartTime.setText(info.get(2));
        mEndTime.setText(info.get(3));
        mStatus.setText(info.get(4));

        LinearLayout layout = (LinearLayout)rowView.findViewById(R.id.info);
        layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+info.get(0).replaceAll(" ", "+"));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                }
            }
        });
        return rowView;
    }
}
