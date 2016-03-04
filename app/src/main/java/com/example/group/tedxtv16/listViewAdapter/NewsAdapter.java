package com.example.group.tedxtv16.listViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.group.tedxtv16.R;
import com.example.group.tedxtv16.item.Item;

import java.util.ArrayList;

/**
 * Created by emanuele on 04/03/16.
 */
public class NewsAdapter extends BaseAdapter {

    private ArrayList list;
    private static LayoutInflater inflater = null;

    //Constructor.
    public NewsAdapter(Context context, ArrayList list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
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
        TextView newsText = (TextView) view.findViewById(R.id.speaker);

        if (list != null) {
            if (!list.isEmpty()) {
                Item newsItem = (Item) list.get(position);

                if (newsItem != null) {
                    if (newsItem.getName() != null)
                        newsText.setText(newsItem.getName());
                    else {
                        newsText.setText("NON DISPONIBILE");
                    }
                    if (newsItem.getPhoto() != null)
                        photo.setImageBitmap(newsItem.getPhoto());
                }
            }
        }

        return view;
    }

}
