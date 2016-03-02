package com.example.group.tedxtv16;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * AsyncTask to populate ListView connecting and download html pages from tedxTV site.
 */
public class AsyncTaskListView extends AsyncTask<String, Void, Void> {

    final String TAG = "JSwa";
    @Override
    protected Void doInBackground(String... strings) {

        try {
            Log.v(TAG, "Connecting to [" + strings[0] + "]");
            Document doc = Jsoup.connect(strings[0]).get();

            Log.v(TAG, "Connected to [" + strings[0] + "]");

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
                Log.v(TAG, "Article image link: " + articleImage);}

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}
