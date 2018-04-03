package com.example.jmfs1.ebec.alerts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jmfs1.ebec.R;
import com.example.jmfs1.ebec.core.Alert;

import java.util.ArrayList;
import java.util.List;

public class AlertFragment extends Fragment {

    private ListView mAlertsListView;

    private List<Alert> mAlerts;

    private AlertAdapter mAlertsAdapter;

    public AlertFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleFragment.
     */
    public static AlertFragment newInstance() {
        AlertFragment fragment = new AlertFragment();
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
        View view = inflater.inflate(R.layout.fragment_alert_layout, container, false);

        // Get list view and adapter
        mAlerts = new ArrayList();
        mAlertsListView = (ListView) view.findViewById(R.id.alerts_fragment_list_view);
        mAlertsAdapter = new AlertAdapter(getContext(), mAlerts);
        mAlertsListView.setAdapter(mAlertsAdapter);

        mAlerts.addAll(Alert.listAll(Alert.class));

        Log.d("Alerts", "" + mAlerts.size());

        mAlertsAdapter.notifyDataSetChanged();

        return view;
    }

}
