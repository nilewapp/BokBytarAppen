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
 * Class only used to test the the token authentication functionality of the server
 * @author nilewapp
 *
 */
public class ServerTokenAuthenticationTest {
    
    private String email;
    private String series;
    private String token;
    
    class UnregisterTask extends AsyncTask<String, Void, String> {
        
        private Exception e = null;
        private final Context context;
        private final String url;
        
        public UnregisterTask(Context context) {
            this.context = context;
            Resources res = context.getResources();
            url = res.getString(R.string.url_server) + res.getString(R.string.url_unregister);
            Log.i(this.toString(), "url: " + url);
        }
        
        @Override
        protected String doInBackground(String... urls) {
            try {
                KeyStore trustStore = TrustManager.getKeyStore(context);

                ArrayList<NameValuePair> body = new ArrayList<NameValuePair>(3);
                body.add(new BasicNameValuePair("email", email));
                body.add(new BasicNameValuePair("series", series));
                body.add(new BasicNameValuePair("token", token));
                
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
    
    public void unregisterUser(Context context, String email, String series, String token) {
        this.email = email;
        this.series = series;
        this.token = token;
        new UnregisterTask(context).execute();
    }
}
