package com.example.var;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.example.var.BallActivity;


/**
 * The starter screen for the VAR app.
 * Created on the initialization of the app.
 *
 */
public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button pickColor=(Button)findViewById(R.id.button1);
        pickColor.setOnClickListener(new OnClickListener()
        {   public void onClick(View v)
        {
            Intent intent = new Intent(MainActivity.this, BallActivity.class);
            startActivity(intent);
            finish();
        }
        });
        Button pickColor2=(Button)findViewById(R.id.button2);
        pickColor2.setOnClickListener(new OnClickListener()
        {   public void onClick(View v)
        {
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent);
            finish();
        }
        });
    }

    /** Called when the user taps the Goal Line Technology button */
    public void pickColIntent(View view) {
        Intent intent = new Intent(this, BallActivity.class);
        startActivity(intent);
    }
    /** Called when the user taps the Instructions button */
    public void pickColIntent2(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

}
