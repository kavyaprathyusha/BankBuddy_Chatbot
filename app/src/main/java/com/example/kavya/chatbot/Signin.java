package com.example.kavya.chatbot;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static android.os.StrictMode.setThreadPolicy;

public class Signin extends AppCompatActivity {
    private static final String url = "jdbc:mysql://1:3306/Bankbuddy";
    private static final String user = "root";
    private static final String pass = "";
    String ul;
    String pl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TextView forgot=(TextView)findViewById(R.id.forgot);
        forgot.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        final EditText account=(EditText)findViewById(R.id.ac_num);
       final EditText pass=(EditText)findViewById(R.id.editText5);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.loadingProgress);
        String fontPath = "fonts/kaushan.otf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);


        account.setTypeface(tf);
        pass.setTypeface(tf);

            Button login = (Button) findViewById(R.id.signin);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ul = account.getText().toString();
                        pl=pass.getText().toString();
                        loginc();
                        pb.setVisibility(View.VISIBLE);
                    }
                });
            }

            public void loginc() {
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    setThreadPolicy(policy);
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(url, user, pass);
                    //String success = "Database connection successful\n";
                    Statement st = con.createStatement();
                    final ResultSet rs = st.executeQuery("select * from users where account_number='" + ul + "' & password='"+pl+"'");
                    if (!rs.next()) {
                        Toast.makeText(getApplicationContext(), "invalid username or password", Toast.LENGTH_LONG).show();
                    } else {
                        Intent i = new Intent(getApplicationContext(), Chatscreen.class);
                      i.putExtra("acno",ul);
                        startActivity(i);
                    }


                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }



