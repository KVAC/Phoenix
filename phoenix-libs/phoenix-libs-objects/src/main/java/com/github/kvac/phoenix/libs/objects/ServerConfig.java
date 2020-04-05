/**
 * 
 */
package com.github.kvac.phoenix.libs.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jdcs_dev
 *
 */
public class ServerConfig {

	@Getter
	@Setter
	private String serverDB_host;
	@Getter
	@Setter
	private int serverDB_port;

	@Getter
	@Setter
	private String serverDB_username;
	@Getter
	@Setter
	private String serverDB_pass;

	@Getter
	@Setter
	private String serverDB_DB;
}
