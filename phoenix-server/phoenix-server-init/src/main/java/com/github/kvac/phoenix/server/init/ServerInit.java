package com.github.kvac.phoenix.server.init;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.Ping;
import com.github.kvac.phoenix.server.db.DataBaseHeader;
import com.github.kvac.phoenix.server.network.NetWorkHeader;

/**
 * @author jdcs_dev
 *
 */
public class ServerInit {

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		try {

			// System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, Level.INFO.toString());
			DataBaseHeader.getDataBase().init();
			DataBaseHeader.getDataBase().connect();
			DataBaseHeader.getDataBase().create();
			// DataBaseHeader.getDataBaseHandler().start();

			NetWorkHeader.getServer().start();

			new Thread(new Runnable() {

				@Override
				public void run() {
					Ping pong = new Ping();
					do {
						EventHEADER.getBus_Pong().post(pong);
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
						}
					} while (true);
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
