package app.console.server;

import conf.Config;
import core.ServerCore;

/**
 * @author Henry on 01/01/17.
 */
public class ConsoleServer {

	private ServerCore serverCore;
	private Config config;

	public ConsoleServer(Config config) {
		this(config.dataBase.type, config.dataBase.url);
		setConfig(config);
	}
	public ConsoleServer(String dataBaseType, String dataBaseFile) {
		serverCore = new ServerCore().loadDataBase(dataBaseType, dataBaseFile);
	}
	public ConsoleServer setConfig(Config config) {
		this.config = config;
		serverCore.WELLCOME_MESSAGE = config.wellcomeMessage;
		return this;
	}
	public ConsoleServer startServer() {
		serverCore.startServer();
		return this;
	}
	public ConsoleServer stopServer() {
		serverCore.closeServer();
		return this;
	}
	public Config getConfig() {
		return config;
	}
}
