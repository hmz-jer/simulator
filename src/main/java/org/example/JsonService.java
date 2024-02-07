package org.example;

import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class JsonService {


    public static boolean sendJsonAndCheckFor413(String urlString, SSLContext sslContext, String jsonContent, boolean secureConnection) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn;

        if (secureConnection && url.getProtocol().equals("https")) {
            conn = (HttpsURLConnection) url.openConnection();
            ((HttpsURLConnection)conn).setSSLSocketFactory(sslContext.getSocketFactory());
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.writeBytes(jsonContent);
            wr.flush();
        }

        int responseCode = conn.getResponseCode();
        return responseCode == 413;
    }
}