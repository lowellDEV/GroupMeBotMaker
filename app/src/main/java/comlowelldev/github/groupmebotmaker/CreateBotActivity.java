package comlowelldev.github.groupmebotmaker;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import java.util.List;
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
    private Button createBtn = (Button) findViewById(R.id.createButton);
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_bot);
        String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token","token");
        createBtn.setEnabled(false);
        try {
            InputStream response =HTTP.sendGET(GroupMe.baseURL+"/groups",new JSONObject().put("token",token));
            populateList(response);
            //System.out.print(groupID);
            EditText botTerms=(EditText) findViewById(R.id.termsEditText);
            EditText botNameEdit = (EditText) findViewById(R.id.nameEditText);
            final Spinner botGroupSpinner = (Spinner) findViewById(R.id.groupSpinner);
            //Button createBtn = (Button) findViewById(R.id.createButton);
            botNameEdit.setOnClickListener(this);
            botTerms.setOnClickListener(this);
            createBtn.setOnClickListener(this);
            botGroupSpinner.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("Selected Group: "+groupList.get(position).getGroupName());
                    groupID = groupList.get(position).getGroupID();
                    runCheck();
                }
            });
            //ArrayAdapter<GroupMeBot> adapter = new ArrayAdapter<GroupMeBot>(this,android.R.layout.simple_list_item_1,(List)groupList);

        } catch (JSONException e) {
            e.printStackTrace();
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
        sItems.setAdapter(adapter);
        /*
        AlertDialog.Builder chooseGroup = new AlertDialog.Builder(CreateBotActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.layout, null);
        chooseGroup.setView(convertView);
        chooseGroup.setTitle("Select Group");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,(List)groupList);
        lv.setAdapter(adapter);
        final AlertDialog show = chooseGroup.create();
        show.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                groupID = groupList.get(position).getGroupID();
                System.out.println("GroupID: "+groupID);

                show.cancel();
            }
        });
        */

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.createButton:
               //create bot
               //return to main view
               break;
           case R.id.termsEditText:
               //fill terms
               runCheck();
               break;
           case R.id.nameEditText:
               //set name
               runCheck();
               break;
           default:
               System.err.println("rouge onclick");
        }
    }
}
