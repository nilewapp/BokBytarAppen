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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticationToken {
    
    private String profile;
    private String series;
    private String token;
    private long expirationTime;
    
    private static final String PROFILE = "profile";
    private static final String SERIES = "series";
    private static final String TOKEN = "token";
    private static final String EXPIRATION_TIME = "expirationTime";

    public AuthenticationToken(JSONObject json) throws JSONException {
        this.profile = json.getString(PROFILE);
        this.series = json.getString(SERIES);
        this.token = json.getString(TOKEN);
        this.expirationTime = json.getLong(EXPIRATION_TIME);
    }
    
    public AuthenticationToken(String user, String series, String token, long expirationTime) {
        this.profile = user;
        this.series = series;
        this.token = token;
        this.expirationTime = expirationTime;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PROFILE).append("=").append(profile).append("&");
        sb.append(SERIES).append("=").append(series).append("&");
        sb.append(TOKEN).append("=").append(token);
        return sb.toString();
    }
    
    public List<NameValuePair> getRequestEntity() {
        List<NameValuePair> body = new ArrayList<NameValuePair>(3);
        body.add(new BasicNameValuePair(PROFILE, profile));
        body.add(new BasicNameValuePair(SERIES, series));
        body.add(new BasicNameValuePair(TOKEN, token));
        return body;
    }

    public String getProfile() {
        return profile;
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
