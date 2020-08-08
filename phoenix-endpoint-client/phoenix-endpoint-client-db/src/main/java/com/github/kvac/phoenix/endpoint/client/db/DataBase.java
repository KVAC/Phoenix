package com.github.kvac.phoenix.endpoint.client.db;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.MySettings;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.cs.S;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.github.kvac.phoenix.libs.objects.events.MyEvent.TYPE;
import com.google.common.eventbus.Subscribe;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.slf4j.LoggerFactory;

/**
 * @author jdcs_dev
 *
 */
public class DataBase {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(DataBase.class);

    public DataBase() {
        EventHEADER.getBus_mysettings().register(this);
        EventHEADER.getBus_cs_save().register(this);
    }

    JdbcPooledConnectionSource connectionSource;

    @Getter
    private Dao<CS, String> csDao;
    @Getter
    private Dao<MySettings, String> settingsDao;
    @Getter
    private Dao<Message, String> messageDao;
    @Getter
    private Dao<S, String> serverDao;

    public void connect() throws SQLException {
        connectionSource = new JdbcPooledConnectionSource(DataBaseHeader.DB_NAME_STRING);
        this.csDao = DaoManager.createDao(connectionSource, CS.class);
        this.settingsDao = DaoManager.createDao(connectionSource, MySettings.class);
        this.messageDao = DaoManager.createDao(connectionSource, Message.class);
        this.serverDao = DaoManager.createDao(connectionSource, S.class);
    }

    public void createDB() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, CS.class);
        TableUtils.createTableIfNotExists(connectionSource, MySettings.class);
        TableUtils.createTableIfNotExists(connectionSource, Message.class);
        TableUtils.createTableIfNotExists(connectionSource, S.class);

    }

    public void createDefault() throws SQLException {
        List<MySettings> settingss = settingsDao.queryForAll();
        if (settingss.isEmpty()) {
            MySettings settings = new MySettings();
            settings.setMyID(UUID.randomUUID().toString());
            long time = System.currentTimeMillis() / 100;
            settings.setName("DEFAULT_USER:" + time);
            settings.setNameTime(time / 10);
            settingsDao.createIfNotExists(settings);
        }
    }

    public void restoreInfo() throws SQLException {
        List<MySettings> list = settingsDao.queryForAll();
        if (list.size() == 1) {
            MySettings settings = list.get(0);

            MyEvent event = new MyEvent();
            event.setType(TYPE.MYSETTINGS_SHOW);
            event.setObject(settings);

            EventHEADER.getBus_mysettings().post(event);
        }
    }

    @Subscribe
    private void saveCS(MyEvent event) throws SQLException {
        TYPE type = event.getType();
        Object obj = event.getObject();
        if (type.equals(TYPE.DATABASE_CS_SAVE)) {
            if (obj instanceof CS) {
                CS cs = (CS) obj;
                cs_save(cs);
            } else if (obj instanceof ArrayList<?>) {
                ArrayList<CS> css = new ArrayList<>();
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
        if (csDao.idExists(cs.getId())) {
            csDao.update(cs);
        } else {
            csDao.create(cs);
        }
    }

    private void cs_save_ArrayList(ArrayList<CS> arrayList) throws SQLException {
        for (CS cs : arrayList) {
            cs_save(cs);
        }
        System.err.println("DataBase.cs_save_ArrayList():" + arrayList.size());
    }

    public void restoreServers() {
        if (serverDao != null) {
            for (S s : serverDao) {
                EventHEADER.getSERVERS_EVENT_BUS().post(s);
            }
        }
    }

    public void saveServer(S server) throws SQLException {
        if (!(this.serverDao.idExists(server.getId()))) {
            this.serverDao.create(server);
        } else {

        }
    }

    void messageSave(Message message) throws SQLException {
        if (messageDao.idExists(message.getMessageID())) {
            //TODO ПРОВЕРКА СТАТУСА
        } else {
            messageDao.create(message);
            logger.info(message.getMessageID() + " is saved");
        }
    }
}
