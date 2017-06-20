package comlowelldev.github.groupmebotmaker;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InfoActivity extends AppCompatActivity  {
    private GroupMeBot bot;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token","token");
        Button editbtn = (Button) findViewById(R.id.EditButton);
        Button deletebtn = (Button) findViewById(R.id.DeleteButton);
        TextView groupTV = (TextView) findViewById(R.id.groupNameTV);
        TextView botNameTV =(TextView) findViewById(R.id.botNameTV);
        TextView searchTV =(TextView) findViewById(R.id.searchTermsTV);
        Intent intent = getIntent();
        bot =intent.getParcelableExtra("groupmebot");
        String terms="";
        Set<String> termSet =PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getStringSet(bot.getBotID(),null);
        if(termSet !=null) {
            String[] termArray = termSet.toArray(new String[termSet.size()]);
            System.out.println(terms);
            for (String term : termArray) {
                System.out.println(term);
                terms += term + "\n";
            }
        }

        botNameTV.setText(bot.getName());
        groupTV.setText(bot.getGroupName());

        searchTV.setText(terms);
        editbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //start edit activity
            }
        });
        deletebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bot.destroy(token,bot.getBotID());
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().remove(bot.getBotID()).apply();
                    setResult(Activity.RESULT_OK);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
