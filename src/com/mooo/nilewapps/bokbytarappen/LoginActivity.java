/**
 *  Copyright 2013 Robert Welin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mooo.nilewapps.bokbytarappen;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.mooo.nilewapps.androidnilewapp.Password;
import com.mooo.nilewapps.androidnilewapp.Preferences;

/**
 * Handles login to Facebook and registration with the server
 * @author nilewapp
 *
 */
public class LoginActivity extends SherlockActivity {
    
    private void registerUser(String id) {
        String storedId = Preferences.get(this, R.string.profile_id_key);
        
        /* User not registered */
        if (!id.equals(storedId)) {
            /* Generate password */
            String password = Password.generate();
            
            /* Register with server */
            Registry reg = new Registry();
            reg.register(this, id, password);
            
            /* Store id and password locally */
            Preferences.put(this, R.string.profile_id_key, id);
            Preferences.put(this, R.string.password_key, password);
        }
    }
    
    private Request.GraphUserCallback storeProfileIdCallback = new Request.GraphUserCallback() {
        @Override
        public void onCompleted(GraphUser user, Response response) {
            if (user != null) {
                registerUser(user.getId());
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
