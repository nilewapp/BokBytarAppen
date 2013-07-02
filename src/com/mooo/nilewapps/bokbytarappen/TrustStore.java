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
