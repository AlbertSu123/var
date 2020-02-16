package com.example.var;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Stats extends Activity {
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String goals1 = "1";//mDatabase.child("goalscored");
        String goals2 = "";

        ((TextView)findViewById(R.id.madeGoals1)).setText("Goals Scored: " + goals1);
        ((TextView)findViewById(R.id.madeGoals2)).setText("Goals Scored: " + goals2);
        ((TextView)findViewById(R.id.goalAttempts1)).setText("Shots Attempted: "+ "2");
        ((TextView)findViewById(R.id.goalAttempts2)).setText("Shots Attempted: " + "");
    }
}