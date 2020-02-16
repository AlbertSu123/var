package com.example.var;

<<<<<<< HEAD
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Stats extends Activity {
    private DatabaseReference mDatabase;
=======
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.example.var.BallActivity;

public class Stats extends Activity {
>>>>>>> db1a78de409ef2fa6f767057f8617c3bc3b188eb

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);

<<<<<<< HEAD
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String goals1 = "1";//mDatabase.child("goalscored");
        String goals2 = "";

        ((TextView)findViewById(R.id.madeGoals1)).setText("Goals Scored: " + goals1);
        ((TextView)findViewById(R.id.madeGoals2)).setText("Goals Scored: " + goals2);
        ((TextView)findViewById(R.id.goalAttempts1)).setText("Shots Attempted: "+ "2");
        ((TextView)findViewById(R.id.goalAttempts2)).setText("Shots Attempted: " + "");
    }
}
=======
    }
}


>>>>>>> db1a78de409ef2fa6f767057f8617c3bc3b188eb
