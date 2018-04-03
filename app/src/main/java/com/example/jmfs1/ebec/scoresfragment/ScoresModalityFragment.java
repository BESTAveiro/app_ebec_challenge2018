package com.example.jmfs1.ebec.scoresfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jmfs1.ebec.R;

/**
 * Created by miguel on 11-03-2017.
 */

public class ScoresModalityFragment extends Fragment {
    private FragmentTabHost mTabHost;

    public ScoresModalityFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment.
     */
    public static ScoresModalityFragment newInstance() {
        ScoresModalityFragment fragment = new ScoresModalityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_scores_modality, container, false);

        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        Bundle bundle = new Bundle();

        bundle.putString("modality", "TD");
        mTabHost.addTab(mTabHost.newTabSpec("Team Design").setIndicator("Team Design"), ScoresFragment.class, bundle);

        bundle = new Bundle();
        bundle.putString("modality", "CS");
        mTabHost.addTab(mTabHost.newTabSpec("Case Study").setIndicator("Case Study"), ScoresFragment.class, bundle);

        return rootView;
    }
}
