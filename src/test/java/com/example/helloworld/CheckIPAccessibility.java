package com.example.helloworld;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class CheckIPAccessibility {
    public static void main(String[] args) {
        String ipAddress = "20.151.216.203";
        int port = 8050;
        String url = "http://" + ipAddress + ":" + port + "/helloworld-0.0.1-SNAPSHOT/hello";

        try {
            // Create the HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Check if the response code is 200 (OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response content
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Print the response content
                System.out.println("IP is accessible. Response: " + response.toString());
            } else {
                System.out.println("IP is not accessible. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
