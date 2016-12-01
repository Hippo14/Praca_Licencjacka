package pl.code_zone.praca_licencjacka;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import java.util.List;

import pl.code_zone.praca_licencjacka.fragments.BoardFragment;
import pl.code_zone.praca_licencjacka.fragments.EventsFragment;
import pl.code_zone.praca_licencjacka.fragments.UserFragment;
import pl.code_zone.praca_licencjacka.utils.ActivityUtils;
import pl.code_zone.praca_licencjacka.utils.GpsManager;
import pl.code_zone.praca_licencjacka.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
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
                // Check permissions
                if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                        (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION },
                            TAG_CODE_PERMISSION_LOCATION);
                }
                else {
                    googleMaps();
                }
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

    private void googleMaps() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            GpsManager.init(this, locationManager);
            GpsManager.findLocation();

            int gpsOff = gpsOff();
            // If gps is turn off
            if (gpsOff == GpsManager.GPS_OFF) {
                GpsManager.dialog();
            }
            else {
                ActivityUtils.change(MainActivity.this, SearchEventActivity.class);
            }
        } catch (SecurityException | Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int gpsOff() throws Settings.SettingNotFoundException {
        return Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == TAG_CODE_PERMISSION_LOCATION) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                googleMaps();
            }
            else {
                Snackbar.make(findViewById(R.id.relativeLayoutMenu), "Permission for GPS not granted! Functionality off.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int gpsOff = RESULT_CANCELED;

        try {
            gpsOff = gpsOff();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (gpsOff != GpsManager.GPS_OFF) {
            switch (requestCode) {
                case GpsManager.GPS_LOCATION:
                    ActivityUtils.change(MainActivity.this, SearchEventActivity.class);
                break;
            }
        }
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
