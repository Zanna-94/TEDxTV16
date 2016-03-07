package com.example.group.tedxtv16;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import com.example.group.tedxtv16.fragment.*;
import com.example.group.tedxtv16.item.Item;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /**
     * Progress dialog refered to be dismessed from different method
     * {@link #waitingDialog()}
     * {@link #createFragment()}
     */
    private ProgressDialog waitingDialog;

    /**
     * Static because they are passed to AsyncTask that set them with download datas.
     * {@link Item}
     */
    private static ArrayList<Item> speakers;
    private static ArrayList<Item> news;
    private static ArrayList<Item> team;

    /**
     * Current Instance of MainActivity that is passed to AsyncTask to inform  when it finishes.
     * {@link AsyncTaskListView#onPostExecute(Void)}
     */
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        news = new ArrayList<>();
        speakers = new ArrayList<>();
        team = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (savedInstanceState == null) {

            //check connection
            if (!isNetworkAvailable())
                finish();

            //show progress dialog
            waitingDialog();

            // start asyncTask to download datas from the web site
            AsyncTaskListView mytask = new AsyncTaskListView(this);
            mytask.setSpeakers(speakers);
            mytask.setNews(news);
            mytask.setTeam(team);

            mytask.execute();
        } else {
            news = savedInstanceState.getParcelableArrayList("NEWS");
            speakers = savedInstanceState.getParcelableArrayList("SPEAKER");
            team = savedInstanceState.getParcelableArrayList("TEAM");

            createFragment();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("NEWS", news);
        outState.putParcelableArrayList("TEAM", team);
        outState.putParcelableArrayList("SPEAKER", speakers);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Create Fragment and set Adapter for viewPager
     *
     * @param viewPager: to be set
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsFragment(), "NEWS");
        adapter.addFragment(new SpeakersFragment(), "SPEAKERS");
        adapter.addFragment(new TeamFragment(), "TEAM");
        adapter.addFragment(new ContactUsFragment(), "CONTACT US");

        viewPager.setAdapter(adapter);
    }

    /**
     * Call {@link #setupViewPager(ViewPager)} and set TabLayout after that {@link AsyncTaskListView}
     * has finished and {@link AsyncTaskListView#onPostExecute(Void)} is called
     */
    public void createFragment() {

        if (waitingDialog != null)
            waitingDialog.dismiss();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Check if connection to internet is available
     *
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

        waitingDialog.setMessage("Waiting to connect");
        waitingDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Exit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        waitingDialog.show();
    }

    /**
     * Adapter for the ViewPager
     */
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


    /**
     * Called from Fragment to obtain ArrayList of Items and pass them to Adapter that create the
     * custom view
     *
     * @return
     */
    public static ArrayList<Item> getTeam() {
        return team;
    }

    public static ArrayList<Item> getSpeakers() {
        return speakers;
    }

    public static ArrayList<Item> getNews() {
        return news;
    }

}
