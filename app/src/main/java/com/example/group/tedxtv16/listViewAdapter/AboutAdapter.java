package com.example.group.tedxtv16.listViewAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.group.tedxtv16.R;
import com.example.group.tedxtv16.item.Item;

import java.util.ArrayList;

public class AboutAdapter extends BaseAdapter {

    private ArrayList list;
    private static LayoutInflater inflater = null;

    //Constructor.
    public AboutAdapter(Context context, ArrayList list) {
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
            view = inflater.inflate(R.layout.fragment_about_sample_layout, null);

        ImageView photo = (ImageView) view.findViewById(R.id.photo);
        TextView aboutText = (TextView) view.findViewById(R.id.titolo);
        TextView description = (TextView) view.findViewById(R.id.tvNewsDescription);

        if (list != null) {
            if (!list.isEmpty()) {
                Item aboutItem = (Item) list.get(position);

                if (aboutItem != null) {
                    if (aboutItem.getName() != null)
                        aboutText.setText(aboutItem.getName());
                    else {
                        aboutText.setText(view.getResources().getString(R.string.notAvailable));
                    }
                    if (aboutItem.getPhoto() != null)
                        photo.setImageBitmap(Bitmap.createScaledBitmap(aboutItem.getPhoto(), 400, 450, false));
                    else {
                        photo.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeFile("/res/drawable/no_image_available.png"), 400, 450, false));
                    }
                    if (aboutItem.getDescription() != null)
                        description.setText(aboutItem.getDescription());
                    else {
                        description.setText(view.getResources().getString(R.string.notAvailable));
                    }
                }
            }
        }

        return view;
    }

}