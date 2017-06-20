package comlowelldev.github.groupmebotmaker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {
    private String token;
    private GroupMe groupme;
    private ArrayList<GroupMeBot> botList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //System.out.println("test*****************************************************\n*****************************************************\n****************************************");

        if(!preferences.contains("token")) {
            startActivityForResult(new Intent(this, LoginActivity.class), Activity.RESULT_OK);
        }else{
            token = preferences.getString("token",null);
        }
        System.out.println("******************Token: "+preferences.getString("token",null));
        try {
            InputStream response =HTTP.sendGET(GroupMe.baseURL+"/bots",new JSONObject().put("token",token));
            populateList(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //groupme = new GroupMe(token,,"AndroidGroupBotMaker");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivityForResult(new Intent(getApplicationContext(), CreateBotActivity.class), Activity.RESULT_OK);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            InputStream response =HTTP.sendGET(GroupMe.baseURL+"/bots",new JSONObject().put("token",token));
            populateList(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateList(InputStream response) {
        StringBuilder responseBld = new StringBuilder();
        BufferedReader read = new BufferedReader(new InputStreamReader(response));
        if (response == null) {
            //messages = new Message[0];
            return;
        } else {
            try {
                String jsonStr;
                while ((jsonStr = read.readLine()) != null) {
                    responseBld.append(jsonStr);
                }
                JSONArray bots = new JSONObject(responseBld.toString()).getJSONArray("response");
                System.out.println(bots.toString());

                int count = bots.length();


                for (int i = 0; i < count; i++) {
                    JSONObject botI = (JSONObject) bots.get(i);
                    System.out.println(botI.toString());
                    String botName = botI.getString("name");
                    String botID = botI.getString("bot_id");
                    String botGroupID = botI.getString("group_id");
                    String botGroupName = botI.getString("group_name");
                    GroupMeBot temp =new GroupMeBot(botName, botID, botGroupID);
                    temp.setGroupName(botGroupName);
                    botList.add(temp);

                }

            } catch (IOException ex) {
                Logger.getLogger(GroupMe.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<GroupMeBot> adapter = new ArrayAdapter<GroupMeBot>(this,android.R.layout.simple_list_item_1,botList);
        System.out.println("*****************************************************************");
        for(GroupMeBot bo:botList){
            System.out.println(bo.getName());
        }
        System.out.println("*****************************************************************");
        ListView lv= (ListView) findViewById(R.id.bot_list);
        lv.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
