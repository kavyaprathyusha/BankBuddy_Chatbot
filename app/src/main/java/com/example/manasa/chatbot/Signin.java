package com.example.manasa.chatbot;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Signin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        TextView forgot=(TextView)findViewById(R.id.forgot);
        forgot.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        EditText account=(EditText)findViewById(R.id.ac_num);
        EditText pass=(EditText)findViewById(R.id.editText5);
        String fontPath = "fonts/kaushan.otf";


        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);


        account.setTypeface(tf);
        pass.setTypeface(tf);

    }
}
