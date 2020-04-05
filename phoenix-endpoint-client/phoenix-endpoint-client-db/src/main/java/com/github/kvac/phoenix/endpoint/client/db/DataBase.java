/**
 * 
 */
package com.github.kvac.phoenix.endpoint.client.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.MySettings;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.github.kvac.phoenix.libs.objects.events.MyEvent.TYPE;
import com.google.common.eventbus.Subscribe;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * @author jdcs_dev
 *
 */
public class DataBase {
	public DataBase() {
		EventHEADER.getBus_mysettings().register(this);
		EventHEADER.getBus_cs_save().register(this);
	}

	JdbcPooledConnectionSource connectionSource;

	Dao<CS, String> CSDao;
	Dao<MySettings, String> SettingsDao;
	Dao<Message, String> MessageDao;

	public void connect() throws SQLException {
		connectionSource = new JdbcPooledConnectionSource(DataBaseHeader.DB_NAME_STRING);
		CSDao = DaoManager.createDao(connectionSource, CS.class);
		SettingsDao = DaoManager.createDao(connectionSource, MySettings.class);
		MessageDao = DaoManager.createDao(connectionSource, Message.class);
	}

	public void CreateDB() throws SQLException {
		TableUtils.createTableIfNotExists(connectionSource, CS.class);
		TableUtils.createTableIfNotExists(connectionSource, MySettings.class);
		TableUtils.createTableIfNotExists(connectionSource, Message.class);
	}

	public void createDefault() throws SQLException {
		List<MySettings> settingss = SettingsDao.queryForAll();
		if (settingss.size() == 0) {
			MySettings settings = new MySettings();
			settings.setMyID(UUID.randomUUID().toString());
			long time = System.currentTimeMillis() / 100;
			settings.setName("DEFAULT_USER:" + time);
			settings.setNameTime(time/10);
			SettingsDao.createIfNotExists(settings);
		}
	}

	@Subscribe
	private void saveCS(MyEvent event) throws SQLException {
		TYPE type = event.getType();
		Object obj = event.getObject();
		if (type.equals(TYPE.Database_CS_SAVE)) {
			if (obj instanceof CS) {
				CS cs = (CS) obj;
				cs_save(cs);
			} else if (obj instanceof ArrayList<?>) {
				ArrayList<CS> css = new ArrayList<CS>();
				ArrayList<?> in = (ArrayList<?>) obj;
				for (Object object : in) {
					if (object instanceof CS) {
						CS cs = (CS) object;
						css.add(cs);
					}
				}
				cs_save_ArrayList(css);
			}
		}
	}

	private void cs_save(CS cs) throws SQLException {
		if (CSDao.idExists(cs.getID())) {
			CSDao.update(cs);
		} else {
			CSDao.create(cs);
		}
	}

	private void cs_save_ArrayList(ArrayList<CS> arrayList) throws SQLException {
		for (CS cs : arrayList) {
			cs_save(cs);
		}
		System.err.println("DataBase.cs_save_ArrayList():" + arrayList.size());
	}

	public void restoreInfo() throws SQLException {
		List<MySettings> list = SettingsDao.queryForAll();
		if (list.size() == 1) {
			MySettings settings = list.get(0);

			MyEvent event = new MyEvent();
			event.setType(TYPE.MySettings_show);
			event.setObject(settings);

			EventHEADER.getBus_mysettings().post(event);
		}
	}
}
