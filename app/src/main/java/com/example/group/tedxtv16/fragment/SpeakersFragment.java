package com.example.group.tedxtv16.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.group.tedxtv16.R;
import com.example.group.tedxtv16.SpeakerItem;
import com.example.group.tedxtv16.SpeakersAdapter;

import java.util.ArrayList;

public class SpeakersFragment extends ListFragment{

    private ListView myListView;
    private ArrayList<SpeakerItem> adapter = new ArrayList<SpeakerItem>();

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
        return inflater.inflate(R.layout.fragment_speakers, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < 10; i++){
            adapter.add(new SpeakerItem(String.valueOf(i), String.valueOf(i+1)));
        }

        SpeakersAdapter speakersAdapter = new SpeakersAdapter(getActivity(), R.layout.speaker_layout, adapter);

        myListView = (ListView) getActivity().findViewById(android.R.id.list);
        myListView.setAdapter(speakersAdapter);
    }


}
