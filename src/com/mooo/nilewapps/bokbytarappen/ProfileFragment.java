package com.mooo.nilewapps.bokbytarappen;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import com.mooo.nilewapps.androidnilewapp.FilterableListDialogFragment;
import com.mooo.nilewapps.androidnilewapp.PreferenceUtil;

import com.actionbarsherlock.app.SherlockFragment;

public class ProfileFragment extends SherlockFragment
        implements FilterableListDialogFragment.FilterableListDialogListener {

    @Override
    public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState) {
        
        final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_profile, null, false);
        
        createProfileHeader(view);
        
        final LinearLayout university = (LinearLayout) view.findViewById(R.id.setting_university);
        university.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UniversityListDialog dialog = new UniversityListDialog();
                dialog.setTargetFragment(ProfileFragment.this, 0);
                dialog.show();
            }
        });
        
        updateUniversityTextView((TextView) view.findViewById(R.id.setting_university_value));
        
        return view;
        
    }

    public void onDialogItemClick(String university) {
        storeUniversity(university);
        updateUniversityTextView((TextView) getView().findViewById(R.id.setting_university_value));
    }
    
    private void updateUniversityTextView(TextView university) {
        final Activity activity = getActivity();
        final String uninitialisedUniversityHelp = 
                activity.getResources().getText(R.string.uninitialised_university_help).toString();
        university.setText(
                PreferenceUtil.getPreference(
                        activity,
                        R.string.setting_university_key,
                        uninitialisedUniversityHelp));
    }

    private void storeUniversity(String item) {
        PreferenceUtil.storePreference(getActivity(), R.string.setting_university_key, item);
    }
    
    private void createProfileHeader(final View view) {
        final TextView tv = (TextView) view.findViewById(R.id.user_name);
        final ProfilePictureView pv = (ProfilePictureView) view.findViewById(R.id.profile_pic);
        
        /* Show stored username */
        tv.setText(PreferenceUtil.getPreference(getActivity(), R.string.username_key, ""));
        
        Request request = Request.newMeRequest(Session.getActiveSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    /* Get downloaded username */
                    String username = user.getName();
                    if (!username.equals(tv.getText().toString())) {
                        /* Update the shown username if it is different from the stored one */
                        tv.setText(username);
                        PreferenceUtil.storePreference(getActivity(), R.string.username_key, username);
                    }
                    
                    /* Show profile picture */
                    pv.setProfileId(user.getId());
                }
            }
        });
        
        request.executeAsync();
    }
    
}
