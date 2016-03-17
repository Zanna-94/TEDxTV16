package com.example.group.tedxtv16.fragment;

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

import com.example.group.tedxtv16.ArticleActivity;
import com.example.group.tedxtv16.MainActivity;
import com.example.group.tedxtv16.R;
import com.example.group.tedxtv16.item.Item;
import com.example.group.tedxtv16.listViewAdapter.AboutAdapter;

import java.util.ArrayList;


public class AboutFragment extends ListFragment {

    private ListView myListView;
    private ArrayList<Item> aboutItems;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    private AboutAdapter aboutAdapter;

    public AboutFragment() {
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
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        mySwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        myListView = (ListView) v.findViewById(android.R.id.list);
        TextView content = (TextView) v.findViewById(R.id.textView);

        aboutItems = MainActivity.getAbout();

        content.setVisibility(View.INVISIBLE);

        if (aboutItems != null) {
            if (aboutItems.size() == 0)
                content.setVisibility(View.VISIBLE);
        } else {
            content.setVisibility(View.VISIBLE);
        }

        aboutAdapter = new AboutAdapter(getActivity(), aboutItems);
        myListView.setAdapter(aboutAdapter);

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

        Log.v("TAG", "entered onItemClick of ListView in position: " + position);
        Intent articleIntent = new Intent(getActivity(), ArticleActivity.class);
        articleIntent.putExtra("articleLink", aboutItems.get(position).getUrl());
        startActivity(articleIntent);
    }

    public void update() {
        Log.v("update", "ListView notify");
        if (aboutAdapter != null)
            aboutAdapter.notifyDataSetChanged();
    }
}