package com.example.jmfs1.ebec.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.jmfs1.ebec.R;
import com.example.jmfs1.ebec.core.Order;
import com.example.jmfs1.ebec.core.OrderEntry;

import java.util.List;

/**
 * Created by jeronimo on 21/12/2016.
 */

public class HistoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Order> orders;

    public HistoryAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return orders.get(groupPosition).getEntries().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = infalInflater.inflate(R.layout.history_order_entry_layout, null);

        // Get order entry
        OrderEntry orderEntry = (OrderEntry) getChild(groupPosition, childPosition);

        // Set product, quantity and cost
        TextView productTextView = (TextView) rowView.findViewById(R.id.history_order_entry_product);
        TextView quantityTextView = (TextView) rowView.findViewById(R.id.history_order_entry_quantity);
        TextView costTextView = (TextView) rowView.findViewById(R.id.history_order_entry_cost);

        if (!orderEntry.getProduct_name().isEmpty()) {
            productTextView.setText(orderEntry.getProduct_name());
        } else {
            productTextView.setText("--");
        }

        if (orderEntry.getQuantity() >= 0) {
            quantityTextView.setText(orderEntry.getQuantity() + " " + orderEntry.getUnits());
        } else {
            quantityTextView.setText("--");
        }

        if (orderEntry.getPrice() >= 0) {
            double totalCost = orderEntry.getPrice() * orderEntry.getQuantity();
            costTextView.setText(totalCost + " créditos");
        } else {
            costTextView.setText("--");
        }

        return rowView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return orders.get(groupPosition).getEntries().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orders.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return orders.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.history_order_layout, parent, false);

        // Get order
        Order order = (Order) getGroup(groupPosition);

        // Set date and cost
        TextView dateTextView = (TextView) rowView.findViewById(R.id.history_order_date);
        TextView costTextView = (TextView) rowView.findViewById(R.id.history_order_cost);

        if (!order.getDate().trim().isEmpty()) {
            dateTextView.setText(order.getDate());
        } else {
            dateTextView.setText("--");
        }

        if (order.getCost() >= 0) {
            costTextView.setText("Total: " + order.getCost() + " créditos");
        } else {
            costTextView.setText("--");
        }

        return rowView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
