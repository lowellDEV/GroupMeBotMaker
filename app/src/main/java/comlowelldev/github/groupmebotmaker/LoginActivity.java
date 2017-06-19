package comlowelldev.github.groupmebotmaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static android.content.Intent.ACTION_VIEW;

public class LoginActivity extends AppCompatActivity {
    /**
     * Based on code found at https://stackoverflow.com/questions/25831981/browser-intent-and-return-to-correct-activity-close-opened-tab
      * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && "groupbot".equals(intent.getData().getScheme())) {
            //System.out.println("*******************************************************ON Create");
            String code = getIntent().getData().getQueryParameter("access_token");
            System.out.println(code);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            preferences.edit().putString("token",code).apply();


            // complete oauth flow
        } else {
            Intent browserIntent = null;
            try {
                browserIntent = new Intent(ACTION_VIEW,Uri.parse(new URL("https://oauth.groupme.com/oauth/authorize?client_id="+getResources().getString(R.string.client_id)).toURI().toString() ))
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            startActivity(browserIntent);
            finish();
        }


    }



}
