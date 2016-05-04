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
import com.tedxtorvergatau.tedxtv16.tedxtv16.MainActivity;
import com.tedxtorvergatau.tedxtv16.tedxtv16.R;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.Item;
import com.tedxtorvergatau.tedxtv16.tedxtv16.listViewAdapter.TeamAdapter;

import java.util.ArrayList;


public class TeamFragment extends ListFragment {

    private ListView myListView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private TeamAdapter teamAdapter;

    private ArrayList<Item> team;

    public TeamFragment() {
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
        View v = inflater.inflate(R.layout.fragment_team, container, false);

        myListView = (ListView) v.findViewById(android.R.id.list);
        mySwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        TextView content = (TextView) v.findViewById(R.id.textView);
        content.setVisibility(View.INVISIBLE);

        team = MainActivity.getTeam();

        if (team != null) {
            if (team.size() == 0) {
                content.setVisibility(View.VISIBLE);
            }
        } else {
            content.setVisibility(View.VISIBLE);
        }

        teamAdapter = new TeamAdapter(getActivity(), team);
        myListView.setAdapter(teamAdapter);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        mySwipeRefreshLayout.setRefreshing(false);

                        if (((MainActivity) getActivity()).isNetworkAvailable()) {
                            ((MainActivity) getActivity()).waitingDialog();
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
        articleIntent.putExtra("articleLink", team.get(position).getUrl());
        articleIntent.putExtra("whoIam", "Team");
        startActivity(articleIntent);

    }

    public void update() {
        Log.v("update", "ListView notify");
        if (teamAdapter != null)
            teamAdapter.notifyDataSetChanged();
    }

}