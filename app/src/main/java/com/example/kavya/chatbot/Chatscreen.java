package com.example.kavya.chatbot;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;

import static android.os.StrictMode.setThreadPolicy;


public class Chatscreen extends AppCompatActivity implements AIListener {
    private static final String TAG = "ChatActivity";
    public static CountDownTimer timer;
    String e;
    private static final String url = "jdbc:mysql://10.0.2.2:3306/Bankbuddy";
    private static final String user = "root";
    private static final String pass = "";
    private com.example.kavya.chatbot.ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    String r;
 String i;
    String value;
    private boolean side = false;
    private AIService aiService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatscreen);
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
value=extras.getString("acno");
        buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new com.example.kavya.chatbot.ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        int count=0;
        String first="Hey, Welcome to BankBuddy! How can I help you?";

        if(count==0)
        {
            chatArrayAdapter.add(new ChatMessage(!side,first));
            count=1;
        }
        //db1();
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
                //db();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private boolean sendChatMessage() {

        EditText editText = (EditText) findViewById(R.id.msg);
        //final String e;

        e = editText.getText().toString();
        final AIConfiguration config = new AIConfiguration("460ff23deae44729981033d2ad6d3b29", AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);
        final AIDataService aiDataService = new AIDataService(config);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);


        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(e);
        new AsyncTask<AIRequest, Void, AIResponse>() {
            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {
                    // process aiResponse here
                    Result result = aiResponse.getResult();
                    Gson gson = new Gson();
                    final Metadata metadata = result.getMetadata();
                    // Get parameters
                    String parameterString = "";
                    if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                        for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                            parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                        }

                    }


                    // Show results in TextView.
                    //resultTextView.setText("Query:" + result.getResolvedQuery() +
                    //  "\nAction: " + result.getAction() +
                    //"\nParameters: " + parameterString + "\nIntent name:" + metadata.getIntentName() + "\nResponse:" + result.getFulfillment().getSpeech());
                    r = result.getFulfillment().getSpeech();
                    i=metadata.getIntentName();
                    // Toast.makeText(getApplicationContext(),r,Toast.LENGTH_LONG).show();
                    update();
                    //Toast.makeText(getApplicationContext(),r,Toast.LENGTH_LONG).show();
                    chatArrayAdapter.add(new ChatMessage(side, e));
                    chatText.setText("");
                    side = !side;
                    chatArrayAdapter.add(new ChatMessage(side, r));
                    side = !side;

                    //Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
                }
            }

        }.execute(aiRequest);

            /*chatArrayAdapter.add(new ChatMessage(side, e));
            chatText.setText("");
            side = !side;
            chatArrayAdapter.add(new ChatMessage(side, r));
            side = !side;
            //Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
            return true;*/

        return true;
    }
    public void update() {
        // Toast.makeText(getApplicationContext(),"connected",Toast.LENGTH_LONG).show();
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            //String success = "Database connection successful\n";
            Statement st = con.createStatement();
            //   Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG).show();
            st.executeUpdate("INSERT INTO messages(acno,message,response,intent)values('" + value + "','" + e + "','" + r + "','" + i + "')");
            // Toast.makeText(getApplicationContext(), "Recorded successfully", Toast.LENGTH_LONG).show();
          /*  INSERT INTO users (full_name, login, password)
            SELECT 'Mahbub Tito','tito',SHA1('12345') FROM DUAL
            WHERE NOT EXISTS
                    (SELECT login FROM users WHERE login='tito');*/

            final ResultSet rs = st.executeQuery("select * from intent_count where intent='" + i + "'");
           if(!rs.next()){
                st.executeUpdate("INSERT INTO intent_count (intent) values ('"+i+"')");

                           }
              else { st.executeUpdate("UPDATE intent_count set count=count+1 where intent='"+i+"'");}

        }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }


    public void retrieve()
    {
        //Toast.makeText(getApplicationContext(),"connected",Toast.LENGTH_LONG).show();
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            //String success = "Database connection successful\n";
            Statement st = con.createStatement();
          //  Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG).show();
            final ResultSet rs = st.executeQuery("select * from messages where acno='"+value+"'");
            while(rs.next())
            {
                String m1=rs.getString(3);
                String r1=rs.getString(4);

                chatArrayAdapter.add(new ChatMessage(side, m1));
                chatText.setText("");
                side = !side;
                chatArrayAdapter.add(new ChatMessage(side, r1));
                side = !side;
            }



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Enter valid college name",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }

        }

        // Show results in TextView.
        // resultTextView.setText("Query:" + result.getResolvedQuery() +
        //       "\nAction: " + result.getAction() +
        //     "\nParameters: " + parameterString);
        r=result.getFulfillment().getSpeech();
    }



    @Override
    public void onError(AIError aiError) {

    }

    @Override
    public void onAudioLevel(float v) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
Boolean b=false;
        //noinspection SimplifiableIfStatement
        if (id == R.id.History) {
                retrieve();
                item.setEnabled(false);
            }

         else if (id == R.id.Logout) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}