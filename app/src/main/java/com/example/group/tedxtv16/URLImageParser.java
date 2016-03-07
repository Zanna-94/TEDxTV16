package com.example.group.tedxtv16;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLImageParser implements Html.ImageGetter {
    Context c;
    View container;

    /***
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     *
     * @param t
     * @param c
     */
    public URLImageParser(View t, Context c) {
        this.c = c;
        this.container = t;
    }

    public Drawable getDrawable(String source) {
        URLDrawable urlDrawable = new URLDrawable();

        // get the actual source
        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);

        asyncTask.execute(source);

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = "http://www.tedxtorvergatau.com" + params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {


            urlDrawable.setBounds(0, 0, result.getIntrinsicWidth()-500, result.getIntrinsicHeight()-500);

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable.drawable = result;

            // redraw the image by invalidating the container
            URLImageParser.this.container.invalidate();
        }

        /***
         * Get the Drawable from URL
         *
         * @param src
         * @return
         */
        public Drawable fetchDrawable(String src) {
            try {

                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream is = connection.getInputStream();

                Drawable drawable = Drawable.createFromStream(is, "src");
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth()-500, drawable.getIntrinsicHeight()-500);

                return drawable;
            } catch (Exception e) {
                return null;
            }
        }


    }
}