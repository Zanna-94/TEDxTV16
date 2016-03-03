package com.example.group.tedxtv16;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import com.example.group.tedxtv16.fragment.*;

public class MainActivity extends AppCompatActivity {

    private static final String WebSiteUrl = "http://www.tedxtorvergatau.com";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ProgressDialog waitingDialog;

    private static ArrayList<SpeakerItem> speakers = new ArrayList<>();
    private static ArrayList<NewsItem> news = new ArrayList<>();
    private static ArrayList<TeamItem> team = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(isNetworkAvailable()){
            //waitingDialog();
        }

        AsyncTaskListView mytask = new AsyncTaskListView();
        mytask.setSpeakers(speakers);
        mytask.setNews(news);
        mytask.setTeam(team);
        mytask.setContext(this);

        mytask.execute();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SpeakersFragment(), "SPEAKERS");
        adapter.addFragment(new TeamFragment(), "TEAM");
        adapter.addFragment(new ContactUsFragment(), "CONTACT US");
        adapter.addFragment(new NewsFragment(), "NEWS");

        viewPager.setAdapter(adapter);
    }

    /**
     * Check if connection to internet is available
     * @return true if the device if connected to internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Create a dialog waiting connection to the web site. If it is deleted the application finishes.
     */
    private void waitingDialog() {
        waitingDialog = new ProgressDialog(this);

        waitingDialog.setMessage("Waiting");
        waitingDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Wait connection",
                new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        waitingDialog.show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static ArrayList<TeamItem> getTeam() {
        return team;
    }

    public static ArrayList<SpeakerItem> getSpeakers() {
        return speakers;
    }

    public static ArrayList<NewsItem> getNews() {
        return news;
    }

}
