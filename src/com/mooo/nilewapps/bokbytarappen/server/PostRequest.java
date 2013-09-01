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
package com.mooo.nilewapps.bokbytarappen.server;

import java.security.KeyStore;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.mooo.nilewapps.androidnilewapp.HttpException;
import com.mooo.nilewapps.androidnilewapp.HttpPostString;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Class that performs an asynchronous HTTP POST request. Always sends
 * the AuthenticationToken stored on the device or a username/password pair
 * to the server. It also expects that an AuthenticationToken should be 
 * sent in the response. The previously stored token is replaced by the
 * received token.
 * @author nilewapp
 *
 */
public class PostRequest {
    
    /**
     * Name of the JSON object containing the AuthenticationToken.
     */
    public static final String TOKEN = "sess";
    
    /**
     * Name of the JSON object containing the response message.
     */
    public static final String MESSAGE = "mess";
    
    private final String url;
    private final List<BasicNameValuePair> requestEntity;
    private final NilewappAuthHeader authorizationHeader;

    private final String profile;
    private final String password;
    
    private final KeyStore trustStore;
    
    private final PostRequestListener listener;
    
    /**
     * Constructor. Sends the AuthenticationToken with the request.
     * @param fragment host fragment
     * @param url service url
     * @param requestEntity request body
     */
    public PostRequest(PostRequestListener listener, KeyStore trustStore, String url, NilewappAuthHeader authorizationHeader, List<BasicNameValuePair> requestEntity) {
        this.listener = listener;
        this.url = url;
        this.requestEntity = requestEntity;
        this.authorizationHeader = authorizationHeader;
        this.profile = null;
        this.password = null;
        this.trustStore = trustStore;
    }
    
    /**
     * Constructor. Sends a username/password pair with the request.
     * @param fragment
     * @param url
     * @param profile
     * @param password
     * @param requestEntity2
     */
    public PostRequest(PostRequestListener listener, KeyStore trustStore, String url, String profile, String password, List<BasicNameValuePair> requestEntity) {
        this.listener = listener;
        this.url = url;
        this.requestEntity = requestEntity;
        this.authorizationHeader = null;
        this.profile = profile;
        this.password = password;
        this.trustStore = trustStore;
    }
    
    /**
     * Performs the POST request.
     * @return the request task
     */
    public void execute() {
        PostRequestTask task = new PostRequestTask(); 
        task.execute();
    }

    class PostRequestTask extends AsyncTask<Void, Void, SessMess> {

        private HttpException httpEx = null;

        public HttpException getHttpException() {
            return httpEx;
        }

        @Override
        protected SessMess doInBackground(Void...voids) {
            try {
                if (authorizationHeader != null) {
                    return toSessMess(HttpPostString.request(trustStore, url, authorizationHeader, requestEntity));
                } else {
                    return toSessMess(HttpPostString.request(trustStore, url, profile, password, requestEntity));
                }
            } catch (HttpException e) {
                listener.onFailure(e);
                return null;
            } catch (Exception e) {
                Log.e(this.toString(), "Uncaught PostRequest exception", e);
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(SessMess response) {
            if (response != null) {
                listener.onSuccess(response);
            } else {
                Log.w(this.toString(), "Failed to communicate with server");
            }
        }
        
        private SessMess toSessMess(String response) throws JSONException {
            JSONObject json = new JSONObject(response);
            JSONObject session = json.getJSONObject(TOKEN);
            String message = json.getString(MESSAGE);
            AuthenticationToken token = new AuthenticationToken(session);
            return new SessMess(token, message);
        }
    }
    
    public interface PostRequestListener {
        public void onSuccess(SessMess response);
        public void onFailure(HttpException e);
    }
}
