package com.tedxtorvergatau.tedxtv16.tedxtv16;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.tedxtorvergatau.tedxtv16.tedxtv16.db.InsertListIntoDBAsyncTask;
import com.tedxtorvergatau.tedxtv16.tedxtv16.db.LoadFromDatabaseAsyncTask;
import com.tedxtorvergatau.tedxtv16.tedxtv16.fragment.*;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.Item;

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
    private static String sponsors;

    private LoadFromDatabaseAsyncTask loadItemsThread;
    private InsertListIntoDBAsyncTask saveItemsThread;

    private FrameLayout mainFrame;
    private FrameLayout loadFrame;

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

        // set visibility of frame Layout
        mainFrame = (FrameLayout) findViewById(R.id.MainFrame);
        loadFrame = (FrameLayout) findViewById(R.id.LoadFrame);
        mainFrame.setVisibility(View.INVISIBLE);
        loadFrame.setVisibility(View.VISIBLE);

        // set background for LoadFrame through imageView
        ImageView backgroud = (ImageView) findViewById(R.id.backgraound);
        backgroud.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.start_image));
        ImageView title =  (ImageView) findViewById(R.id.loadTitle);
        title.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo_dark));

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

//        waitingDialog();

        news = new ArrayList<>();
        speakers = new ArrayList<>();
        team = new ArrayList<>();
        about = new ArrayList<>();
        sponsors = new String();

        if (!isNetworkAvailable()) {

            if (loadItemsThread != null)
                if (loadItemsThread.getStatus() == AsyncTask.Status.PENDING ||
                        loadItemsThread.getStatus() == AsyncTask.Status.RUNNING) {
                    Log.v("debug", "load db thread already running");
                    return;
                }

            Toast.makeText(MainActivity.this, getText(R.string.NoConnection),
                    Toast.LENGTH_SHORT).show();

            Log.v("debug", "load db thread started");
            loadItemsThread = new LoadFromDatabaseAsyncTask(this);
            loadItemsThread.setNewsItemList(news);
            loadItemsThread.setAboutItemList(about);
            loadItemsThread.setSpeakerItemList(speakers);
            loadItemsThread.setTeamItemList(team);
            loadItemsThread.setMainFrame(mainFrame);
            loadItemsThread.setLoadFrame(loadFrame);

            loadItemsThread.execute();

        } else {

            // start asyncTask to download datas from the web site
            AsyncTaskListView mytask = new AsyncTaskListView(this);
            mytask.setSpeakers(speakers);
            mytask.setNews(news);
            mytask.setTeam(team);
            mytask.setAbout(about);
            mytask.setSponsor(sponsors);
            mytask.setLoadFrame(loadFrame);
            mytask.setMainFrame(mainFrame);

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
        adapter.addFragment(new SponsorFragment(), "SPONSORS");

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
    public void waitingDialog() {
        waitingDialog = new ProgressDialog(this);

        waitingDialog.setMessage("Waiting to connect");
        waitingDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Exit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        waitingDialog.dismiss();
                    }
                });

        waitingDialog.setCancelable(false);
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

        ImageView tabAbout = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabAbout.setImageResource(R.drawable.ic_about_24dp);
        tabLayout.getTabAt(0).setCustomView(tabAbout);

        ImageView tabNews = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabNews.setImageResource(R.drawable.ic_news_24dp);
        tabLayout.getTabAt(1).setCustomView(tabNews);

        ImageView tabSpeaker = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabSpeaker.setImageResource(R.drawable.ic_speaker_24dp);
        tabLayout.getTabAt(2).setCustomView(tabSpeaker);

        ImageView tabTeam = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTeam.setImageResource(R.drawable.ic_team_24dp);
        tabLayout.getTabAt(3).setCustomView(tabTeam);

        ImageView tabSponsor = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabSponsor.setImageResource(R.drawable.ic_sponsor_24dp);
        tabLayout.getTabAt(4).setCustomView(tabSponsor);
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

    public static String getSponsors() {
        return sponsors;
    }

    public static void setSponsor(String html) {
        sponsors = html;
    }

    public void refreshFragment() {
        Log.v("update", "fragment.update");
        ((AboutFragment) adapter.getItem(0)).update();
        ((NewsFragment) adapter.getItem(1)).update();
        ((SpeakersFragment) adapter.getItem(2)).update();
        ((TeamFragment) adapter.getItem(3)).update();
        ((SponsorFragment) adapter.getItem(4)).update();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = new MenuInflater(getApplicationContext());
        inflate.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Mail dall'app: ");
                //intent.putExtra(Intent.EXTRA_TEXT, etBody.getText().toString());
                intent.setData(Uri.parse("mailto:info@tedxtorvergatau.com; marketing@tedxtorvergatau.com; webmaster@tedxtorvergatau.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
                break;
        }
        return false;
    }
}
