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
import java.security.MessageDigest;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.mooo.nilewapps.androidnilewapp.HttpPostString;

public class Registry {
    
    private static final String ID_SALT = "innovationsoulkey";
    private final String id;
    private final String hash;

    public Registry(String id) throws Exception {
        this.id = id;
        hash = hashId(id);
    }
    
    private String hashId(String id) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((id + ID_SALT).getBytes("UTF-8"));
        String hash = new String(md.digest());
        Log.d(this.toString(), "Computed profile id: '" + hash + "'");
        return hash;
    }
    
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
                params.add(new BasicNameValuePair("hash", hash));
                
                return HttpPostString.request(trustStore, urls[0], params);
            } catch (Exception e) {
                this.e = e;
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(String response) {
            if (e != null) {
                e.printStackTrace();
            }
            if (response != null && !response.isEmpty()) {
                Log.w(this.toString(), response);
            }
        }
    }
    
    public void register(Context context) throws Exception {                
        Resources res = context.getResources();
        String url = res.getString(R.string.server_url) + res.getString(R.string.register_url);
        new RegisterTask(context).execute(url);
    }
    
}
