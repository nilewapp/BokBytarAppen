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

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import android.content.Context;
import android.content.res.Resources;

/**
 * Provides functionality to load SSL key stores from the resources
 * @author nilewapp
 *
 */
public class TrustManager {
    
    private static KeyStore keyStore = null;
    
    /**
     * Lazily loads the key store from resources
     * @param context
     * @return
     * @throws KeyStoreException 
     * @throws IOException 
     * @throws CertificateException 
     * @throws NoSuchAlgorithmException 
     * @throws Exception
     */
    public static KeyStore getKeyStore(Context context) 
            throws KeyStoreException, 
                   NoSuchAlgorithmException, 
                   CertificateException, 
                   IOException {
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
