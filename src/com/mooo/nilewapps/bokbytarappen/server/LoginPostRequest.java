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

import java.net.HttpURLConnection;
import java.security.KeyStore;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.support.v4.app.Fragment;

import com.mooo.nilewapps.androidnilewapp.HttpException;
import com.mooo.nilewapps.bokbytarappen.server.PostRequest.PostRequestCallback;
import com.mooo.nilewapps.bokbytarappen.view.LoginDialogFragment;

/**
 * Creates an easy way of making post requests by first trying to
 * perform it using a stored authentication token and if that fails
 * open up a login dialog.
 * @author nilewapp
 *
 */
public class LoginPostRequest {
    
    private final PostRequestCallback listener;
    private final String url;
    private final List<BasicNameValuePair> body;
    private final Fragment targetFragment;
    private final PostRequest postRequest;
    
    /**
     * Constructor
     * @param targetFragment
     * @param listener
     * @param trustStore
     * @param url
     * @param authorizationHeader Authentication token
     * @param body
     */
    public LoginPostRequest(
            Fragment targetFragment,
            PostRequestCallback listener,
            KeyStore trustStore,
            String url,
            NilewappAuthHeader authorizationHeader,
            List<BasicNameValuePair> body) {
        this.listener = listener;
        this.url = url;
        this.body = body;
        this.targetFragment = targetFragment;
        postRequest = authorizationHeader != null ? 
                new PostRequest(tokenPostRequestListener, trustStore, url, authorizationHeader, body) : null;
    }
    
    /**
     * Tries to execute the post request with the given authentication token. If
     * it wasn't given in the constructor, the login dialog is opened immediately.
     */
    public void execute() {
        if (postRequest != null) {
            postRequest.execute();
        } else {
            openLoginDialog();
        }
    }
    
    private PostRequestCallback loginDialogPostRequestListener = new PostRequestCallback() {

        @Override
        public void onSuccess(SessMess response) {
            listener.onSuccess(response);
        }

        @Override
        public void onHttpException(HttpException e) {
            listener.onHttpException(e);
        }

        @Override
        public void onJsonException(JSONException e) {
            listener.onJsonException(e);
        }

        @Override
        public void onFailure(Exception e) {
            listener.onFailure(e);
        }
        
    };
    
    private PostRequestCallback tokenPostRequestListener = new PostRequestCallback() {
        
        @Override
        public void onSuccess(SessMess response) {
            listener.onSuccess(response);
        }

        @Override
        public void onHttpException(HttpException e) {
            /* If communication with the server succeeded but the authentication token
               was declined, the login dialog is opened */
            if (e.getResponse().getStatusLine().getStatusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                openLoginDialog();
            } else {
                listener.onHttpException(e);
            }
        }

        @Override
        public void onJsonException(JSONException e) {
            listener.onJsonException(e);
        }

        @Override
        public void onFailure(Exception e) {
            listener.onFailure(e);
        }
    };
    
    private void openLoginDialog() {
        LoginDialogFragment dialog = LoginDialogFragment.newInstance(loginDialogPostRequestListener, url, body);
        dialog.setTargetFragment(targetFragment, 0);
        dialog.show(); 
    }
}
