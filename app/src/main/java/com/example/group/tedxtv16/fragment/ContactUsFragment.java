package com.example.group.tedxtv16.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.group.tedxtv16.MainActivity;
import com.example.group.tedxtv16.R;


public class ContactUsFragment extends Fragment {

    private EditText etSubject, etBody;
    private Button btnSend;


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

        btnSend = (Button) v.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etSubject.getText().equals("") && !etBody.getText().equals("")){
                    send();
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
        intent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText());
        intent.putExtra(Intent.EXTRA_TEXT, etBody.getText());
        intent.setData(Uri.parse("mailto:2simonemancini5@gmail.com")); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

}