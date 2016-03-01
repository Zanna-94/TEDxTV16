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

        StringBuffer buffer = new StringBuffer();

        try {
            Log.v(TAG, "Connecting to [" + strings[0] + "]");
            Document doc = Jsoup.connect(strings[0]).get();

            Log.v("JSwa", "Connected to [" + strings[0] + "]");

            Log.v("JSwa", "Selecting articles");
            Elements articles = doc.select("article");

            for (Element article : articles) {

                Element name = article.select("li.item__info_item item__info_item--title").first();

                //return name of card
                String text = name.text();
                System.out.println(text);
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }
}
