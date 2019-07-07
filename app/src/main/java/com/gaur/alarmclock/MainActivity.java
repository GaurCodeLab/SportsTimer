package com.gaur.alarmclock;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

//import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.tabs.TabLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

//import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity {

private int[] imageResId={
    R.drawable.ic_timer,
    R.drawable.ic_stop_watch,
    R.drawable.ic_alarm_black_24dp

    };
    private int[] navLabels = {
            R.string.timer_tab,
            R.string.stop_watch_tab,
            R.string.alarm_tab
    };

    private int[] active_icon={
            R.drawable.ic_timer_red,
            R.drawable.ic_stop_watch_red,
            R.drawable.ic_alarm_red
    };

    MenuItem prevMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        final ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);
      //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Timer()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_timer:
                        pager.setCurrentItem(0);
                        break;

                    case R.id.nav_stopwatch:
                        pager.setCurrentItem(1);
                        break;

                    case R.id.nav_alarm:
                        pager.setCurrentItem(2);

                }
                return false;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    private class SectionPagerAdapter extends FragmentPagerAdapter{

        public SectionPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount(){
            return 3;
        }

        @Override

        public Fragment getItem(int position){
            switch (position){

                case 0: return new Timer();

                case 1:  return new StopWatch();

                case 2: return new Alarm();

            }

            return null;

        }



//        @Override
//
//        public  CharSequence getPageTitle(int position){
//
//            switch (position){
//
//                case 0: return getResources().getText(navLabels[0]);
//                case 1: return getResources().getText(navLabels[1]);
//                case 2: return getResources().getText(navLabels[2]);
//
//            }
//
//            return null;
//        }




    }


}
