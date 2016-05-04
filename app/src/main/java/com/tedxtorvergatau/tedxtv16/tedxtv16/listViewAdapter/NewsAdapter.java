package com.tedxtorvergatau.tedxtv16.tedxtv16.listViewAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tedxtorvergatau.tedxtv16.tedxtv16.R;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.Item;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {

    private ArrayList list;
    private static LayoutInflater inflater = null;

    private Context context;

    //Constructor.
    public NewsAdapter(Context context, ArrayList list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null)
            view = inflater.inflate(R.layout.fragment_news_sample_layout, null);

        ImageView photo = (ImageView) view.findViewById(R.id.photo);
        TextView newsText = (TextView) view.findViewById(R.id.titolo);
        TextView description = (TextView) view.findViewById(R.id.tvNewsDescription);

        if (list != null) {
            if (!list.isEmpty()) {
                Item newsItem = (Item) list.get(position);

                if (newsItem != null) {
                    if (newsItem.getName() != null)
                        newsText.setText(newsItem.getName());
                    else {
                        newsText.setText(view.getResources().getString(R.string.notAvailable));
                    }
                    if (newsItem.getPhoto() != null)
                        photo.setImageBitmap(Bitmap.createScaledBitmap(newsItem.getPhoto(), 250, 250, false));
                    else {
                        photo.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_available, null),
                                250, 250, false));
                    }
                    if (newsItem.getDescription() != null)
                        description.setText(newsItem.getDescription());
                    else {
                        description.setText(view.getResources().getString(R.string.notAvailable));
                    }
                }
            }
        }
        return view;
    }

}
