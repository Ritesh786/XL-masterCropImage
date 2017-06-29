package com.extralarge.fujitsu.xl.ReporterSection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.extralarge.fujitsu.xl.R;

/**
 * Created by Fujitsu on 28/04/2017.
 */

public class tabdesign  extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 4 ;
    int strtext;
    Bundle bundle,bundle1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.tabdesign,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs123);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager123);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });


        bundle = this.getArguments();
        strtext = bundle.getInt("message",0);
        Log.d("idv", String.valueOf(strtext));

      bundle1 = new Bundle();
        bundle1.putInt("message",strtext);

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {




        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){

                case 0 :DashboardFragment dashfab = new DashboardFragment();
                    FragmentManager manager = getFragmentManager();
                    dashfab.setArguments(bundle1);
                    manager.beginTransaction().commit();
                    Log.d("tab0123", String.valueOf(strtext));
                    return dashfab;

                case 1 :  PendingNews pendfrag = new PendingNews();
                FragmentManager manager12 = getFragmentManager();
                    pendfrag.setArguments(bundle1);
                manager12.beginTransaction().commit();
                Log.d("tab0123", String.valueOf(strtext));
                return pendfrag;

                case 2 :  VerifiedNews verfeb = new VerifiedNews();
                FragmentManager manager1 = getFragmentManager();
                verfeb.setArguments(bundle1);
                manager1.beginTransaction().commit();
                Log.d("tab0123", String.valueOf(strtext));
                return verfeb;

                case 3 : RejectedNews rejectedNews = new RejectedNews();
                    FragmentManager manager2 = getFragmentManager();
                    rejectedNews.setArguments(bundle1);
                    manager2.beginTransaction().commit();
                    Log.d("tab0123", String.valueOf(strtext));
                    return rejectedNews;
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Upload News";
                case 1 :
                    return "Pending News";
                case 2 :
                    return "Verified News";
                case 3 :
                    return "Rejected News";

            }
            return null;
        }
    }

}


