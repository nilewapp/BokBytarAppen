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

import java.io.InputStream;
import java.security.KeyStore;

import android.content.Context;
import android.content.res.Resources;

public class TrustStore {
    
    private static KeyStore keyStore = null;
    
    public static KeyStore get(Context context) throws Exception {
        if (keyStore == null) {
            KeyStore trustStore = KeyStore.getInstance("BKS");
            Resources res = context.getResources();
            InputStream in = res.openRawResource(R.raw.public_truststore);
            String trustStorePassword = res.getString(R.string.truststore_password);
            trustStore.load(in, trustStorePassword.toCharArray());
            keyStore = trustStore;
        }
        return keyStore;
    }
    
}
