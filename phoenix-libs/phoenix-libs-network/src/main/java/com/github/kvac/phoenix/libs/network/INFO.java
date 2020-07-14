package com.github.kvac.phoenix.libs.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class INFO {

    public static String getExternalIP() {
        String ip = null;

        ArrayList<String> urList = new ArrayList<>();
        urList.add("https://ident.me/");
        urList.add("https://checkip.amazonaws.com/");
        urList.add("https://icanhazip.com/");
        urList.add("https://agentgatech.appspot.com/");
        while (ip == null) {
            for (int i = 0; i < urList.size(); i++) {
                try {
                    ip = readFromSite(urList.get(i));
                } catch (IOException ex) {
                    Logger.getLogger(INFO.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (ip != null) {
                    break;
                }
            }
        }
        return ip;
    }

    static String readFromSite(String site) throws MalformedURLException, IOException {

        String ip;
        try (InputStream urlOpenStream = new URL(site).openStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(urlOpenStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            ip = bufferedReader.readLine();
            bufferedReader.close();
            inputStreamReader.close();
            urlOpenStream.close();
        }

        return ip;
    }
}
