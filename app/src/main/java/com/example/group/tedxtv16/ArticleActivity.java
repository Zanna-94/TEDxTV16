package com.example.group.tedxtv16;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

/**
 * Class create the Activity for visualize the articles inserted in the web site.
 * The text content is puts in WebView after that it's formatted with html code in
 * {@link com.example.group.tedxtv16.ArticleActivity.AsyncTaskArticle#onPostExecute(String)}
 */
public class ArticleActivity extends AppCompatActivity {

    private String url;
    private WebView webview;

    private ProgressDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        url = getIntent().getStringExtra("articleLink");

        webview = (WebView) findViewById(R.id.textView);

        waitingDialog();
        AsyncTaskArticle myAsyncTask = new AsyncTaskArticle();
        myAsyncTask.execute();


    }

    /**
     * Create a dialog waiting connection to the web site. If it is deleted the application finishes.
     */
    private void waitingDialog() {
        waitingDialog = new ProgressDialog(this);

        waitingDialog.setMessage("Waiting to connect");
        waitingDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Exit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        waitingDialog.show();
    }


    protected class AsyncTaskArticle extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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

            //Setting the WebView to show in the right esy the web site content.
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setBuiltInZoomControls(true);
            webview.getSettings().setBuiltInZoomControls(true);

            //Test autoresizing is available only from api 19
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            }

            //adding html code to the webpage's content to center the text and the images,
            // set margin and the padding of the page.
            webview.loadDataWithBaseURL("http://www.tedxtorvergatau.com", "<html><head><style type='text/css'>html,body {margin: 0;padding: 0;width: 100%;height: 100%;}html {display: table;}body {display: table-cell;vertical-align: middle;text-align: center;}</style></head><body><p>"+html+"</p></body></html>", "text/html", "utf-8",null);

            waitingDialog.dismiss();
        }

    }
}
