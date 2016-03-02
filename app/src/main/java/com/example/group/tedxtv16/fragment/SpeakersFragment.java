package com.example.group.tedxtv16.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.group.tedxtv16.MainActivity;
import com.example.group.tedxtv16.R;
import com.example.group.tedxtv16.SpeakerItem;
import com.example.group.tedxtv16.SpeakersAdapter;

import java.util.ArrayList;

public class SpeakersFragment extends ListFragment {

    private ListView myListView;

    private ArrayList<SpeakerItem> speakers;

    public SpeakersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        speakers = MainActivity.getSpeakers();

        myListView = (ListView) this.getActivity().findViewById(R.id.list);

        SpeakersAdapter speakAdapter = new SpeakersAdapter(getActivity(), speakers);
        myListView.setAdapter(speakAdapter);
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
    }


}
