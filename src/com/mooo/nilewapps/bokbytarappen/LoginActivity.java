package com.mooo.nilewapps.bokbytarappen;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.mooo.nilewapps.androidnilewapp.PreferenceUtil;

/**
 * Handles login to Facebook and registration with the server
 * @author nilewapp
 *
 */
public class LoginActivity extends SherlockActivity {
    
    private void storeProfileId(String id) {
        try {
            Registry reg = new Registry(id);
            reg.register(this);
            PreferenceUtil.storePreference(this, R.string.profile_id_key, id);
        } catch (Exception e) {
            Toast t = Toast.makeText(this, "Failed to store profile id.", Toast.LENGTH_SHORT);
            t.show();
            e.printStackTrace();
        }
    }
    
    private Request.GraphUserCallback storeProfileIdCallback = new Request.GraphUserCallback() {
        @Override
        public void onCompleted(GraphUser user, Response response) {
            if (user != null) {
                storeProfileId(user.getId());
            }
        }
    };
    
    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (session.isOpened()) {
                
                /* Register user id */
                Request request = Request.newMeRequest(session, storeProfileIdCallback);
                request.executeAsync();

                /* Switch to main activity */
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    };
    
    private FacebookActivity fbActivity = new FacebookActivity(this, statusCallback);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);
        
        final Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbActivity.login();
            }
        });
        
        fbActivity.restoreSession(savedInstanceState);
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbActivity.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fbActivity.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        fbActivity.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        fbActivity.onStop();
    }

}
