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

import java.security.KeyStore;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.mooo.nilewapps.androidnilewapp.HttpPostString;

/**
 * Registers a user with the server
 * @author nilewapp
 *
 */
public class Registry {
    
    private String email;
    private String name;
    private String phone;
    private String university;
    private String password;

    class UnregisterTask extends AsyncTask<String, Void, String> {
        
        private Exception e = null;
        private final Context context;
        private final String url;
        
        public UnregisterTask(Context context) {
            this.context = context;
            Resources res = context.getResources();
            url = res.getString(R.string.url_server) + res.getString(R.string.url_unregister);
        }
        
        @Override
        protected String doInBackground(String... urls) {
            try {
                KeyStore trustStore = TrustManager.getKeyStore(context);
                return HttpPostString.request(trustStore, url, email, password, new ArrayList<NameValuePair>());
            } catch (Exception e) {
                this.e = e;
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(String response) {
            if (e != null) {
                Log.e("Registry", "Uncaught exception", e);
            }
            if (response == null) {
                Log.e("Registry", "Communication with server failed");
            } else if (!response.isEmpty()) {
                Log.i("Registry", response);
            } else {
                Log.i("Registry", "Successfully unregistered user with server");
            }
        }
    }
    
    class RegisterTask extends AsyncTask<String, Void, String> {
        
        private Exception e = null;
        private final Context context;
        private final String url;
        
        public RegisterTask(Context context) {
            this.context = context;
            Resources res = context.getResources();
            url = res.getString(R.string.url_server) + res.getString(R.string.url_register);
        }
        
        public Exception getException() {
            return e;
        }
        
        @Override
        protected String doInBackground(String... urls) {
            try {
                KeyStore trustStore = TrustManager.getKeyStore(context);

                ArrayList<NameValuePair> body;
                if (phone != null) {
                    body = new ArrayList<NameValuePair>(5);
                    body.add(new BasicNameValuePair("phone", phone));
                } else {
                    body = new ArrayList<NameValuePair>(4);
                }
                body.add(new BasicNameValuePair("email", email));
                body.add(new BasicNameValuePair("name", name));
                body.add(new BasicNameValuePair("university", university));
                body.add(new BasicNameValuePair("password", password));
                
                return HttpPostString.request(trustStore, url, body);
            } catch (Exception e) {
                this.e = e;
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(String response) {
            if (e != null) {
                Log.e("Registry", "Uncaught exception", e);
            }
            if (response == null) {
                Log.e("Registry", "Communication with server failed");
            } else if (!response.isEmpty()) {
                Log.i("Registry", response);
            } else {
                Log.i("Registry", "Successfully registered user with server");
            }
        }
    }
    
    public void register(Context context, String email, String name, String phone, String university, String password) {        
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.university = university;
        this.password = password;
        new RegisterTask(context).execute();
    }
    
    public void unregister(Context context, String email, String password) {
        this.email = email;
        this.password = password;
        new UnregisterTask(context).execute();
    }
    
}
