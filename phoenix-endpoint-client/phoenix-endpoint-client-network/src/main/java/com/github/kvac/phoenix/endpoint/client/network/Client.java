package com.github.kvac.phoenix.endpoint.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.Ping;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;
import lombok.Setter;

public class Client implements Runnable {
	@Getter
	@Setter
	private String host;
	@Getter
	@Setter
	private int port;
	@Getter
	@Setter
	private Socket socket;

	public Client() {
		EventHEADER.getBus_Ping().register(this);
	}

	@Getter
	@Setter
	private Reader reader = new Reader();

	class Reader implements Runnable {
		@Override
		public void run() {
			System.out.println(getClass());
			stopdd();
		}
	}

	boolean writerstop = false;

	@Override
	public void run() {
		System.err.println(getClass());
		try {
			openWriter();
		} catch (IOException e) {
			e.printStackTrace();
			writerstop = true;
		}
		while (writerstop == false) {

		}
		stopdd();
	}

	private void stopdd() {
		// TODO Auto-generated method stub

	}

	@Getter
	@Setter
	private ObjectOutputStream oos;

	private void openWriter() throws IOException {
		setOos(new ObjectOutputStream(getSocket().getOutputStream()));
		System.out.println("Client.openWriter()");
	}

	public void connect() throws UnknownHostException, IOException {
		setSocket(new Socket(getHost(), getPort()));
	}

	private void sendObject(Object object) throws IOException {
		getOos().writeObject(object);
	}

	@Subscribe
	private void pingServer(Ping ping) {
		try {
			sendObject(ping);
		} catch (IOException e) {
			writerstop = true;
			System.err.println("Client.pingServer()");
			e.printStackTrace();
		}
	}

}
