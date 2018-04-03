package com.example.jmfs1.ebec.schedulefragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.TextView;
import android.util.Log;

import com.example.jmfs1.ebec.R;
import com.example.jmfs1.ebec.schedulefragment.classes.DaySchedule;

import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SchedulePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchedulePageFragment extends Fragment {

    private DaySchedule daySchedule;

    public SchedulePageFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance(DaySchedule daySchedule)
    {
        SchedulePageFragment fragment = new SchedulePageFragment();
        Bundle args = new Bundle();
        args.putSerializable("SCHEDULE_DAY", daySchedule);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            daySchedule = (DaySchedule) getArguments().getSerializable("SCHEDULE_DAY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dayschedule, container, false);

        Log.d("tag", "Number of events : " + daySchedule.getNumOfEvents());

        ExpandableListAdapter adapter = new ExpandableListAdapter(this.getContext(), daySchedule);

        ExpandableListView expandableListView = (ExpandableListView)  view.findViewById(R.id.listview_schedule);

        expandableListView.setAdapter(adapter);


        return view;
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private DaySchedule daySchedule;

        public ExpandableListAdapter(Context context, DaySchedule daySchedule) {
            this._context = context;
            this.daySchedule = daySchedule;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this.daySchedule.getByIndex(groupPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            DaySchedule.Event event = (DaySchedule.Event) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_schedule, null);
            }

            TextView txtDescription = (TextView) convertView
                    .findViewById(R.id.lblDescription);

            txtDescription.setText(event.getDescription());
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.daySchedule.getByIndex(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.daySchedule.getNumOfEvents();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            DaySchedule.Event event = (DaySchedule.Event) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_schedule, null);
            }

            TextView lblListName = (TextView) convertView
                    .findViewById(R.id.lblName);
            lblListName.setText(event.getName());

            TextView lblListStartHour = (TextView) convertView
                    .findViewById(R.id.lblStartHour);
            lblListStartHour.setText(event.getStartHour());

            TextView lblListFinishHour = (TextView) convertView
                    .findViewById(R.id.lblFinishHour);
            lblListFinishHour.setText(event.getFinishHour());

            return convertView;
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
}
