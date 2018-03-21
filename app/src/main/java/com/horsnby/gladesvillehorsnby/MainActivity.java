package com.horsnby.gladesvillehorsnby;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseVC {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FrameLayout frmL;
    private TextView mTitle;
    private String[] titles = {"Noticeboard", "Groups", "Events", "Profile"};


    private int[] tabIcons = {
            R.drawable.noticeboard_empty,
            R.drawable.groups_empty,
            R.drawable.events_empty,
            R.drawable.profile_empty
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_top);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("Noticeboard");


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        frmL = findViewById(R.id.frm);
        viewPager = findViewById(R.id.viewpager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(titles[position]);

                switch (position)
                {
                    case 0:
                        frmL.setVisibility(View.GONE);
                        mTitle.setText("Noticeboard");

                        break;
                    case 1:
                        frmL.setVisibility(View.GONE);
                        mTitle.setText("Groups");

                        break;
                    case 2:
                        frmL.setVisibility(View.GONE);
                        mTitle.setText("Events");

                        break;
                    case 3:
                        frmL.setVisibility(View.VISIBLE);
                        mTitle.setText("Profile");

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        setupIcon();
    }

    private void setupIcon() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);



        Resources res1 = getResources();
        int primaryColor = res1.getColor(R.color.tab_background_unselected);
        int second = res1.getColor(R.color.tab_background_selected);


        tabLayout.getTabAt(0).getIcon().setColorFilter(second, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Resources res = getResources();
                int primaryColor1 = res.getColor(R.color.tab_background_selected);
                tab.getIcon().setColorFilter(primaryColor1, PorterDuff.Mode.SRC_IN);

                /*if (tab.getPosition()==0){
                    tabLayout.getTabAt(0).setIcon(R.drawable.noticeboard_hover);
                    return;
                }
                if (tab.getPosition()==1){
                    tabLayout.getTabAt(1).setIcon(R.drawable.groups_hover);
                    return;
                }
                if (tab.getPosition()==2){
                    tabLayout.getTabAt(2).setIcon(R.drawable.events_hover);
                    return;
                }
                if (tab.getPosition()==3){
                    tabLayout.getTabAt(3).setIcon(R.drawable.profile_hover);
                    return;
                }*/

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //for removing the color of first icon when switched to next tab
                tabLayout.getTabAt(0).getIcon().clearColorFilter();
                tab.getIcon().clearColorFilter();
                /*if (tab.getPosition()==0){
                    tabLayout.getTabAt(0).setIcon(R.drawable.noticeboard_empty);
                    return;
                }
                if (tab.getPosition()==1){
                    tabLayout.getTabAt(1).setIcon(R.drawable.groups_empty);
                    return;
                }
                if (tab.getPosition()==2){
                    tabLayout.getTabAt(2).setIcon(R.drawable.events_empty);
                    return;
                }
                if (tab.getPosition()==3){
                    tabLayout.getTabAt(3).setIcon(R.drawable.profile_empty);
                    return;
                }*/
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new NoticeboardFragment(), "NOTICEBOARD");
        adapter.addFrag(new GroupFragment(), "GROUPS");
        adapter.addFrag(new EventsFragment(), "EVENTS");
        adapter.addFrag(new ProfileFragment(), "PROFILE");
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
}
