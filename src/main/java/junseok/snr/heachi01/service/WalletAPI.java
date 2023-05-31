package junseok.snr.heachi01.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WalletAPI {

    public void createWallet() throws Exception {
        String clientId = "815fcd01324b8f75818a755a72557750";
        String urlString = "https://tn.henesis.io/ethereum/goerli";

        // Create a Url object from the urlString.
        URL url = new URL(urlString);

        // Open a connection to the URL.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method (GET is the default).
        connection.setRequestMethod("GET");

        // Set the request property "clientId".
        connection.setRequestProperty("clientId", clientId);

        // Get the response code.
        int responseCode = connection.getResponseCode();

        // Read the response.
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        // Close connections.
        in.close();
        connection.disconnect();

        System.out.println("Response Code: " + responseCode);
        System.out.println("Response Content: " + content.toString());
    }
}
