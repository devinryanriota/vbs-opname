package com.projects.devin.opname.cls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by devin on 2/26/2017.
 */

public class WebService {
    public String responseBody;

    public WebService(String urlString, String method, String params) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.connect();
            if(method == "POST") {
                connection.getOutputStream().write(params.getBytes());
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            StringBuilder lines = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.append(line);
            }
            responseBody = lines.toString();
        }
        catch (MalformedURLException e){
            e.printStackTrace();
            responseBody = e.toString();
        }
        catch (IOException e){
            e.printStackTrace();
            responseBody = e.toString();
        }
    }
}
