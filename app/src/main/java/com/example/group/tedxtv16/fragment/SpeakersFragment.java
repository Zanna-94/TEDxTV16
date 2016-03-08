package com.example.group.tedxtv16.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.group.tedxtv16.ArticleActivity;
import com.example.group.tedxtv16.item.Item;
import com.example.group.tedxtv16.MainActivity;
import com.example.group.tedxtv16.R;
import com.example.group.tedxtv16.listViewAdapter.SpeakersAdapter;

import java.util.ArrayList;

public class SpeakersFragment extends ListFragment {

    private ListView myListView;

    private ArrayList<Item> speakers;

    public SpeakersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_speakers, container, false);

        TextView content = (TextView) v.findViewById(R.id.textView);
        content.setVisibility(View.INVISIBLE);

        speakers = MainActivity.getSpeakers();

        if (speakers.size() == 0) {
            content.setVisibility(View.VISIBLE);
        } else {
            myListView = (ListView) v.findViewById(android.R.id.list);
            SpeakersAdapter speakAdapter = new SpeakersAdapter(getActivity(), speakers);
            myListView.setAdapter(speakAdapter);
        }


        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent articleIntent = new Intent(getActivity(), ArticleActivity.class);
        articleIntent.putExtra("articleLink", speakers.get(position).getUrl());
        startActivity(articleIntent);
    }


}
