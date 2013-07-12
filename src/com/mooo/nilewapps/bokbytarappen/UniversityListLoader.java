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
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.mooo.nilewapps.androidnilewapp.HttpGetString;

/**
 * Asynchronously loads a list of universities from the app server
 * @author nilewapp
 *
 */
public class UniversityListLoader extends AsyncTaskLoader<List<String>> {
    
    private List<String> unis;
    private final String universityUrl;
    private final int country;
    
    /**
     * Defines the number of http requests that will be sent before giving up
     */
    private static final int NUM_TRIES = 3;
    
    public UniversityListLoader(Context context, int country) {
        super(context);
        this.country = country;
        Resources res = context.getResources();
        universityUrl = res.getString(R.string.server_url) + res.getString(R.string.university_url);
        Log.d(this.toString(), "Init UniversityListLoader with universityUrl '" + universityUrl + "'");
    }
    
    @Override 
    public List<String> loadInBackground() {
        String json = null;
        JSONArray arr = null;
        List<String> universities = new ArrayList<String>();
        try {
            KeyStore trustStore = TrustManager.getKeyStore(getContext());
            
            /* Request the json formated list of unis */
            for (int i = 0; json == null && i < NUM_TRIES; i++) {
                json = HttpGetString.request(trustStore, universityUrl + country);
            }
            arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                universities.add(arr.optString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return universities;
    }
    
    @Override
    public void deliverResult(List<String> unis) {
        this.unis = unis;
        if (isStarted()) {
            super.deliverResult(unis);
        }
    }
    
    @Override
    protected void onStartLoading() {
        if (unis != null) {
            deliverResult(unis);
        } else {
            forceLoad();
        }
    }
    
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
    
    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        unis = null;
    }
}
