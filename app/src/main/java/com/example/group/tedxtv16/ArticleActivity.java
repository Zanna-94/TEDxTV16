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

        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "(document.select('.'+'subpage post'))[0].innerHTML);");
            }
        });

        webview.loadUrl("http://www.tedxtorvergatau.com/index.php/it/news/24-opening-night-ted2016-opening-night");

    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        public void showHTML(String html) {
            Toast.makeText(getApplicationContext(), html, Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(ctx).setTitle("HTML Code").setMessage(html)
                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();
        }
    }


}
