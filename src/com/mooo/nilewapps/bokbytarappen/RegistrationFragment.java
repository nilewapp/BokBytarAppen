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

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.mooo.nilewapps.androidnilewapp.FilterableListDialogFragment;
import com.mooo.nilewapps.androidnilewapp.HttpException;
import com.mooo.nilewapps.androidnilewapp.Preferences;

public class RegistrationFragment extends SherlockFragment
        implements FilterableListDialogFragment.FilterableListDialogListener,
            LoginDialog.LoginDialogListener {

    @Override
    public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState) {
        
        final ScrollView layout = (ScrollView) inflater.inflate(R.layout.fragment_registration, null, false);
        final LinearLayout view = (LinearLayout) layout.findViewById(R.id.registration_view);
        
        final LinearLayout university = (LinearLayout) view.findViewById(R.id.setting_university);
        university.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UniversityListDialog dialog = new UniversityListDialog();
                dialog.setTargetFragment(RegistrationFragment.this, 0);
                dialog.show();
            }
        });
        
        updateUniversityTextView((TextView) view.findViewById(R.id.setting_university_value));
        
        final Button register = (Button) view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        
        final Button unregister = (Button) view.findViewById(R.id.unregister);
        unregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterUser();
            }
        });

        final Button tokenAuthentication = (Button) view.findViewById(R.id.tokenauthentication);
        tokenAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenAuthenticateUser();
            }
        });

        final Button initToken = (Button) view.findViewById(R.id.inittoken);
        initToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initToken();
            }
        });        
        return layout;
        
    }

    public void onDialogItemClick(String university) {
        Preferences.put(getActivity(), R.string.key_university, university);
        updateUniversityTextView((TextView) getView().findViewById(R.id.setting_university_value));
    }
    
    private void updateUniversityTextView(TextView university) {
        final Activity activity = getActivity();
        final String uninitialisedUniversityHelp = 
                activity.getResources().getText(R.string.uninitialised_university_help).toString();
        university.setText(
                Preferences.get(
                        activity,
                        R.string.key_university,
                        uninitialisedUniversityHelp));
    }
    
    private String getTextFromId(int id) {
        return ((EditText) getView().findViewById(id)).getText().toString();
    }
    
    private void registerUser() {
        String email      = getTextFromId(R.id.email);
        String name       = getTextFromId(R.id.name);
        String phone      = getTextFromId(R.id.phone);
        String university = Preferences.get(getActivity(), R.string.key_university);
        String password   = getTextFromId(R.id.password);
        new Registry().register(getActivity(), email, name, phone, university, password);
    }
    
    private void unregisterUser() {
        String email    = getTextFromId(R.id.email);
        String password = getTextFromId(R.id.password);
        new Registry().unregister(getActivity(), email, password);
    }

    private void tokenAuthenticateUser() {
        Resources res = getResources();
        String url = res.getString(R.string.url_server) + res.getString(R.string.url_unregister);
        List<BasicNameValuePair> body = new ArrayList<BasicNameValuePair>();
        /* Add AuthenticationToken to the request body */
        body.addAll(TokenManager.getToken(getActivity()).getRequestEntity());
        body.add(new BasicNameValuePair("name", "blubbi"));
        PostRequest request = new PostRequest(this, url, body);
        try {
            PostRequest.PostRequestTask task = request.execute();
            String response = task.get();
            HttpException e = task.getHttpException();
            if (e != null && e.getResponse().getStatusLine().getStatusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.i(this.toString(), "Opening login dialog");
                LoginDialog dialog = LoginDialog.newInstance(url, body);
                dialog.setTargetFragment(this, 0);
                dialog.show();
                Log.i(this.toString(), "Login Dialog should be showing");
            } else {
                Log.i(this.toString(), response);
            }
        } catch (Exception e) {
            Log.e(this.toString(), "Uncaught exception", e);
        }
    }
    
    private void initToken() {
        TokenManager.setToken(getActivity(), new AuthenticationToken("a", "1", "1", 0));
    }

    @Override
    public void onLoginSuccessful(String response) {
        Log.i(this.toString(), response);
    }

    @Override
    public void onLoginFailed(Exception e) {
        Log.e(this.toString(), "Uncaught exception", e);
    }
}