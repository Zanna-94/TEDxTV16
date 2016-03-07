package com.example.group.tedxtv16;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        String url = getIntent().getStringExtra("articleLink");

        final WebView webview = (WebView) findViewById(R.id.textView);
//        text.setMovementMethod(new ScrollingMovementMethod());

//        AsyncTaskArticle myArticle = new AsyncTaskArticle();
//        myArticle.setText(text);
//        myArticle.setContext(this);
//        myArticle.execute(url);

        WebViewClient yourWebClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String javascript = "javascript: var form = document.getElementsByClassName('site__content');"
                        + "var body = document.getElementsByTagName('body');"
                        + "body[0].innerHTML = form[0].innerHTML;";

                view.getSettings().setDefaultFontSize(30);
                view.loadUrl(javascript);
            }
        };
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebViewClient(yourWebClient);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.loadUrl(url);
    }
}
