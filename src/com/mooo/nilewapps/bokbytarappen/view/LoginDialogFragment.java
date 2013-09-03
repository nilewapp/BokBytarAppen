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
package com.mooo.nilewapps.bokbytarappen.view;

import java.security.KeyStore;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mooo.nilewapps.androidnilewapp.BasicAuthorizationHeader;
import com.mooo.nilewapps.androidnilewapp.HttpException;
import com.mooo.nilewapps.androidnilewapp.ParcelableRequestEntity;
import com.mooo.nilewapps.androidnilewapp.Preferences;
import com.mooo.nilewapps.bokbytarappen.R;
import com.mooo.nilewapps.bokbytarappen.TrustManager;
import com.mooo.nilewapps.bokbytarappen.server.PostRequest;
import com.mooo.nilewapps.bokbytarappen.server.PostRequest.PostRequestListener;
import com.mooo.nilewapps.bokbytarappen.server.SessMess;

/**
 * Fragment defining a login dialog, performs an HTTP post request.
 * @author nilewapp
 *
 */
public class LoginDialogFragment extends DialogFragment {
    
    public static final String URL = "key_url";
    public static final String REQUEST_ENTITY = "key_request_entity";
    
    private String url;
    private List<BasicNameValuePair> requestEntity;
    
    private LinearLayout layout;
    
    private PostRequestListener listener;
    
    public static LoginDialogFragment newInstance(PostRequestListener listener, String url, List<BasicNameValuePair> requestEntity) {
        final LoginDialogFragment d = new LoginDialogFragment();
        final PostRequestListener requestListener = listener; 
        
        /* Replicates the behaviour of the listener in the arguments, but
           adds a call to the dialog dismiss method on a successful login. */
        d.listener = new PostRequestListener() {
            
            @Override
            public void onSuccess(SessMess response) {
                requestListener.onSuccess(response);
                d.getDialog().dismiss();
            }

            @Override
            public void onHttpException(HttpException e) {
                requestListener.onHttpException(e);
            }

            @Override
            public void onJsonException(JSONException e) {
                requestListener.onJsonException(e);
            }

            @Override
            public void onFailure(Exception e) {
                requestListener.onFailure(e);
            }
            
        };
        
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
        
        String storedEmail = Preferences.get(getActivity(), R.string.key_authentication_email);
        if (storedEmail != null) {
            EditText email = (EditText) layout.findViewById(R.id.email);
            email.setText(storedEmail);
        }
        
        TextView register = (TextView) layout.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "REGISTR MEH PLZ", Toast.LENGTH_SHORT).show();
            }
        });
        
        final AlertDialog dialog = builder
                .setView(layout)
                .setTitle(R.string.app_name)
                .setPositiveButton(R.string.signin, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        
        /* Make the dialog resize when the soft keyboard is open */
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        
        return dialog;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        /* The OnClickListener for the positive button is set here in
           order to make sure that the dialog isn't automatically dismissed
           whenever the button is pressed. */
        final AlertDialog d = (AlertDialog) getDialog();
        Button positiveButton = d.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    
    private String getTextFromId(int id) {
        return ((EditText) layout.findViewById(id)).getText().toString();
    }
    
    private void login() {
        String profile = getTextFromId(R.id.email);
        String password = getTextFromId(R.id.password);
        KeyStore trustStore = null;
        try {
            trustStore = TrustManager.getKeyStore(getActivity());
        } catch (Exception e) {
            Log.e(this.toString(), "Failed to load trust store", e);
        }
        PostRequest request = new PostRequest(listener, trustStore, url, new BasicAuthorizationHeader(profile, password), requestEntity);
        request.execute();
    }
    
    public void show() {
        show(getTargetFragment().getFragmentManager(), "login_dialog");
    }
}
