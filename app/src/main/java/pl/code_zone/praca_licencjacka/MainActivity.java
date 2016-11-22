package pl.code_zone.praca_licencjacka;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import pl.code_zone.praca_licencjacka.fragments.BoardFragment;
import pl.code_zone.praca_licencjacka.fragments.EventsFragment;
import pl.code_zone.praca_licencjacka.fragments.UserFragment;
import pl.code_zone.praca_licencjacka.utils.ActivityUtils;
import pl.code_zone.praca_licencjacka.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    // UI
    TabLayout mTabLayout;
    RelativeLayout mRelativeLayout;
    ViewPager mViewPager;
    PagerAdapter mFragmentPagerAdapter;
    FloatingActionsMenu mFam;
    FloatingActionButton mFabSearch;
    FloatingActionButton mFabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TabLayout
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.contacts_icon));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.game_center_icon));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ibooks_author_icon));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutMenu);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mFragmentPagerAdapter = new BoardPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mFragmentPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mFam = (FloatingActionsMenu) findViewById(R.id.fabMenu);
        mFabSearch = (FloatingActionButton) findViewById(R.id.fabSearchEvents);
        mFabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.change(MainActivity.this, SearchEventActivity.class);

            }
        });

        mFabAdd = (FloatingActionButton) findViewById(R.id.fabAddEvent);
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.change(MainActivity.this, AddEventActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String msg = SessionManager.getMessaage();
        if (msg != null) {
            Snackbar.make(findViewById(R.id.relativeLayoutMenu), msg, Snackbar.LENGTH_LONG).show();
        }
    }

    private static class BoardPagerAdapter extends FragmentStatePagerAdapter {
        private static int NUM_ITEMS;
        public BoardPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.NUM_ITEMS = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new BoardFragment();
                case 1:
                    return new EventsFragment();
                case 2:
                    return new UserFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
}
