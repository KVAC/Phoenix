package com.github.kvac.phoenix.server.network;

import com.github.kvac.phoenix.server.network.handler.NetWorkHandler;
import com.github.kvac.phoenix.server.network.server.Server;

import lombok.Getter;
import lombok.Setter;

public class NetWorkHeader {

	@Getter
	@Setter
	private static NetWorkHandler netWorkHandler = new NetWorkHandler();
	
	@Getter
	@Setter
	private static Server server=new Server();
}
