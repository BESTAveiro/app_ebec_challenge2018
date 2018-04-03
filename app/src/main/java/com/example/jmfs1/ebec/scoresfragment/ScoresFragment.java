package com.example.jmfs1.ebec.scoresfragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jmfs1.ebec.R;
import com.example.jmfs1.ebec.core.MiniCompetition;
import com.example.jmfs1.ebec.core.Team;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoresFragment extends Fragment {

    private DatabaseReference mDatabase;

    List<NLevelItem> list;
    ListView listView;

    private List<Team> mTeams;
    private List<String> mMembers;
    private List<MiniCompetition> mMiniProvas;
    private List<String> mKeys;

    private List<String> staticTeams;
    private List<String> staticScores;

    public ScoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("teams");
        final String modality = (String) getArguments().get("modality");

        final List<String> staticList = new ArrayList<>();
        staticList.add("Membros");
        staticList.add("Mini Provas");

        mTeams = new ArrayList();
        mKeys = new ArrayList();
        list = new ArrayList<>();
        staticScores = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_scores, container, false);
        listView = (ListView) view.findViewById(R.id.listView1);

        final LayoutInflater inflater_groups = LayoutInflater.from(getActivity());

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Scores", dataSnapshot.getChildrenCount()+"");

                Iterator it = dataSnapshot.getChildren().iterator();
                while(it.hasNext())
                {
                    Log.d("scores", it.next().toString());
                }

                Team team = dataSnapshot.getValue(Team.class);

                if (!team.getModality().equalsIgnoreCase(modality)) {
                    return;
                }

                mTeams.add(team);

                String key = dataSnapshot.getKey();
                mKeys.add(key);

                final NLevelItem grandParent = new NLevelItem(team, null, new NLevelView() {
                    @Override
                    public View getView(NLevelItem item) {
                        View view = inflater_groups.inflate(R.layout.list_item, null);
                        view.setBackgroundColor(Color.parseColor("#294F66"));
                        TextView tname = (TextView) view.findViewById(R.id.teamname);
                        TextView tcredits = (TextView) view.findViewById(R.id.teamcredits);
                        Team t = (Team) item.getWrappedObject();
                        String mTeamName = t.getName();
                        String mTeamCredits = Double.toString(t.getCredits());
                        tname.setText(mTeamName);
                        tcredits.setText(mTeamCredits);
                        return view;
                    }
                });
                list.add(grandParent);

                NLevelItem parentMem = new NLevelItem(staticList.get(0), grandParent, new NLevelView() {
                    @Override
                    public View getView(NLevelItem item) {
                        View view = inflater_groups.inflate(R.layout.list_item, null);
                        view.setBackgroundColor(Color.parseColor("#326AA5"));
                        TextView tv = (TextView) view.findViewById(R.id.teamname);
                        String name = "     " + (String) item.getWrappedObject();
                        tv.setText(name);
                        return view;
                    }
                });
                list.add(parentMem);

                for (String teamMember : team.getParticipants()) {
                    NLevelItem childMem = new NLevelItem(teamMember, parentMem, new NLevelView() {
                        @Override
                        public View getView(NLevelItem item) {
                            View view = inflater_groups.inflate(R.layout.list_item, null);
                            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            TextView tv = (TextView) view.findViewById(R.id.teamname);
                            String name = "             " + (String) item.getWrappedObject();
                            tv.setText(name);
                            tv.setTextColor(Color.parseColor("#000000"));
                            return view;
                        }
                    });

                    list.add(childMem);
                }

                NLevelItem parentCpt = new NLevelItem(staticList.get(1), grandParent, new NLevelView() {
                    @Override
                    public View getView(NLevelItem item) {
                        View view = inflater_groups.inflate(R.layout.list_item, null);
                        view.setBackgroundColor(Color.parseColor("#326AA5"));
                        TextView tv = (TextView) view.findViewById(R.id.teamname);
                        String name = "     " + (String) item.getWrappedObject();
                        tv.setText(name);
                        return view;
                    }
                });
                list.add(parentCpt);

                if (team.getMini_competitions() != null) {
                    for (MiniCompetition minicpt : team.getMini_competitions()) {
                        NLevelItem childCpt = new NLevelItem(minicpt, parentCpt, new NLevelView() {
                            @Override
                            public View getView(NLevelItem item) {
                                View view = inflater_groups.inflate(R.layout.list_item, null);
                                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                TextView tv = (TextView) view.findViewById(R.id.teamname);
                                MiniCompetition mcpt = (MiniCompetition) item.getWrappedObject();
                                String name = "             " + mcpt.getName() + ": " + mcpt.getCredits();
                                tv.setText(name);
                                tv.setTextColor(Color.parseColor("#000000"));
                                return view;
                            }
                        });

                        list.add(childCpt);
                    }
                }

                final NLevelAdapter adapter = new NLevelAdapter(list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        ((NLevelAdapter) listView.getAdapter()).toggle(arg2);
                        ((NLevelAdapter) listView.getAdapter()).getFilter().filter();

                    }
                });

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                /*Team team = dataSnapshot.getValue(Team.class);

                String key = dataSnapshot.getKey();

                int index = mKeys.indexOf(key);
                mTeams.set(index, team);

                final NLevelItem grandParent = new NLevelItem(team, null, new NLevelView() {
                    @Override
                    public View getView(NLevelItem item) {
                        View view = inflater_groups.inflate(R.layout.list_item, null);
                        TextView tname = (TextView) view.findViewById(R.id.teamname);
                        TextView tcredits = (TextView) view.findViewById(R.id.teamcredits);
                        Team t = (Team) item.getWrappedObject();
                        String mTeamName = t.getName();
                        String mTeamCredits = Integer.toString(t.getCredits());
                        tname.setText(mTeamName);
                        tcredits.setText(mTeamCredits);
                        return view;
                    }
                });
                list.add(grandParent);

                NLevelItem parentMem = new NLevelItem(staticList.get(0), grandParent, new NLevelView() {
                    @Override
                    public View getView(NLevelItem item) {
                        View view = inflater_groups.inflate(R.layout.list_item, null);
                        TextView tv = (TextView) view.findViewById(R.id.teamname);
                        String name = "     " + (String) item.getWrappedObject();
                        tv.setText(name);
                        return view;
                    }
                });
                list.add(parentMem);

                for (String teamMember : team.getParticipants()) {
                    NLevelItem childMem = new NLevelItem(teamMember, parentMem, new NLevelView() {
                        @Override
                        public View getView(NLevelItem item) {
                            View view = inflater_groups.inflate(R.layout.list_item, null);
                            TextView tv = (TextView) view.findViewById(R.id.teamname);
                            String name = "             " + (String) item.getWrappedObject();
                            tv.setText(name);
                            return view;
                        }
                    });

                    list.add(childMem);
                }

                NLevelItem parentCpt = new NLevelItem(staticList.get(1), grandParent, new NLevelView() {
                    @Override
                    public View getView(NLevelItem item) {
                        View view = inflater_groups.inflate(R.layout.list_item, null);
                        TextView tv = (TextView) view.findViewById(R.id.teamname);
                        String name = "     " + (String) item.getWrappedObject();
                        tv.setText(name);
                        return view;
                    }
                });
                list.add(parentCpt);

                for (MiniCompetition minicpt : team.getMini_competitions()) {
                    NLevelItem childCpt = new NLevelItem(minicpt, parentCpt, new NLevelView() {
                        @Override
                        public View getView(NLevelItem item) {
                            View view = inflater_groups.inflate(R.layout.list_item, null);
                            TextView tv = (TextView) view.findViewById(R.id.teamname);
                            MiniCompetition mcpt = (MiniCompetition) item.getWrappedObject();
                            String name = "             " + mcpt.getName() + ": " + mcpt.getCredits();
                            tv.setText(name);
                            return view;
                        }
                    });

                    list.add(childCpt);
                }

                final NLevelAdapter adapter = new NLevelAdapter(list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        ((NLevelAdapter) listView.getAdapter()).toggle(arg2);
                        ((NLevelAdapter) listView.getAdapter()).getFilter().filter();

                    }
                });

                adapter.notifyDataSetChanged();*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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