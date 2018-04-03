package com.example.jmfs1.ebec.alerts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jmfs1.ebec.R;
import com.example.jmfs1.ebec.core.Alert;

import java.util.List;

/**
 * Created by jeronimo on 31/12/2016.
 */

public class AlertAdapter extends ArrayAdapter<Alert> {

    private Context context;
    public List<Alert> alerts;

    public AlertAdapter(Context context, List<Alert> alerts) {
        super(context, -1, alerts);
        this.context = context;
        this.alerts = alerts;
    }

    public int getCount()
    {
        return alerts.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.alert_item_layout, parent, false);

        // Get shop item
        Alert alert = alerts.get(position);

        // Set name, cost and quantity
        TextView messageTextView = (TextView) rowView.findViewById(R.id.alert_item_message);
        TextView dateTextView = (TextView) rowView.findViewById(R.id.alert_item_date);

        messageTextView.setText(alert.getText());
        dateTextView.setText(alert.getDate());

        return rowView;
    }
}
