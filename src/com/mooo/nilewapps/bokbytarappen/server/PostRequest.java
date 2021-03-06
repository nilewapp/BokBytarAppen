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

import com.mooo.nilewapps.androidnilewapp.AuthorizationHeader;
import com.mooo.nilewapps.androidnilewapp.HttpException;
import com.mooo.nilewapps.androidnilewapp.HttpPostString;

import android.os.AsyncTask;

/**
 * Class that performs an asynchronous HTTP POST request. Always sends
 * an authorization header to the server. The response is expected to
 * be a new session token and an arbitrary message in JSON format.
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
    private final AuthorizationHeader authorizationHeader;
    
    private final KeyStore trustStore;
    
    private final PostRequestCallback listener;
    
    /**
     * Constructor
     * @param listener
     * @param trustStore
     * @param url
     * @param authorizationHeader
     * @param requestEntity
     */
    public PostRequest(PostRequestCallback listener, KeyStore trustStore, String url, AuthorizationHeader authorizationHeader, List<BasicNameValuePair> requestEntity) {
        this.listener = listener;
        this.url = url;
        this.requestEntity = requestEntity;
        this.authorizationHeader = authorizationHeader;
        this.trustStore = trustStore;
    }
    
    /**
     * Performs the POST request.
     */
    public void execute() {
        PostRequestTask task = new PostRequestTask(); 
        task.execute();
    }

    class PostRequestTask extends AsyncTask<Void, Void, SessMess> {
        
        private HttpException eHttp = null;
        private JSONException eJson = null;
        private Exception eOther = null;
        
        @Override
        protected SessMess doInBackground(Void...voids) {
            try {
                return toSessMess(HttpPostString.request(trustStore, url, authorizationHeader, requestEntity));
            } catch (HttpException e) {
                eHttp = e;
            } catch (JSONException e) {
                eJson = e;                
            } catch (Exception e) {
                eOther = e;
            }
            return null;
        }
        
        @Override
        protected void onPostExecute(SessMess response) {
            if (response != null) {
                listener.onSuccess(response);
            } else if (eHttp != null) {
                listener.onHttpException(eHttp);
            } else if (eJson != null) {
                listener.onJsonException(eJson);
            } else {
                listener.onFailure(eOther);
            }
        }
        
        private SessMess toSessMess(String response) throws JSONException {
            JSONObject json = new JSONObject(response);
            AuthenticationToken token;
            try {
                JSONObject session = json.getJSONObject(TOKEN);
                token = AuthenticationToken.newInstance(session);
            } catch (JSONException e) {
                token = null;
            }
            String message = json.getString(MESSAGE);
            return new SessMess(token, message);
        }
    }
    
    /**
     * Interface definition for callback methods to be invoked after
     * a successful or failed post request.
     * @author nilewapp
     *
     */
    public interface PostRequestCallback {
        
        /**
         * Called when the request was successfully authenticated and
         * a syntactically valid response was returned.
         * @param response
         */
        public void onSuccess(SessMess response);
        
        /**
         * Called when some HTTP status code other than OK was returned.
         * @param e
         */
        public void onHttpException(HttpException e);
        
        /**
         * Called when the HTTP response was OK but the returned JSON
         * was not of the correct format, i.e. did not contain a
         * {@link PostRequest#MESSAGE} JSON object.
         * @param e
         */
        public void onJsonException(JSONException e);
        
        /**
         * Called when some other error occurred.
         * @param e
         */
        public void onFailure(Exception e);
    }
}
