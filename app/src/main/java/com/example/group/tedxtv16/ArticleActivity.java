package com.example.group.tedxtv16;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        String url = getIntent().getStringExtra("articleLink");

        TextView text = (TextView) findViewById(R.id.textView2);
        text.setMovementMethod(new ScrollingMovementMethod());

        AsyncTaskArticle myArticle = new AsyncTaskArticle();
        myArticle.setText(text);
        myArticle.execute(url);

    }




}
