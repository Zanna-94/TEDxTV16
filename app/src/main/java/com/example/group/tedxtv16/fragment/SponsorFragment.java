package com.example.group.tedxtv16.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.group.tedxtv16.MainActivity;
import com.example.group.tedxtv16.R;


/**
 * Created by simone_mancini on 02/04/16.
 */
public class SponsorFragment extends Fragment {

    private SwipeRefreshLayout mySwipeRefreshLayout;

    private WebView webview;

    private String html;

    public SponsorFragment() {
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
        View v = inflater.inflate(R.layout.fragment_sponsor, container, false);
        mySwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        TextView content = (TextView) v.findViewById(R.id.textView);
        webview = (WebView) v.findViewById(R.id.webView);

        html = MainActivity.getSponsors();

        if (html != null) {
            content.setVisibility(View.VISIBLE);
            webview.loadDataWithBaseURL("http://www.tedxtorvergatau.com", html,
                    "text/html", "utf-8", null);
        } else {
            content.setVisibility(View.VISIBLE);
        }

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

    public void update() {
        if (webview != null)
            webview.loadDataWithBaseURL("http://www.tedxtorvergatau.com", html,
                    "text/html", "utf-8", null);
    }

}
