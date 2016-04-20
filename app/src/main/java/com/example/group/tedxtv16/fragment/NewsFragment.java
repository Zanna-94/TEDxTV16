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
import com.example.group.tedxtv16.listViewAdapter.NewsAdapter;

import java.util.ArrayList;


public class NewsFragment extends ListFragment {

    private ListView myListView;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    private ArrayList<Item> news;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v("update", "entered in onCreateView");

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        myListView = (ListView) v.findViewById(android.R.id.list);
        mySwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        TextView content = (TextView) v.findViewById(R.id.textView);

        news = MainActivity.getNews();

        content.setVisibility(View.INVISIBLE);

        if (news != null) {
            if (news.size() == 0) {
                content.setVisibility(View.VISIBLE);
            }
        } else {
            content.setVisibility(View.VISIBLE);
        }

        newsAdapter = new NewsAdapter(getActivity(), news);
        myListView.setAdapter(newsAdapter);

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
        articleIntent.putExtra("articleLink", news.get(position).getUrl());
        articleIntent.putExtra("whoIam", "News");
        startActivity(articleIntent);

    }

    public void update() {
        Log.v("update", "ListView notify");
        if (newsAdapter != null)
            newsAdapter.notifyDataSetChanged();
    }

}