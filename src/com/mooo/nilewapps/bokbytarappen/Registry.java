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
    
    private String id;
    private String password;
    
    class RegisterTask extends AsyncTask<String, Void, String> {
        
        private Exception e = null;
        private final Context context;
        
        public RegisterTask(Context context) {
            this.context = context;
        }
        
        @Override
        protected String doInBackground(String... urls) {
            try {
                KeyStore trustStore = TrustStore.get(context);
                
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("password", password));
                
                return HttpPostString.request(trustStore, urls[0], params);
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
                Log.e("Registry", "Communication with server failded");
            } else if (!response.isEmpty()) {
                Log.w("Registry", response);
            } else {
                Log.i("Registry", "Successfully registered user with server");
            }
        }
    }
    
    public void register(Context context, String id, String password) {        
        this.id = id;
        this.password = password;
        Resources res = context.getResources();
        String url = res.getString(R.string.server_url) + res.getString(R.string.register_url);
        new RegisterTask(context).execute(url);
    }
    
}
