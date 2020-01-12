package com.example.var;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class Instructions2 extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions2);
        Button pickColor=(Button)findViewById(R.id.button4);
        pickColor.setOnClickListener(new View.OnClickListener()
        {   public void onClick(View v)
        {
            Intent intent = new Intent(Instructions2.this, Instructions3.class);
            startActivity(intent);
            finish();
        }
        });
    }

    /** Called when the user taps the Send button */
    public void pickColIntent4(View view) {
        Intent intent = new Intent(this, Instructions3.class);
        startActivity(intent);
    }

}
