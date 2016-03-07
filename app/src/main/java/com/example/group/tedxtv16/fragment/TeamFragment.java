package com.example.group.tedxtv16.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.group.tedxtv16.MainActivity;
import com.example.group.tedxtv16.R;
import com.example.group.tedxtv16.item.Item;
import com.example.group.tedxtv16.listViewAdapter.NewsAdapter;
import com.example.group.tedxtv16.listViewAdapter.TeamAdapter;

import java.util.ArrayList;


public class TeamFragment extends Fragment {

    private ListView myListView;

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
        TextView content = (TextView) v.findViewById(R.id.textView);
        content.setVisibility(View.INVISIBLE);

        team = MainActivity.getTeam();

        if (team.size() == 0) {
            content.setVisibility(View.VISIBLE);
        } else {

            myListView = (ListView) v.findViewById(android.R.id.list);
            TeamAdapter teamAdapter = new TeamAdapter(getActivity(), team);
            myListView.setAdapter(teamAdapter);
        }

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}