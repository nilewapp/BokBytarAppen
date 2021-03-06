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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

public class AuthenticationToken {
    
    private String email;
    private String series;
    private String token;
    private long expirationTime;
    
    private static final String EMAIL  = "email";
    private static final String SERIES = "series";
    private static final String TOKEN  = "token";
    private static final String EXPIRATION_TIME = "expirationTime";

    
    public static AuthenticationToken newInstance(JSONObject json) {
        try {
            return new AuthenticationToken(json);
        } catch(JSONException e) {
            return null;
        }
    }
    
    public static AuthenticationToken newInstance(String user, String series, String token, long expirationTime) {
        if (user != null && series != null && token != null) {
            return new AuthenticationToken(user, series, token, expirationTime);
        } else {
            return null;
        }
    }
    
    private AuthenticationToken(JSONObject json) throws JSONException {
        this.email = json.getString(EMAIL);
        this.series = json.getString(SERIES);
        this.token = json.getString(TOKEN);
        this.expirationTime = json.getLong(EXPIRATION_TIME);
    }
    
    private AuthenticationToken(String user, String series, String token, long expirationTime) {
        this.email = user;
        this.series = series;
        this.token = token;
        this.expirationTime = expirationTime;
    }
    
    private String encode(String s) {
        return Base64.encodeToString(s.getBytes(), Base64.URL_SAFE|Base64.NO_WRAP);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(EMAIL).append("=\"").append(encode(email)).append("\",");
        sb.append(SERIES).append("=\"").append(encode(series)).append("\",");
        sb.append(TOKEN).append("=\"").append(encode(token)).append("\"");
        return sb.toString();
    }
    
    
    public List<BasicNameValuePair> getRequestEntity() {
        List<BasicNameValuePair> body = new ArrayList<BasicNameValuePair>(3);
        body.add(new BasicNameValuePair(EMAIL, email));
        body.add(new BasicNameValuePair(SERIES, series));
        body.add(new BasicNameValuePair(TOKEN, token));
        return body;
    }

    public String getProfile() {
        return email;
    }

    public String getSeries() {
        return series;
    }

    public String getToken() {
        return token;
    }
    
    public Long getExpirationTime() {
        return expirationTime;
    }
}
