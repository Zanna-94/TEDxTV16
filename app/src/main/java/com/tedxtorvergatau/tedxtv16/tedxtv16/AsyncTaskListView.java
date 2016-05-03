package com.tedxtorvergatau.tedxtv16.tedxtv16;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tedxtorvergatau.tedxtv16.tedxtv16.db.ItemDAO;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.AboutItem;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.Item;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.ItemType;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.NewsItem;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.SpeakerItem;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.TeamItem;

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
    private final String BASEURL_ITA = "http://www.tedxtorvergatau.com/index.php/it/";

    private final String NEWSURL_ENG = "http://www.tedxtorvergatau.com/index.php/en/new-releases";
    private final String BASEURL_ENG = "http://www.tedxtorvergatau.com/index.php/en/";

    private boolean fromDB = false;

    private ArrayList<Item> speakers;
    private ArrayList<Item> news;
    private ArrayList<Item> team;
    private ArrayList<Item> about;
    private String sponsorsHtml;

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
                configureListNews(NEWSURL_ITA);
                configureListSpeaker(BASEURL_ITA);
                configureListTeam(BASEURL_ITA);
                configureListAbout(BASEURL_ITA);
                configureSponsors(BASEURL_ITA);
            } else {
                configureListNews(NEWSURL_ENG);
                configureListSpeaker(BASEURL_ENG);
                configureListTeam(BASEURL_ENG);
                configureListAbout(BASEURL_ENG);
                configureSponsors(BASEURL_ENG);
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

            fromDB = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void configureSponsors(String link) throws IOException {

        try {

            if (docNews == null) {
                Log.v(TAG, "Connecting to [" + link + "]");
                docNews = Jsoup.connect(link).get();
            }

            Element content = docBase.body().select("#content").first();

            Element sponsorContent = content.select("#sponsor-content").first();

            Elements cards = sponsorContent.select("#mat__cards");

            Elements ul = cards.select("ul.collapsible.popout");

            Elements li = ul.select("li");

            Elements images = li.select("div.collapsible-body.center.container");

            sponsorsHtml =
                    "<html>" +
                            "<head>" +
                            "<style type='text/css'>body{margin:auto auto;text-align:center;} </style>" +
                            "</head>" +
                            "<body>"
                            + images.html() +
                            "</body>" +
                            "</html>";

        } catch (NullPointerException e) {
            e.printStackTrace();
            sponsorsHtml = null;
        }
    }

    public void configureListNews(String link) throws IOException {

        try {

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

        } catch (NullPointerException e) {
            e.printStackTrace();
            news = null;
        }
    }

    public void configureListAbout(String link) throws IOException {

        try {

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
        } catch (NullPointerException e) {
            e.printStackTrace();
            about = null;
        }

    }

    public void configureListTeam(String link) throws IOException {

        try {

            Log.v(TAG, "Connecting to [" + link + "]");
            if (docBase == null) {
                Log.v(TAG, "retrieve from downloaded html");
                docBase = Jsoup.connect(link).get();
            }

            Element body = docBase.body();

            Element content = body.select("#content").first();

            Element teamContent = content.select("#team-content").first();

            Element matCards = teamContent.select("#mat__cards").first();

            Elements as = matCards.select("a");

            for (Element a : as) {

                String articleLink = "http://www.tedxtorvergatau.com" + a.attr("href");

                Element matCard = a.select("#mat__card").first();
                String imgUrl = "http://www.tedxtorvergatau.com" + matCard.select("img").attr("src");
                Bitmap bitmap = getBitmapFromURL(imgUrl);

                String title = a.select(".header.circle-title").text();

                Item teamItem = new TeamItem(TeamItem.maxID + 1, title, bitmap, "", articleLink);
                TeamItem.incrementMaxID();
                team.add(teamItem);

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            team = null;
        }

    }

    public void configureListSpeaker(String link) throws IOException {

        try {

            Log.v(TAG, "Connecting to [" + link + "]");
            if (docBase == null) {
                Log.v(TAG, "retrieve from downloaded html");
                docBase = Jsoup.connect(link).get();
            }

            Element body = docBase.body();

            Element content = body.select("#content").first();

            Element speakersContent = content.select("#speakers-content").first();

            Element matCards = speakersContent.select("#mat__cards").first();

            Elements matCard = matCards.select("#mat__card");

            for (Element card : matCard) {

                Element a = card.select("a").first();

                String articleLink = "http://www.tedxtorvergatau.com" + a.attr("href");

                String imgUrl = "http://www.tedxtorvergatau.com" + a.select("img").attr("src");
                Bitmap bitmap = getBitmapFromURL(imgUrl);

                String description = card.select(".card-content").text();

                String title = card.select(".card-action").text();

                Item speaker = new SpeakerItem(SpeakerItem.maxID + 1, title, bitmap, description, articleLink);
                SpeakerItem.incrementMaxID();
                speakers.add(speaker);

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            team = null;
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
            options.inSampleSize = calculateInSampleSize(options, 300, 300);

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

        if (fromDB)
            Toast.makeText(activity, activity.getText(R.string.NoConnection),
                    Toast.LENGTH_SHORT).show();


        MainActivity.setSponsor(sponsorsHtml);

        Log.v("update", "MainActivity refreshFragment");
        activity.refreshFragment();

        //if all ArrayListItems are empty is useless save in db
        if (news != null && speakers != null && team != null && about != null)
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

    public void setSponsor(String sponsorsHtml) {
        this.sponsorsHtml = sponsorsHtml;
    }

}
