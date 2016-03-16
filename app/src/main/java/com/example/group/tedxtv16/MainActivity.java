package com.example.group.tedxtv16;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.example.group.tedxtv16.db.InsertListIntoDBAsyncTask;
import com.example.group.tedxtv16.db.LoadFromDatabaseAsyncTask;
import com.example.group.tedxtv16.fragment.*;
import com.example.group.tedxtv16.item.Item;

public class MainActivity extends AppCompatActivity {

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
    private static ArrayList<Item> about;

    private LoadFromDatabaseAsyncTask loadItemsThread;
    private InsertListIntoDBAsyncTask saveItemsThread;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Drawable logo = ContextCompat.getDrawable(this, R.drawable.logo_dark);
        toolbar.setLogo(logo);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View child = toolbar.getChildAt(i);
            if (child != null)
                if (child.getClass() == ImageView.class) {
                    ImageView iv2 = (ImageView) child;
                    if (iv2.getDrawable() == logo) {
                        iv2.setAdjustViewBounds(true);
                    }
                }
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //else branch never get because the application is in portrait mode
        //check connection
        if (savedInstanceState == null) {

            //show progress dialog
            waitingDialog();


            fillListItems();
        } else {
            news = savedInstanceState.getParcelableArrayList("NEWS");
            speakers = savedInstanceState.getParcelableArrayList("SPEAKER");
            team = savedInstanceState.getParcelableArrayList("TEAM");
            about = savedInstanceState.getParcelableArrayList("ABOUT");

            createFragment();
        }

    }

    /**
     * Never user because application is always in portrait mode
     *
     * @param outState: Contains information to keeo while screen rotation
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("NEWS", news);
        outState.putParcelableArrayList("TEAM", team);
        outState.putParcelableArrayList("SPEAKER", speakers);
        outState.putParcelableArrayList("ABOUT", about);

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
        adapter.addFragment(new AboutFragment(), "ABOUT");

        viewPager.setAdapter(adapter);
    }

    public void fillListItems() {

        news = new ArrayList<>();
        speakers = new ArrayList<>();
        team = new ArrayList<>();
        about = new ArrayList<>();

        if (!isNetworkAvailable()) {

            if(loadItemsThread!=null)
                if(loadItemsThread.getStatus() == AsyncTask.Status.PENDING &&
                        loadItemsThread.getStatus() == AsyncTask.Status.RUNNING ){
                    Log.v("debug","load db thread already running");
                    return;
                }

            Log.v("debug","load db thread started");
            loadItemsThread = new LoadFromDatabaseAsyncTask(this);
            loadItemsThread.setNewsItemList(news);
            loadItemsThread.setAboutItemList(about);
            loadItemsThread.setSpeakerItemList(speakers);
            loadItemsThread.setTeamItemList(team);

            loadItemsThread.execute();

        } else {

            // start asyncTask to download datas from the web site
            AsyncTaskListView mytask = new AsyncTaskListView(this);
            mytask.setSpeakers(speakers);
            mytask.setNews(news);
            mytask.setTeam(team);
            mytask.setAbout(about);

            Log.v("debug", "parsing html");
            mytask.execute();
        }
    }

    /**
     * Call {@link #setupViewPager(ViewPager)} and set TabLayout after that {@link AsyncTaskListView}
     * has finished and {@link AsyncTaskListView#onPostExecute(Void)} is called
     */
    public void createFragment() {

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.parseColor("#e62b1e"), Color.parseColor("#e62b1e"));
        tabLayout.setupWithViewPager(viewPager);

        if (waitingDialog != null)
            waitingDialog.dismiss();
    }

    public void saveItems() {

        if (saveItemsThread != null)
            if (saveItemsThread.getStatus() != AsyncTask.Status.FINISHED) {
                Log.v("debug","save db thread already running");
                return;
            }

        Log.v("debug","save db thread started");
        saveItemsThread = new InsertListIntoDBAsyncTask(activity);
        saveItemsThread.execute(speakers, team, news, about);
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

    public static ArrayList<Item> getTeam() {
        return team;
    }

    public static ArrayList<Item> getSpeakers() {
        return speakers;
    }

    public static ArrayList<Item> getNews() {
        return news;
    }

    public static ArrayList<Item> getAbout() {
        return about;
    }

}
