//package com.example.var;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class Instructions3 extends AppCompatActivity{
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_instructions3);
//        Button pickColor=(Button)findViewById(R.id.button5);
//        pickColor.setOnClickListener(new View.OnClickListener()
//        {   public void onClick(View v)
//        {
//            Intent intent = new Intent(Instructions3.this, BallActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        });
//    }
//
//    /** Called when the user taps the Send button */
//    public void pickColIntent5(View view) {
//        Intent intent = new Intent(this, BallActivity.class);
//        startActivity(intent);
//    }
//}