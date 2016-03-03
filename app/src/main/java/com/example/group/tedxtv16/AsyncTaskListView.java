package com.example.group.tedxtv16;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * AsyncTask to populate ListView connecting and download html pages from tedxTV site.
 * On finish it call MainActivit method to create Fragment views
 *
 * @see MainActivity#createFragment()
 */
public class AsyncTaskListView extends AsyncTask<Object, Void, Void> {

    final String TAG = "JSwa";

    private final String NEWSURL = "http://www.tedxtorvergatau.com/index.php/it/news";
    private final String TEAMURL = "http://www.tedxtorvergatau.com/index.php/it/team";
    private final String SPEAKERURL = "http://www.tedxtorvergatau.com/index.php/it/speakers";

    private final String[] NEWS_IDS = {"#ted__item", "#ted__itemTitle", "#ted__itemIntroImage"};
    private final String[] SPEAKER_IDS = {};
    private final String[] TEAM_IDS = {};

    private ArrayList<Item> speakers;
    private ArrayList<Item> news;
    private ArrayList<Item> team;

    private MainActivity activity;

    public AsyncTaskListView(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Object... obj) {

        configureListView(NEWSURL, NEWS_IDS);


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        activity.createFragment();
    }

    private void configureListView(String link, String[] ids) {
        try {
            Log.v(TAG, "Connecting to [" + link + "]");
            Document doc = Jsoup.connect(link).get();

            Log.v(TAG, "Connected to [" + link + "]");

            Log.v(TAG, "Selecting articles...");
            Elements articles = doc.select(ids[0]);
            Log.v(TAG, "Selected articles: " + articles.toString());

            for (Element article : articles) {
                Log.v(TAG, "ARTICLE");
                Log.v(TAG, "Selected article: " + article.toString());

                Element name = article.select(ids[1]).first();

                Element image = article.select(ids[2]).first();

                //return name of card
                String articleName = name.text();
                Log.v(TAG, "Article name: " + articleName);

                String articleImageLink = image.attr("src");
                Log.v(TAG, "Article image link: " + articleImageLink);

                Log.v(TAG, "Creating bitmap...");
                Bitmap articleBitmap = getBitmapFromURL("http://www.tedxtorvergatau.com" + articleImageLink);
                Log.v(TAG, "Image created!");
                switch (link) {
                    case NEWSURL:
                        Item speakerItem = new SpeakerItem(SpeakerItem.maxID + 1, articleName, articleBitmap, null, null);
                        SpeakerItem.incrementMaxID();
                        speakers.add(speakerItem);
                        break;
                    case SPEAKERURL:
                        break;
                    case TEAMURL:
                        break;
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setSpeakers(ArrayList<Item> speakers) {
        this.speakers = speakers;
    }

    public void setNews(ArrayList<Item> news) {
        this.news = news;
    }

    public void setTeam(ArrayList<Item> team) {
        this.team = team;
    }

}
