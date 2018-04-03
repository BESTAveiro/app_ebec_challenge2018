package com.example.jmfs1.ebec.shop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.jmfs1.ebec.R;
import com.example.jmfs1.ebec.core.Order;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private DatabaseReference mDatabase;
    private ExpandableListView mOrdersListView;

    private List<Order> mOrders;
    private List<String> mKeys;

    private HistoryAdapter mOrdersAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleFragment.
     */
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_layout, container, false);

        // Connect to database
        SharedPreferences prefs = getContext().getSharedPreferences("LOGIN_PREFS", 0);
        prefs.edit();
        String team_id = prefs.getString("TEAM", "default");
        mDatabase = FirebaseDatabase.getInstance().getReference("history/" + team_id);

        // Get list view and adapter
        mOrders = new ArrayList();
        mKeys = new ArrayList();
        mOrdersListView = (ExpandableListView) view.findViewById(R.id.history_fragment_list_view);
        mOrdersAdapter = new HistoryAdapter(getContext(), mOrders);
        mOrdersListView.setAdapter(mOrdersAdapter);

        // Set data
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order order = dataSnapshot.getValue(Order.class);
                mOrders.add(order);

                String key = dataSnapshot.getKey();
                mKeys.add(key);

                mOrdersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Order order = dataSnapshot.getValue(Order.class);
                String key = dataSnapshot.getKey();

                int index = mKeys.indexOf(key);

                mOrders.set(index, order);

                mOrdersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Order order = dataSnapshot.getValue(Order.class);
                String key = dataSnapshot.getKey();

                int index = mKeys.indexOf(key);

                mOrders.remove(index);

                mOrdersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
