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

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mooo.nilewapps.androidnilewapp.ParcelableRequestEntity;
import com.mooo.nilewapps.androidnilewapp.R;
import com.mooo.nilewapps.bokbytarappen.PostRequest.PostRequestTask;

public class LoginDialog extends DialogFragment {
    
    public static final String URL = "url";
    public static final String REQUEST_ENTITY = "request_entity";
    
    private String url;
    private List<BasicNameValuePair> requestEntity;
    
    private LinearLayout layout;
    
    private LoginDialogListener listener;
    
    public static LoginDialog newInstance(String url, List<BasicNameValuePair> requestEntity) {
        LoginDialog d = new LoginDialog();
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putParcelable(REQUEST_ENTITY, new ParcelableRequestEntity(requestEntity));
        d.setArguments(args);
        return d;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        url = getArguments().getString(URL);
        requestEntity = ((ParcelableRequestEntity) getArguments().getParcelable(REQUEST_ENTITY)).getEntity();
    
        final Activity activity = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater inflater = activity.getLayoutInflater();
        layout = (LinearLayout) inflater.inflate(R.layout.dialog_login, null);
        
        Button login = (Button) layout.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        
        final Dialog dialog = builder.create();
        
        /* Make the dialog resize when the soft keyboard is open */
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        
        return dialog;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /* Get target fragment and assert that it implements the listener */
        final Fragment frag = getTargetFragment();
        if (frag == null) {
            throw new RuntimeException(this.toString() + " target fragment not set.");
        }
        try {
            listener = (LoginDialogListener) frag;
        } catch (ClassCastException e) {
            throw new ClassCastException(frag.toString()
                    + " must implement " + LoginDialogListener.class.getName());
        }
    }
    
    private String getTextFromId(int id) {
        return ((EditText) getView().findViewById(id)).getText().toString();
    }
    
    private String login() {
        String profile = getTextFromId(R.id.email);
        String password = getTextFromId(R.id.password);
        PostRequest request = new PostRequest(this.getTargetFragment(), url, profile, password, requestEntity);
        PostRequestTask task = request.execute();
        try {
            String response = task.get();
            if (response != null) {
                listener.onLoginSuccessful(response);
            } else {
                listener.onLoginFailed(task.getException());
            }
        } catch (Exception e) {
            listener.onLoginFailed(e);
        }
        return null;
    }
    
    public void show() {
        show(getTargetFragment().getFragmentManager(), "login_dialog");
    }
    
    public interface LoginDialogListener {
        public void onLoginSuccessful(String response);
        public void onLoginFailed(Exception e);
    }
    
    
}
