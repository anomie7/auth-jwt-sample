package com.withkid.auth.utills;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class JwtUtill {

    public static String getSecretKeyOf(String filePath) throws IOException, JSONException {
            StringBuilder sb = new StringBuilder();
            String line;

            BufferedReader br = new BufferedReader(
                    new FileReader(filePath));

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());
            return json.getString("k");
    }
}
