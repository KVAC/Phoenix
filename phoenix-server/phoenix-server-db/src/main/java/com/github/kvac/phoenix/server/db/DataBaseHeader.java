package com.github.kvac.phoenix.server.db;

import com.github.kvac.phoenix.libs.objects.ServerConfig;

import lombok.Getter;
import lombok.Setter;

public class DataBaseHeader {
	@Getter
	private static Database dataBase = new Database();

	@Getter
	@Setter
	private static ServerConfig config = new ServerConfig();

	public static String dbPATH = "";

}
