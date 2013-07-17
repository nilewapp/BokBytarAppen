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

import android.app.Activity;
import android.os.Bundle;
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
import com.mooo.nilewapps.androidnilewapp.Preferences;

public class RegistrationFragment extends SherlockFragment
        implements FilterableListDialogFragment.FilterableListDialogListener {

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
        
        return layout;
        
    }

    public void onDialogItemClick(String university) {
        Preferences.put(getActivity(), R.string.setting_university_key, university);
        updateUniversityTextView((TextView) getView().findViewById(R.id.setting_university_value));
    }
    
    private void updateUniversityTextView(TextView university) {
        final Activity activity = getActivity();
        final String uninitialisedUniversityHelp = 
                activity.getResources().getText(R.string.uninitialised_university_help).toString();
        university.setText(
                Preferences.get(
                        activity,
                        R.string.setting_university_key,
                        uninitialisedUniversityHelp));
    }
    
    private String getTextFromId(int id) {
        return ((EditText) getView().findViewById(id)).getText().toString();
    }
    
    private void registerUser() {
        String email      = getTextFromId(R.id.email);
        String name       = getTextFromId(R.id.name);
        String phone      = getTextFromId(R.id.phone);
        String university = Preferences.get(getActivity(), R.string.setting_university_key);
        String password   = getTextFromId(R.id.password);
        new Registry().register(getActivity(), email, name, phone, university, password);
    }
    
    private void unregisterUser() {
        String email    = getTextFromId(R.id.email);
        String password = getTextFromId(R.id.password);
        new Registry().unregister(getActivity(), email, password);
    }


    private void tokenAuthenticateUser() {
        String email = getTextFromId(R.id.email);
        new ServerTokenAuthenticationTest().unregisterUser(getActivity(), email, "123", "456");
    }
}
