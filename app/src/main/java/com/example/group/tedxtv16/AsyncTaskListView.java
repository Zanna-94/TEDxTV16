package com.example.group.tedxtv16;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import com.example.group.tedxtv16.db.ItemDAO;
import com.example.group.tedxtv16.item.AboutItem;
import com.example.group.tedxtv16.item.Item;
import com.example.group.tedxtv16.item.ItemType;
import com.example.group.tedxtv16.item.NewsItem;
import com.example.group.tedxtv16.item.SpeakerItem;
import com.example.group.tedxtv16.item.TeamItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * AsyncTask to populate ListView connecting and download html pages from tedxTV site.
 * On finish it call MainActivit method to create Fragment views
 *
 * @see MainActivity#createFragment()
 */
public class AsyncTaskListView extends AsyncTask<Object, Void, Void> {

    final String TAG = "JSwa";

    private final String NEWSURL_ITA = "http://www.tedxtorvergatau.com/index.php/it/new-releases";
    private final String BASEURL_ITA = "http://www.tedxtorvergatau.com/index.php/it";

    private final String NEWSURL_ENG = "http://www.tedxtorvergatau.com/index.php/en/new-releases";
    private final String BASEURL_ENG = "http://www.tedxtorvergatau.com/index.php/en";

    private final String[] NEWS_IDS = {};
    private final String[] SPEAKER_IDS = {};
    private final String[] TEAM_IDS = {};
    private final String[] ABOUT_IDS = {};

    private ArrayList<Item> speakers;
    private ArrayList<Item> news;
    private ArrayList<Item> team;
    private ArrayList<Item> about;

    private Document docNews;
    private Document docBase;

    private MainActivity activity;

    String language;

    public AsyncTaskListView(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Object... obj) {

        language = Locale.getDefault().getLanguage();

        try {

            if (language.equals("it")) {
                configureListNews(NEWSURL_ITA, NEWS_IDS);
//                configureListView(BASEURL_ITA, SPEAKER_IDS);
                configureListTeam(BASEURL_ITA, TEAM_IDS);
                configureListAbout(BASEURL_ITA, ABOUT_IDS);
            } else {
                configureListNews(NEWSURL_ENG, NEWS_IDS);
//                configureListView(BASEURL_ENG, SPEAKER_IDS);
                configureListTeam(BASEURL_ENG, TEAM_IDS);
                configureListAbout(BASEURL_ENG, ABOUT_IDS);
            }

        } catch (SocketTimeoutException | java.net.UnknownHostException t) {

            Log.v("update", "Connection exception");

            ItemDAO dao = new ItemDAO(activity);

            news.clear();
            news = (ArrayList<Item>) dao.getAllItems(ItemType.NEWS);

            speakers.clear();
            speakers = (ArrayList<Item>) dao.getAllItems(ItemType.SPEAKER);

            team.clear();
            team = (ArrayList<Item>) dao.getAllItems(ItemType.TEAM);

            about.clear();
            about = (ArrayList<Item>) dao.getAllItems(ItemType.ABOUT);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void configureListNews(String link, String[] ids) throws IOException {

        if (docNews == null) {
            Log.v(TAG, "Connecting to [" + link + "]");
            docNews = Jsoup.connect(link).get();
        }

        Log.v(TAG, "Connected to [" + link + "]");

        Element section = docNews.body().select(".section").first();

        Elements row = section.select("#mat__cards");

        Elements cards = row.select("#mat__card");

        for (Element card : cards) {

            Element image = card.select(".card-image").first();
            String articleLink = "http://www.tedxtorvergatau.com" + image.select("a").attr("href");

            String imageUrl = "http://www.tedxtorvergatau.com" + image.select("img").first().attr("src");
            Bitmap bitmap = getBitmapFromURL(imageUrl);

            Element content = card.select(".card-content").first();
            String description = content.text();

            Element action = card.select(".card-action").first();
            String title = action.text();

            Item newsItem = new NewsItem(NewsItem.maxID + 1, title, bitmap, description, articleLink);
            NewsItem.incrementMaxID();
            news.add(newsItem);

        }
    }

    public void configureListAbout(String link, String[] ids) throws IOException {

        if (docBase == null) {
            Log.v(TAG, "Connecting to [" + link + "]");
            docBase = Jsoup.connect(link).get();
        }

        Element homeContent = docBase.body().select("#home-content").first();

        Elements row = homeContent.select("#mat__cards");

        Elements cards = row.select("#mat__card");

        for (Element card : cards) {

            Element image = card.select(".card-image").first();
            String articleLink = "http://www.tedxtorvergatau.com" + image.select("a").attr("href");

            String imageUrl = "http://www.tedxtorvergatau.com" + image.select("img").first().attr("src");
            Bitmap bitmap = getBitmapFromURL(imageUrl);

            Element content = card.select(".card-content").first();
            String description = content.text();

            Element action = card.select(".card-action").first();
            String title = action.text();

            Item aboutItem = new AboutItem(AboutItem.maxID + 1, title, bitmap, description, articleLink);
            AboutItem.incrementMaxID();
            about.add(aboutItem);
        }

    }

    public void configureListTeam(String link, String[] ids) throws IOException {

        Log.v(TAG, "Connecting to [" + link + "]");
        if (docBase == null) {
            Log.v(TAG, "retrieve from downloaded html");
            docBase = Jsoup.connect(link).get();
        }

        Element teamContent = docBase.body().select("#team-content").first();

        Elements row = teamContent.select("#mat__cards");

        Elements cards = row.select("a");

        for (Element card : cards) {

            String articleLink = "http://www.tedxtorvergatau.com" + card.attr("href");

            Element circlImage = card.select("#mat__card").first();
            String imageUrl = "http://www.tedxtorvergatau.com" + card.select("img").first().attr("src");
            Bitmap bitmap = getBitmapFromURL(imageUrl);

            Element content = card.select(".card-content.circle-content").first();

            Element titleContent = content.select(".header.circle-title").first();

            String title = titleContent.text();

            Item teamItem = new TeamItem(TeamItem.maxID + 1, title, bitmap, "", articleLink);
            SpeakerItem.incrementMaxID();
            team.add(teamItem);
        }

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            // download only metadata to know image size
            // decodeStream will return null
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);

            // disconnect because inputstream can be used only one time
            // so we can recreate the inputStream
            connection.disconnect();

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();

            // calculate the resize factor and set it in options
            options.inSampleSize = calculateInSampleSize(options, 400, 400);

            // now decodeStream won't return null but the bitmap
            options.inJustDecodeBounds = false;

            Bitmap map = BitmapFactory.decodeStream(input, null, options);

            return map;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Log.v("update", "MainActivity refreshFragment");
        activity.refreshFragment();

        //if all ArrayListItems are empty is useless save in db
        if (!news.isEmpty() || !speakers.isEmpty() || !team.isEmpty() || !about.isEmpty())
            activity.saveItems();
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

    public void setAbout(ArrayList<Item> about) {
        this.about = about;
    }
}
