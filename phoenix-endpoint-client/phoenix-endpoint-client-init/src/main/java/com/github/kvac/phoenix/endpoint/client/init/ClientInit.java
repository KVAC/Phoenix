package com.github.kvac.phoenix.endpoint.client.init;

import java.sql.SQLException;

import com.github.kvac.phoenix.endpoint.client.db.DataBaseHeader;
import com.github.kvac.phoenix.endpoint.client.gui.ClientGui;
import com.github.kvac.phoenix.endpoint.client.network.NetWorkHeader;
import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.Ping;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.logger.Log.Level;

public class ClientInit {
	public static void main(String[] args) throws SQLException {
		System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, Level.INFO.toString());

		// FIXME
		new NetWorkHeader();

		DataBaseHeader.getDataBase().connect();// YED 28.03.2020
		DataBaseHeader.getDataBase().CreateDB();// YED 28.03.2020
		DataBaseHeader.getDataBase().createDefault();// YED 28.03.2020
		DataBaseHeader.getDataBase().restoreInfo();// YED 28.03.2020

		DataBaseHeader.getDataBaseHandler().start();

		NetWorkHeader.getNetWorkD().start();
		NetWorkHeader.getClientHandler().start();

		ClientGui clientGui = new ClientGui();
		clientGui.setVisible(true);

		new Thread(new Runnable() {

			@Override
			public void run() {
				Ping ping = new Ping();
				do {
					EventHEADER.getBus_Ping().post(ping);

					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
				} while (true);
			}
		}).start();

	}
}
