package com.github.kvac.phoenix.libs.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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
				ip = readFromSite(urList.get(i));
				if (ip != null) {
					break;
				}
			}
		}
		return ip;
	}

	static String readFromSite(String site) {
		String ip = null;
		try {
			InputStream urlOpenStream = new URL(site).openStream();
			InputStreamReader inputStreamReader = new InputStreamReader(urlOpenStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			ip = bufferedReader.readLine();
		} catch (Exception e) {
		}
		return ip;
	}
}
