package com.tedxtorvergatau.tedxtv16.tedxtv16;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Class create the Activity for visualize the articles inserted in the web site.
 * The text content is puts in WebView after that it's formatted with html code in
 * {@link com.tedxtorvergatau.tedxtv16.tedxtv16.ArticleActivity.AsyncTaskArticle#onPostExecute(String)}
 */
public class ArticleActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String url;

    private WebView webview;

    private ProgressDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        /* set toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        Drawable logo = ContextCompat.getDrawable(this, R.drawable.logo_dark);
        toolbar.setLogo(logo);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View child = toolbar.getChildAt(i);
            if (child != null)
                if (child.getClass() == ImageView.class) {
                    ImageView iv2 = (ImageView) child;
                    if (iv2.getDrawable() == logo) {
                        iv2.setAdjustViewBounds(true);
                    }
                }
        }
        setSupportActionBar(toolbar);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_arrow_back_red_24px, null));
        } else {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_arrow_back_red_24px));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /* obtain link of the article from intent */
        url = getIntent().getStringExtra("articleLink");

        webview = (WebView) findViewById(R.id.webView);

        //Setting the WebView to show in the right way the web site content.
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setBuiltInZoomControls(true);

        Resources res = getResources();
        webview.getSettings().setDefaultFontSize((int) res.getDimension(R.dimen.txtSize));

        //Text autoresizing is available only from api 19
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        }

        waitingDialog();

        if (!isNetworkAvailable()) {

            SharedPreferences sharedPreferences = getSharedPreferences("preferenze",
                    Activity.MODE_PRIVATE);

            String article = sharedPreferences.getString(url, null);

            if (article != null && !article.equals("")) {
                webview.loadDataWithBaseURL("http://www.tedxtorvergatau.com", article,
                        "text/html", "utf-8", null);
            } else {
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(getResources().getText(R.string.noContent));
                textView.setVisibility(View.VISIBLE);
            }

            waitingDialog.dismiss();


        } else {
            AsyncTaskArticle myAsyncTask = new AsyncTaskArticle();
            String whoIam = getIntent().getStringExtra("whoIam");
            myAsyncTask.execute(whoIam);
        }

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    protected class AsyncTaskArticle extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            switch (params[0]) {
                case "Speaker":
                    return upSpeaker();
                default:
                    return upload();
            }
        }

        private String upload() {
            try {

                Document doc = Jsoup.connect(url).get();

                Element head = doc.head();

                Element content = doc.body().select("#content").first();

                Element container = content.select(".container").first();

                String html = format(container, head);

                System.out.println(html);

                return html;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private String upSpeaker() {
            try {
                Document doc = Jsoup.connect(url).get();

                Element head = doc.head();

                Element section = doc.body().select("div.section").first();

                Element row = section.select("div.row").first();

                String html = format(row, head);

                System.out.println(html);

                return html;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected String format(Element code, Element head) {

//            // resize image
//            Elements img = code.getElementsByTag("img");
//            if (img.size() != 0) {
//                for (Element e_Img : img) {
//                    //if(e_Img.)
//                    e_Img.attr("style", "max-width:100%");
//                }
//            }

            String html = code.html();

//            String formattedHtlm =
//                    "<html>"+head+"<style type='text/css'>html,body {width: 100%;height: 100%;}html {display: table;}body {display: table-cell;vertical-align: middle;}</style>" +
//                            "</head><body><p>" + html + "</p></body></html>";

            //adding html code to the webpage's content to center the text and the images,
            // set margin and the padding of the page.
            String formattedHtlm =
                    "<html><head>" + head + "</head><body" + html + "</body></html>";

            // delete unusefull breaking line
            formattedHtlm = formattedHtlm.replace("<p>&nbsp;</p>", "");

            return formattedHtlm;
        }

        @Override
        protected void onPostExecute(String html) {
            super.onPostExecute(html);

            if (html != null) {

                webview.loadDataWithBaseURL("http://www.tedxtorvergatau.com", html,
                        "text/html", "utf-8", null);

                SharedPreferences sharedPreferences = getSharedPreferences("preferenze",
                        Activity.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(url, html);
                editor.apply();

            } else {

                SharedPreferences sharedPreferences = getSharedPreferences("preferenze",
                        Activity.MODE_PRIVATE);

                String article = sharedPreferences.getString(url, null);

                if (article != null && !article.equals("")) {
                    webview.loadDataWithBaseURL("http://www.tedxtorvergatau.com", article,
                            "text/html", "utf-8", null);

                } else {
                    TextView textView = (TextView) findViewById(R.id.textView);
                    textView.setText(getResources().getText(R.string.noContent));
                    textView.setVisibility(View.VISIBLE);
                }

            }

            if (waitingDialog != null)
                waitingDialog.dismiss();

        }

    }


}

