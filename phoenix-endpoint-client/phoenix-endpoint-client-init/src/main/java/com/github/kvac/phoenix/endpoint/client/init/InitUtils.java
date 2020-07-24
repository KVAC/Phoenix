package com.github.kvac.phoenix.endpoint.client.init;

import com.github.kvac.phoenix.endpoint.client.db.DataBaseHeader;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitUtils {

    protected static final Logger logger = LoggerFactory.getLogger(InitUtils.class);

    public static boolean checkSettingsAv() throws SQLException {
        String msg = "getSettingsDao:" + DataBaseHeader.getDataBase().getSettingsDao().countOf()
                + ":getServerDao:" + DataBaseHeader.getDataBase().getServerDao().countOf();
        logger.info(msg);

        if (DataBaseHeader.getDataBase().getSettingsDao().countOf() == 0) {
            return false;
        }
        return DataBaseHeader.getDataBase().getServerDao().countOf() != 0;
    }
}
