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

import com.mooo.nilewapps.androidnilewapp.Preferences;
import com.mooo.nilewapps.bokbytarappen.server.AuthenticationToken;

/**
 * Stores and extracts an AuthenticationToken on the device.
 * @author nilewapp
 *
 */
public class TokenManager {

    public static AuthenticationToken getToken(Activity activity) {
        String user = Preferences.get(activity, R.string.key_authentication_profile);
        String series = Preferences.get(activity, R.string.key_authentication_series);
        String token = Preferences.get(activity, R.string.key_authentication_token);
        String expiration = Preferences.get(activity, R.string.key_authentication_expiration_time, "-1");
        long expirationTime = Long.parseLong(expiration);
        return new AuthenticationToken(user, series, token, expirationTime);
    }
    
    public static void setToken(Activity activity, AuthenticationToken token) {
        Preferences.put(activity, R.string.key_authentication_profile, token.getProfile());
        Preferences.put(activity, R.string.key_authentication_series, token.getSeries());
        Preferences.put(activity, R.string.key_authentication_token, token.getToken());
        Preferences.put(activity, R.string.key_authentication_expiration_time, token.getExpirationTime().toString());
    }
}
