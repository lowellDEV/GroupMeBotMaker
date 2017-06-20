package comlowelldev.github.groupmebotmaker;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Rider X2 on 6/19/2017.
 */

 public class CreateBotActivity extends AppCompatActivity implements OnClickListener{
    private List<Group>groupList= new ArrayList<>();
    private String groupID;
    private String[] terms;
    private String botName;
    private Button createBtn ;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_bot);
        token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token","token");
        createBtn = (Button) findViewById(R.id.createButton);
        createBtn.setEnabled(false);
        try {
            InputStream response =HTTP.sendGET(GroupMe.baseURL+"/groups",new JSONObject().put("token",token));
            populateList(response);
            //System.out.print(groupID);
            final EditText botTerms=(EditText) findViewById(R.id.termsEditText);
            botTerms.setOnFocusChangeListener(new OnFocusChangeListener() {
                  @Override
                  public void onFocusChange(View v, boolean hasFocus) {
                      System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                      System.out.println(botTerms.getText().toString());
                      EditText text1  = (EditText) v;
                      String temp =botTerms.getText().toString();
                      terms =temp.split(",");
                      for(String t :terms){
                          System.out.println(t);
                      }
                      System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                      runCheck();

                  }
              }

            );
            EditText botNameEdit = (EditText) findViewById(R.id.nameEditText);
            botNameEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText text2  = (EditText) v;
                    botName =text2.getText().toString();
                    runCheck();
                }
            });
            final Spinner botGroupSpinner = (Spinner) findViewById(R.id.groupSpinner);

           // botNameEdit.setOnClickListener(this);
            //botTerms.setOnClickListener(this);
            createBtn.setOnClickListener(this);
            botGroupSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("Selected Group: "+groupList.get(position).getGroupName());
                    groupID = groupList.get(position).getGroupID();
                    runCheck();
                }
                public void onNothingSelected(AdapterView<?> parent){
                    runCheck();
                }
            });
            //ArrayAdapter<GroupMeBot> adapter = new ArrayAdapter<GroupMeBot>(this,android.R.layout.simple_list_item_1,(List)groupList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createButton:
                GroupMeBot bot =GroupMeBot.createBot(token,botName,groupID);
                if(bot!=null){
                    bot.addTerms(terms);
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putStringSet(bot.getBotID(),new HashSet<String>(Arrays.asList(terms))).apply();
                    System.out.println(bot);
                }else{
                    System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Bot Creation Failed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                finish();
                break;

            default:
                System.err.println("rouge onclick");
        }
    }
    private void runCheck() {
        if(groupID!=""&& groupID!=null){
            if(terms!=null){
                if(botName !=null && botName != ""){
                    createBtn.setEnabled(true);
                }
            }
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
                JSONArray groups = new JSONObject(responseBld.toString()).getJSONArray("response");
                System.out.println(groups.toString());

                int count = groups.length();


                for (int i = 0; i < count; i++) {
                    JSONObject groupI = (JSONObject) groups.get(i);
                    System.out.println(groupI.toString());
                    String groupName = groupI.getString("name");
                    String groupID = groupI.getString("id");
                    Group temp = new Group(groupName,groupID);
                    //temp.setGroupName(botGroupName);
                    groupList.add(temp);

                }

            } catch (IOException ex) {
                Logger.getLogger(GroupMe.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println("*****************************************************************");
        for(Group bo:groupList){
            System.out.println(bo.getGroupName());
        }
        System.out.println("*****************************************************************");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, (List) groupList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.groupSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems.setAdapter(adapter);



    }



}
