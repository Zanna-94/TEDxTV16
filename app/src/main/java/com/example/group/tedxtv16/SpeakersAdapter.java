package com.example.group.tedxtv16;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by simone_mancini on 13/02/16.
 */
public class SpeakersAdapter extends BaseAdapter {

    private ArrayList list;
    private static LayoutInflater inflater = null;

    //Constructor.
    public SpeakersAdapter(Context context, ArrayList list) {
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
            view = inflater.inflate(R.layout.fragment_speaker_sample_layout, null);

        ImageView photo = (ImageView) view.findViewById(R.id.photo);
        TextView speaker = (TextView) view.findViewById(R.id.speaker);

        if (list != null) {
            if (!list.isEmpty()) {
                Item speakerItem = (SpeakerItem) list.get(position);

                if (speakerItem != null) {
                    if (speakerItem.getName() != null)
                        speaker.setText(speakerItem.getName());
                    else {
                        speaker.setText("NON DISPONIBILE");
                    }
                    if (speakerItem.getPhoto()!=null)
                        photo.setImageBitmap(speakerItem.getPhoto());
                }

            }
        }

        return view;
    }

}

