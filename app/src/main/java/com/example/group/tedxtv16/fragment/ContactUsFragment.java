package com.example.group.tedxtv16.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.group.tedxtv16.R;


public class ContactUsFragment extends Fragment {

    private EditText etBody;
    private EditText etSubject;
    private ImageButton btnSend;




    public ContactUsFragment() {
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
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);

        etSubject = (EditText) v.findViewById(R.id.etSubject);

        etBody = (EditText) v.findViewById(R.id.etBody);


        btnSend = (ImageButton) v.findViewById(R.id.ibtnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etBody.getText().toString().equals("") && !etSubject.getText().toString().equals("")) {
                    send();
                } else {
                    if (etSubject.getText().toString().equals("")) {
                        etSubject.setHint("Write a subject here!");
                        etSubject.setHintTextColor(Color.RED);
                    }
                    if (etBody.getText().toString().equals("")) {
                        etBody.setHint("Write an email here!");
                        etBody.setHintTextColor(Color.RED);
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void send(){
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Mail dall'app: " + etSubject.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, etBody.getText().toString());
        intent.setData(Uri.parse("mailto:info@tedxtorvergatau.com; marketing@tedxtorvergatau.com; webmaster@tedxtorvergatau.com")); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

}