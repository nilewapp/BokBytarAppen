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

/**
 * Represents the response from the server in the form 
 * of a session variable and a response message.
 * @author nilewapp
 *
 */
public class SessMess {
    
    private final AuthenticationToken token;
    private final String message;
    
    public SessMess(AuthenticationToken token, String message) {
        this.token = token;
        this.message = message;
    }

    public AuthenticationToken getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}
