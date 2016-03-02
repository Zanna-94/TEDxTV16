package com.example.group.tedxtv16;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * AsyncTask to populate ListView connecting and download html pages from tedxTV site.
 */
public class AsyncTaskListView extends AsyncTask< Object, Void, Void> {

    final String TAG = "JSwa";

    private  final String NEWSURL= "http://www.tedxtorvergatau.com/index.php/it/news";
    private  final String TEAMURL= "http://www.tedxtorvergatau.com/index.php/it/team";
    private  final String SPEAKERURL= "http://www.tedxtorvergatau.com/index.php/it/speakers";

    private ArrayList<SpeakerItem> speakers;
    private ArrayList<NewsItem> news;
    private ArrayList<TeamItem> team;

    private ViewPager pager;
    private Activity context;

    @Override
    protected Void doInBackground(Object... obj) {

        try {
            Log.v(TAG, "Connecting to [" + NEWSURL + "]");
            Document doc = Jsoup.connect(NEWSURL).get();

            Log.v(TAG, "Connected to [" + NEWSURL + "]");

            Log.v(TAG, "Selecting articles...");
            Elements articles = doc.select("article");
            Log.v(TAG, "Selected articles: " + articles.toString());

            for (Element article : articles) {
                Log.v(TAG, "ARTICLE");
                Log.v(TAG, "Selected article: " + article.toString());

                Element name = article.select("li").first();

                Element image = article.select("img").first();

                //return name of card
                String articleName = name.text();
                Log.v(TAG, "Article name: " + articleName);

                String articleImage = image.attr("src");
                Log.v(TAG, "Article image link: " + articleImage);
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

    public void setSpeakers(ArrayList<SpeakerItem> speakers) {
        this.speakers = speakers;
    }

    public void setNews(ArrayList<NewsItem> news) {
        this.news = news;
    }

    public void setTeam(ArrayList<TeamItem> team) {
        this.team = team;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

}
