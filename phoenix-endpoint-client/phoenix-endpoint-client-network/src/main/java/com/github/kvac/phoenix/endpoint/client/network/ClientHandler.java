package com.github.kvac.phoenix.endpoint.client.network;

import java.net.ConnectException;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;

public class ClientHandler extends Thread implements Runnable {

	@Override
	public void run() {
		do {
			Client client = null;
			try {

				client = new Client();
				client.setHost("127.0.0.1");
				client.setPort(5000);

				client.connect();

				new Thread(client).start();
				new Thread(client.getReader()).start();

				// client.send(new String("aaaaaaaaaaa"));

				break;
			} catch (ConnectException e) {
				System.err.println(client.getHost() + ":" + client.getPort());
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				EventHEADER.getBus_Ping().unregister(client);
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (true);
	}

}
