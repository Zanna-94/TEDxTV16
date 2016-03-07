package com.example.group.tedxtv16;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.webkit.WebSettings;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class ArticleActivity extends AppCompatActivity {

    private String url;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        url = getIntent().getStringExtra("articleLink");

        webview = (WebView) findViewById(R.id.textView);

        AsyncTaskArticle myas = new AsyncTaskArticle();
        myas.execute();


    }

    protected class AsyncTaskArticle extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect(url).get();

                Elements body = doc.body().select(".site__content");
                String html = body.html();

                return html;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String html) {
            super.onPostExecute(html);

            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setBuiltInZoomControls(true);
            webview.getSettings().setBuiltInZoomControls(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            }
            webview.loadDataWithBaseURL("http://www.tedxtorvergatau.com", "<html><head><style type='text/css'>html,body {margin: 0;padding: 0;width: 100%;height: 100%;}html {display: table;}body {display: table-cell;vertical-align: middle;text-align: center;}</style></head><body><p>"+html+"</p></body></html>", "text/html", "utf-8",null);

        }

    }
}
