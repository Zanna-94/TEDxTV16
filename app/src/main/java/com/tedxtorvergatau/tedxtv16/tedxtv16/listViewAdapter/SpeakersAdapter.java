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

/**
 * Created by simone_mancini on 13/02/16.
 */
public class SpeakersAdapter extends BaseAdapter {

    private ArrayList list;
    private static LayoutInflater inflater = null;

    private Context context;

    //Constructor.
    public SpeakersAdapter(Context context, ArrayList list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
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
            view = inflater.inflate(R.layout.fragment_speaker_sample_layout, null);

        ImageView photo = (ImageView) view.findViewById(R.id.photo);
        TextView speaker = (TextView) view.findViewById(R.id.titolo);
        TextView description = (TextView) view.findViewById(R.id.tvNewsDescription);

        if (list != null) {
            if (!list.isEmpty()) {
                Item speakerItem = (Item) list.get(position);

                if (speakerItem != null) {
                    if (speakerItem.getName() != null)
                        speaker.setText(speakerItem.getName());
                    else {
                        speaker.setText(view.getResources().getString(R.string.notAvailable));
                    }
                    if (speakerItem.getPhoto() != null)
                        photo.setImageBitmap(Bitmap.createScaledBitmap(speakerItem.getPhoto(), 250, 250, false));
                } else {
                    photo.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_available, null),
                            250, 250, false));
                }
                if (speakerItem.getDescription() != null)
                    description.setText(speakerItem.getDescription());
                else {
                    description.setText(view.getResources().getString(R.string.notAvailable));
                }
            }
        }

        return view;
    }

}

