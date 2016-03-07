package com.example.group.tedxtv16;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * AsyncTask
 */
public class AsyncTaskArticle extends AsyncTask<String, Void , String> {

    final String TAG = "JSa";

    private TextView text;


    @Override
    protected String doInBackground(String... params) {
        try {
            Log.v(TAG, "Connecting to [" + params[0] + "]");
            Document doc = Jsoup.connect(params[0]).get();

            Element body = doc.body();
            String html = body.toString();

            return html;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String body) {
        super.onPostExecute(body);

        text.setText(Html.fromHtml(body));
    }

    public void setText(TextView text) {
        this.text = text;
    }
}
