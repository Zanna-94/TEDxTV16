package com.example.group.tedxtv16.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class NewsFragment extends ListFragment  {

    private ListView myListView;

    private ArrayList<Item> news;

    private SwipeRefreshLayout mySwipeRefreshLayout;

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

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        mySwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);


        TextView content = (TextView) v.findViewById(R.id.textView);
        content.setVisibility(View.INVISIBLE);

        news = MainActivity.getNews();

        if (news.size() == 0) {
            content.setVisibility(View.VISIBLE);
        } else {

            myListView = (ListView) v.findViewById(android.R.id.list);
            NewsAdapter newsAdapter = new NewsAdapter(getActivity(), news);
            myListView.setAdapter(newsAdapter);
        }

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        ((MainActivity) getActivity()).fillListItems();
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

        Log.v("TAG", "entered onItemClick of ListView in position: "+position);
        Intent articleIntent = new Intent(getActivity(), ArticleActivity.class);
        articleIntent.putExtra("articleLink", news.get(position).getUrl());
        startActivity(articleIntent);

    }

}