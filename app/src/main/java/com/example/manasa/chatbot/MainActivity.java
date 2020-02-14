package com.example.manasa.chatbot;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       TextView welcome = (TextView) findViewById(R.id.welcome_msg);

        TextView tagline1 = (TextView) findViewById(R.id.tagline1);
        TextView tagline2 = (TextView) findViewById(R.id.tagline2);
        String fontPath = "fonts/green avocado.ttf";
        String fontPathAlex="fonts/alex.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath);
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPathAlex);
        welcome.setTypeface(tf2);
        tagline1.setTypeface(tf1);
        tagline2.setTypeface(tf1);
        Button signin=(Button)findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Signin.class);
                startActivity(i);
            }
        });
    }
}
