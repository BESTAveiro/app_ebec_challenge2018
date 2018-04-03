package com.example.jmfs1.ebec.shop;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jmfs1.ebec.R;

public class ShopCategoriesFragment extends Fragment {

    private FragmentTabHost mTabHost;

    public ShopCategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment.
     */
    public static ShopCategoriesFragment newInstance() {
        ShopCategoriesFragment fragment = new ShopCategoriesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shop_categories_layout, container, false);

        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        Bundle bundle = new Bundle();

        bundle.putString("category", "Todos");
        mTabHost.addTab(mTabHost.newTabSpec("Todos").setIndicator("Todos"), ShopFragment.class, bundle);

        bundle = new Bundle();
        bundle.putString("category", "Card");
        mTabHost.addTab(mTabHost.newTabSpec("Cartão").setIndicator("Cartão"), ShopFragment.class, bundle);

        bundle = new Bundle();
        bundle.putString("category", "Electronic");
        mTabHost.addTab(mTabHost.newTabSpec("Eletrónico").setIndicator("Eletrónico"), ShopFragment.class, bundle);

        bundle = new Bundle();
        bundle.putString("category", "Wood");
        mTabHost.addTab(mTabHost.newTabSpec("Madeiras").setIndicator("Madeiras"), ShopFragment.class, bundle);

        bundle = new Bundle();
        bundle.putString("category", "Metal");
        mTabHost.addTab(mTabHost.newTabSpec("Metal").setIndicator("Metal"), ShopFragment.class, bundle);

        bundle = new Bundle();
        bundle.putString("category", "Plastic");
        mTabHost.addTab(mTabHost.newTabSpec("Plástico").setIndicator("Plástico"), ShopFragment.class, bundle);

        bundle = new Bundle();
        bundle.putString("category", "Undifferentiated");
        mTabHost.addTab(mTabHost.newTabSpec("Indiferenciados").setIndicator("Indiferenciados"), ShopFragment.class, bundle);

        return rootView;
    }
}
