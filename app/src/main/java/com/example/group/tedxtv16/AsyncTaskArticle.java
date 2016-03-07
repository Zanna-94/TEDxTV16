package com.example.group.tedxtv16;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * AsyncTask
 */
public class AsyncTaskArticle extends AsyncTask<String, Void , String> {

    final String TAG = "JSa";

    private TextView text;
    private Context context;


    @Override
    protected String doInBackground(String... params) {
        try {
            Log.v(TAG, "Connecting to [" + params[0] + "]");
            Document doc = Jsoup.connect(params[0]).get();

            Elements body = doc.body().select(".post__content");
            String html = body.html();

//            Elements content = body.select(".post__content");

//            String html="";
//            for(Element p : content)
//                 html += p.toString();

            return html;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String body) {
        super.onPostExecute(body);

        URLImageParser imgParser = new URLImageParser(text, context );
        Spanned htmlSpan = Html.fromHtml(body, imgParser, null);
        text.setText(htmlSpan);

    }

    public void setText(TextView text) {
        this.text = text;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
