package com.tedxtorvergatau.tedxtv16.tedxtv16.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tedxtorvergatau.tedxtv16.tedxtv16.ArticleActivity;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.Item;
import com.tedxtorvergatau.tedxtv16.tedxtv16.MainActivity;
import com.tedxtorvergatau.tedxtv16.tedxtv16.R;
import com.tedxtorvergatau.tedxtv16.tedxtv16.listViewAdapter.SpeakersAdapter;

import java.util.ArrayList;

public class SpeakersFragment extends ListFragment {

    private ListView myListView;
    private SpeakersAdapter speakAdapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    private ArrayList<Item> speakers;

    public SpeakersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_speakers, container, false);

        mySwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        myListView = (ListView) v.findViewById(android.R.id.list);
        TextView content = (TextView) v.findViewById(R.id.textView);
        content.setVisibility(View.INVISIBLE);

        speakers = MainActivity.getSpeakers();

        if (speakers != null) {
            if (speakers.size() == 0) {
                content.setVisibility(View.VISIBLE);
            }
        } else {
            content.setVisibility(View.VISIBLE);
        }

        speakAdapter = new SpeakersAdapter(getActivity(), speakers);
        myListView.setAdapter(speakAdapter);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        mySwipeRefreshLayout.setRefreshing(false);

                        if (((MainActivity) getActivity()).isNetworkAvailable()) {
                            ((MainActivity) getActivity()).fillListItems();
                        }
                    }
                }
        );

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
        articleIntent.putExtra("whoIam", "Speaker");
        startActivity(articleIntent);
    }

    public void update() {
        Log.v("update", "ListView notify");
        if (speakAdapter != null)
            speakAdapter.notifyDataSetChanged();
    }

}
