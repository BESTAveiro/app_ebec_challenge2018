package com.example.jmfs1.ebec.shop;

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
import com.example.jmfs1.ebec.core.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private DatabaseReference mDatabase;
    private ListView mProductsListView;

    private List<Product> mProducts;
    private List<String> mKeys;

    private ShopAdapter mProductsAdapter;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleFragment.
     */
    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
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
        View view = inflater.inflate(R.layout.fragment_shop_layout, container, false);

        // Connect to database
        mDatabase = FirebaseDatabase.getInstance().getReference("products");
        final String category = (String) getArguments().get("category");
        Log.d("Category", category);

        // Get list view and adapter
        mProducts = new ArrayList();
        mKeys = new ArrayList();
        mProductsListView = (ListView) view.findViewById(R.id.shop_fragment_list_view);
        mProductsAdapter = new ShopAdapter(getContext(), mProducts);
        mProductsListView.setAdapter(mProductsAdapter);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);

                if (product == null || category == null|| product.getCategory()== null) {
                    return;
                }

                if (!category.equalsIgnoreCase("Todos") && !product.getCategory().equalsIgnoreCase(category)) {
                    return;
                }

                mProducts.add(product);

                String key = dataSnapshot.getKey();
                mKeys.add(key);
                mProductsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);

                if (!category.equalsIgnoreCase("Todos") && !product.getCategory().equalsIgnoreCase(category)) {
                    return;
                }

                String key = dataSnapshot.getKey();

                int index = mKeys.indexOf(key);

                mProducts.set(index, product);

                mProductsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);

                if (!category.equalsIgnoreCase("Todos") && !product.getCategory().equalsIgnoreCase(category)) {
                    return;
                }

                String key = dataSnapshot.getKey();

                int index = mKeys.indexOf(key);

                mProducts.remove(index);

                mProductsAdapter.notifyDataSetChanged();
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

    public void onViewCreated(View paramView, Bundle paramBundle) {
        super.onViewCreated(paramView, paramBundle);

        FloatingActionButton mHistoryButton = (FloatingActionButton) paramView.findViewById(R.id.shop_fragment_history_button);

        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.frame, new HistoryFragment()).addToBackStack(null).commit();

            }
        });
    }
}
