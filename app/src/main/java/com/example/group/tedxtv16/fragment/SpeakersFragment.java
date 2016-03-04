package com.example.group.tedxtv16.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_speakers, container, false);

        speakers = MainActivity.getSpeakers();
        myListView = (ListView) v.findViewById(android.R.id.list);

        SpeakersAdapter speakAdapter = new SpeakersAdapter(getActivity(), speakers);
        myListView.setAdapter(speakAdapter);

        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
