package com.github.kvac.phoenix.endpoint.client.db;

import java.sql.SQLException;

public class DataBaseHandler extends Thread implements Runnable {
	public DataBaseHandler() {
	}

	@Override
	public void run() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("Settings get");
				do {
					try {
						DataBaseHeader.getDataBase().restoreInfo();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (true);
			}
		}).start();
	}
}
