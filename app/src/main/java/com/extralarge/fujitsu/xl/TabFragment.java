package com.extralarge.fujitsu.xl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.extralarge.fujitsu.xl.NewsSection.International;
import com.extralarge.fujitsu.xl.NewsSection.MainNews;
import com.extralarge.fujitsu.xl.NewsSection.National;
import com.extralarge.fujitsu.xl.NewsSection.State;

/**
 * Created by Fujitsu on 28/04/2017.
 */

public class TabFragment  extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 5 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

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
                case 0 :MainNews dashfab = new MainNews();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().commit();
                   // Log.d("tab0123", String.valueOf(strtext));
                    return dashfab;
                case 1 : return new National();
                case 2 : return new International();
                case 3 : return new State();
                case 4 : return new National();

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
                    return "HOME";
                case 1 :
                    return "NATIONAL";
                case 2 :
                    return "INTERNATIONAL";
                case 3 :
                    return "STATE";
                case 4 :
                    return "BUSINESS";
            }
            return null;
        }
    }

}


