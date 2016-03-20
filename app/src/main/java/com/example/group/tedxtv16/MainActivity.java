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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private ViewPagerAdapter adapter;

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
    private AsyncTaskListView mytask;

    /**
     * Current Instance of MainActivity that is passed to AsyncTask to inform  when it finishes.
     * {@link AsyncTaskListView#onPostExecute(Void)}
     */
    public static Activity activity;


    private int[] tabIcons = {
            R.drawable.ic_info_black_24dp,
            R.drawable.ic_announcement_black_24dp,
            R.drawable.ic_speaker_notes_black_24dp,
            R.drawable.ic_people_black_24dp,
            R.drawable.ic_contact_mail_black_24dp
    };

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
            createFragment();
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
        outState.putParcelableArrayList("ABOUT", about);
        outState.putParcelableArrayList("NEWS", news);
        outState.putParcelableArrayList("SPEAKER", speakers);
        outState.putParcelableArrayList("TEAM", team);

        super.onSaveInstanceState(outState);
    }


    public void fillListItems() {

        waitingDialog();

        news = new ArrayList<>();
        speakers = new ArrayList<>();
        team = new ArrayList<>();
        about = new ArrayList<>();

        if (!isNetworkAvailable()) {

            if (loadItemsThread != null)
                if (loadItemsThread.getStatus() == AsyncTask.Status.PENDING ||
                        loadItemsThread.getStatus() == AsyncTask.Status.RUNNING) {
                    Log.v("debug", "load db thread already running");
                    return;
                }

            Toast.makeText(MainActivity.this, "Le informazioni potrebbero non essere aggiornate. Controlla la tua connessione!",
                    Toast.LENGTH_SHORT).show();

            Log.v("debug", "load db thread started");
            loadItemsThread = new LoadFromDatabaseAsyncTask(this);
            loadItemsThread.setNewsItemList(news);
            loadItemsThread.setAboutItemList(about);
            loadItemsThread.setSpeakerItemList(speakers);
            loadItemsThread.setTeamItemList(team);

            loadItemsThread.execute();

        } else {

            // start asyncTask to download datas from the web site
            mytask = new AsyncTaskListView(this);
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

        setupTabIcons(tabLayout);

        if (waitingDialog != null)
            waitingDialog.dismiss();
    }

    /**
     * Create Fragment and set Adapter for viewPager
     *
     * @param viewPager: to be set
     */
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutFragment(), "ABOUT");
        adapter.addFragment(new NewsFragment(), "NEWS");
        adapter.addFragment(new SpeakersFragment(), "SPEAKERS");
        adapter.addFragment(new TeamFragment(), "TEAM");
        adapter.addFragment(new ContactUsFragment(), "CONTACT US");

        viewPager.setAdapter(adapter);
    }

    public void saveItems() {

        if (saveItemsThread != null)
            if (saveItemsThread.getStatus() != AsyncTask.Status.FINISHED) {
                Log.v("debug", "save db thread already running");
                return;
            }

        Log.v("debug", "save db thread started");
        saveItemsThread = new InsertListIntoDBAsyncTask(activity);
        saveItemsThread.execute(speakers, team, news, about);
    }

    /**
     * Check if connection to internet is available
     *
     * @return true if the device if connected to internet
     */
    public boolean isNetworkAvailable() {
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

                        waitingDialog.dismiss();
                        finish();
//
//                        if (mytask != null)
//                            if (mytask.getStatus() != AsyncTask.Status.FINISHED)
//                                mytask.cancel(true);
//
//                        if (loadItemsThread != null)
//                            if (loadItemsThread.getStatus() != AsyncTask.Status.FINISHED)
//                                return;
//
//                        if(waitingDialog==null)
//                            waitingDialog();
//
//                        loadItemsThread = new LoadFromDatabaseAsyncTask((MainActivity) activity);
//                        loadItemsThread.setNewsItemList(news);
//                        loadItemsThread.setAboutItemList(about);
//                        loadItemsThread.setSpeakerItemList(speakers);
//                        loadItemsThread.setTeamItemList(team);
//
//                        Log.v("update", "datas from db");
//                        loadItemsThread.execute();
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

        @Override
        public int getItemPosition(Object item) {
            super.getItemPosition(item);

            return POSITION_NONE;
        }
    }

    private void setupTabIcons(TabLayout tabLayout) {
        //tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        //tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        //tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        //tabLayout.getTabAt(4).setIcon(tabIcons[4]);

        TextView tabAbout = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabAbout.setText("ABOUT");
        tabAbout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_info_red_24px, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabAbout);

        TextView tabNews = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabNews.setText("NEWS");
        tabNews.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_ic_announcement_red_24px, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabNews);

        TextView tabSpeakers = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabSpeakers.setText("SPEAKERS");
        tabSpeakers.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_speaker_notes_red_24px, 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabSpeakers);

        TextView tabTeam = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTeam.setText("TEAM");
        tabTeam.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_ic_people_red_24px, 0, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabTeam);

        TextView tabContactUs = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabContactUs.setText("CONTACT US");
        tabContactUs.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_contact_mail_red_24px, 0, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabContactUs);

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

    public void refreshFragment() {
        Log.v("update", "fragment.update");
        ((AboutFragment) adapter.getItem(0)).update();
        ((NewsFragment) adapter.getItem(1)).update();
        ((SpeakersFragment) adapter.getItem(2)).update();
        ((TeamFragment) adapter.getItem(3)).update();

        try {

            if (!news.isEmpty() || !about.isEmpty() || !team.isEmpty() || !about.isEmpty())
                adapter.notifyDataSetChanged();

        } catch (IllegalStateException e) {
            Log.v("update", "Illegal State exception in refresh fragments");
            e.printStackTrace();
            return;
        }

        if (waitingDialog != null)
            waitingDialog.dismiss();

    }
}
