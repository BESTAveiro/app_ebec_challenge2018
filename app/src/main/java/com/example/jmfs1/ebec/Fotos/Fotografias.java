package com.example.jmfs1.ebec.Fotos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jmfs1.ebec.R;

import java.util.ArrayList;
import java.util.List;

public class Fotografias extends Fragment {

    static final String className = "Fotografias";
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View myView =inflater.inflate(R.layout.activity_fotografias, container, false);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fotografias);

       // toolbar = (Toolbar) myView.findViewById(R.id.toolbar);
        //getSupportActionBar().setTitle("Fotografias");
        //viewPager = (ViewPager) myView.findViewById(R.id.viewPager);
        //setupViewPager(viewPager);

        //tabLayout = (TabLayout) myView.findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(viewPager);

        return myView;
    }

//    public void gototugaBox(View view) { goToUrl("https://www.tugabox.pt/?lang=en-en");    }
//
//    public void gotoitGrow(View view) { goToUrl("www.itgrow.pt/");    }
//
//    public void gotoshoyce(View view) { goToUrl("shoyce.pt/pt/");    }
//
//    public void gotomirtiflor(View view) { goToUrl("https://pt-pt.facebook.com/Mirtiflor-745004355521875/");    }
//
//    public void gotodiatosta (View view) { goToUrl("http://diatosta.pt/pt/");   }
//
//
//    public void gotoua(View view) {
//        goToUrl("https://www.ua.pt/");
//    }
//
//    public void gotoipdj(View view) {
//        goToUrl("http://www.ipdj.pt/");
//    }
//
//    public void gotoaauav(View view) {
//        goToUrl("http://www.aauav.pt/");
//    }
//
//
//    private void goToUrl(String url) {
//        Uri uriUrl = Uri.parse(url);
//        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
//        startActivity(launchBrowser);
//    }

//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
//        adapter.addFragment(new MinhasFotos(), "Minhas");
//        adapter.addFragment(new OutrasFotos(), "Comunidade");
//        viewPager.setAdapter(adapter);
//    }
//
//    class ViewPagerAdapter extends FragmentStatePagerAdapter
//    {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }
}
