package com.github.kvac.phoenix.server.db;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.kvac.phoenix.libs.objects.ServerConfig;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.table.TableUtils;

import lombok.Getter;

public class Database {

	DataSourceConnectionSource connectionSource;
	@Getter
	Dao<CS, String> CsDao;

	//
	//

	public void init() throws JsonGenerationException, JsonMappingException, IOException {
		ServerConfig config = new ServerConfig();

		String serverDB_host;
		int serverDB_port;

		String serverDB_username;
		String serverDB_pass;
		String serverDB_DB;

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

		File configFile = new File("serverConfig.yml");
		if (!configFile.exists()) {
			serverDB_host = "example.com";
			serverDB_port = 5432;

			serverDB_username = "userName";
			serverDB_pass = "passwd";
			serverDB_DB = "name_db";

			config.setServerDB_host(serverDB_host);
			config.setServerDB_port(serverDB_port);
			config.setServerDB_username(serverDB_username);
			config.setServerDB_pass(serverDB_pass);
			config.setServerDB_DB(serverDB_DB);
			mapper.writeValue(configFile, config);
			System.err.println("change values in " + configFile.getAbsolutePath());
			System.exit(0);
		}

		ServerConfig configR = mapper.readValue(configFile, ServerConfig.class);

		System.out.println(ReflectionToStringBuilder.toString(configR, ToStringStyle.MULTI_LINE_STYLE));

		DataBaseHeader.setConfig(configR);

		StringBuilder configStringBuilder = new StringBuilder();
		configStringBuilder.append("jdbc:postgresql://");
		configStringBuilder.append(
				DataBaseHeader.getConfig().getServerDB_host() + "/" + DataBaseHeader.getConfig().getServerDB_DB());

		DataBaseHeader.dbPATH = configStringBuilder.toString();
		}

	public static DataSource createDataSource() {
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(DataBaseHeader.dbPATH,
				DataBaseHeader.getConfig().getServerDB_username(), DataBaseHeader.getConfig().getServerDB_pass());
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
		GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<PoolableConnection>(
				poolableConnectionFactory);
		poolableConnectionFactory.setPool(connectionPool);
		return new PoolingDataSource<PoolableConnection>(connectionPool);
	}

	public void connect() throws SQLException {
		connectionSource = new DataSourceConnectionSource(createDataSource(), DataBaseHeader.dbPATH);
		CsDao = DaoManager.createDao(connectionSource, CS.class);
		// photoeDao = DaoManager.createDao(connectionSource, Photoe.class);
		// photoPA = DaoManager.createDao(connectionSource, PrePhoto.class);
	}

	public void create() throws SQLException {
		TableUtils.createTableIfNotExists(connectionSource, CS.class);

	}
}
