package com.github.kvac.phoenix.server.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.github.kvac.phoenix.libs.objects.Ping;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;
import lombok.Setter;

public class ServerWorker implements Runnable {
	@Getter
	@Setter
	private Server server;
	@Getter
	@Setter
	private Socket client;

	public ServerWorker(Server server, Socket client) {
		setServer(server);
		setClient(client);
	}

	
	
	@Getter
	private Writter writter = new Writter();

	
	class Writter implements Runnable {
		
		
		@Override
		public void run() {
			System.err.println(1);
		}

	}

	@Getter
	@Setter
	private ObjectInputStream ois;
	@Getter
	@Setter
	private boolean stoppd_reader;

	@Override
	public void run() {
		try {
			openReadStream();
		} catch (IOException e) {
			setStoppd_reader(true);
		}
		Object object;
		while (isStoppd_reader() == false) {
			try {
				object = getOis().readObject();
				if (object instanceof Ping) {
					System.out.println("Ping");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				setStoppd_reader(true);
				e.printStackTrace();
			}

		}
	}

	private void openReadStream() throws IOException {
		setOis(new ObjectInputStream(getClient().getInputStream()));
	}

	@Subscribe
	private void pongSend(Ping pong) {

	}

}
