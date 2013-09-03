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
import android.util.Log;

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
    
    private final PostRequestListener listener;
    
    /**
     * Constructor
     * @param listener
     * @param trustStore
     * @param url
     * @param authorizationHeader
     * @param requestEntity
     */
    public PostRequest(PostRequestListener listener, KeyStore trustStore, String url, AuthorizationHeader authorizationHeader, List<BasicNameValuePair> requestEntity) {
        this.listener = listener;
        this.url = url;
        this.requestEntity = requestEntity;
        this.authorizationHeader = authorizationHeader;
        this.trustStore = trustStore;
    }
    
    /**
     * Performs the POST request. On a valid response the listener method 
     * {@link PostRequestListener#onSuccess(SessMess)} is called on the
     * SessMess object. If there is an error in the HTTP response 
     * {@link PostRequestListener#onHttpException(HttpException)} is called on 
     * the produced HttpException.
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
                return toSessMess(HttpPostString.request(trustStore, url, authorizationHeader, requestEntity));
            } catch (HttpException e) {
                listener.onHttpException(e);
                return null;
            } catch (JSONException e) {
                listener.onJsonException(e);
                Log.e(this.toString(), "Problem with the JSON in response to post request", e);
                return null;
            } catch (Exception e) {
                listener.onFailure(e);
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
        public void onHttpException(HttpException e);
        public void onJsonException(JSONException e);
        public void onFailure(Exception e);
    }
}
