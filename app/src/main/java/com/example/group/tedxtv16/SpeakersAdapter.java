package com.example.group.tedxtv16;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by simone_mancini on 13/02/16.
 */
public class SpeakersAdapter extends ArrayAdapter<Speaker> {

    private ArrayList<Speaker> list;

    public SpeakersAdapter(Context context, int textViewResourceId, ArrayList<Speaker> list) {
        super(context, textViewResourceId, list);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.speaker_layout, null);
        }
        Speaker speaker = list.get(position);
        if (speaker != null) {
            TextView tvProva = (TextView) rowView.findViewById(R.id.tvProva);
            Button btnProva = (Button) rowView.findViewById(R.id.btnProva);
            if (tvProva != null){
                tvProva.setText(speaker.getName());
            }
            if (btnProva != null){
                btnProva.setText(speaker.getDesctiption());
            }
        }
        return rowView;
    }
}
