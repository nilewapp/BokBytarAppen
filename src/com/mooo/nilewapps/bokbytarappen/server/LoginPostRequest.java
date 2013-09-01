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

import android.support.v4.app.Fragment;

import com.mooo.nilewapps.androidnilewapp.HttpException;
import com.mooo.nilewapps.bokbytarappen.server.PostRequest.PostRequestListener;
import com.mooo.nilewapps.bokbytarappen.view.LoginDialogFragment;

/**
 * Creates an easy way of making post requests by first trying to
 * perform it using a stored authentication token and if that fails
 * open up a login dialog.
 * @author nilewapp
 *
 */
public class LoginPostRequest {
    
    private final PostRequestListener listener;
    private final String url;
    private final List<BasicNameValuePair> body;
    private final Fragment targetFragment;
    private final PostRequest postRequest;
    
    public LoginPostRequest(Fragment targetFragment, PostRequestListener listener, KeyStore trustStore, String url, NilewappAuthHeader authorizationHeader, List<BasicNameValuePair> body) {
        this.listener = listener;
        this.url = url;
        this.body = body;
        this.targetFragment = targetFragment;
        postRequest = new PostRequest(tokenPostRequestListener, trustStore, url, authorizationHeader, body);
    }
    
    public void execute() {
        postRequest.execute();
    }
    
    private PostRequestListener loginDialogPostRequestListener = new PostRequestListener() {

        @Override
        public void onSuccess(SessMess response) {
            listener.onSuccess(response);
        }

        @Override
        public void onFailure(HttpException e) {
            listener.onFailure(e);
        }
        
    };
    
    private PostRequestListener tokenPostRequestListener = new PostRequestListener() {
        
        @Override
        public void onSuccess(SessMess response) {
            listener.onSuccess(response);
        }

        @Override
        public void onFailure(HttpException e) {
            if (e.getResponse().getStatusLine().getStatusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                LoginDialogFragment dialog = LoginDialogFragment.newInstance(loginDialogPostRequestListener, url, body);
                dialog.setTargetFragment(targetFragment, 0);
                dialog.show();
            } else {
                listener.onFailure(e);
            }
        }
        
    };
}
