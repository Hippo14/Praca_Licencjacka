package pl.code_zone.praca_licencjacka;

import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import pl.code_zone.praca_licencjacka.fragments.BoardFragment;
import pl.code_zone.praca_licencjacka.fragments.EventsFragment;
import pl.code_zone.praca_licencjacka.fragments.UserFragment;

public class MainActivity extends AppCompatActivity {

    // UI
    TabLayout tabLayout;
    RelativeLayout relativeLayout;
    ViewPager viewPager;
    PagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.contacts_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.game_center_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ibooks_author_icon));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutMenu);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        fragmentPagerAdapter = new BoardPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
