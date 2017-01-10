package app.console.server;

import conf.Config;
import core.ServerCore;

import java.util.function.Consumer;

/**
 * @author Henry on 01/01/17.
 */
public class ConsoleServer {

	private ServerCore serverCore;
	private Config config;

	public ConsoleServer(Config config) {
		this(config.dataBase.type, config.dataBase.url, config.serverPort);
		setConfig(config);
	}
	public ConsoleServer(String dataBaseType, String dataBaseFile, int port) {
		serverCore = new ServerCore(port).loadDataBase(dataBaseType, dataBaseFile);

	}
	public ConsoleServer setConfig(Config config) {
		this.config = config;
		serverCore.WELLCOME_MESSAGE = config.wellcomeMessage;
		return this;
	}
	public ConsoleServer startServer(Consumer<String> action) {
		serverCore.startServer(action);
		return this;
	}
	public ConsoleServer startServer() {
		return startServer(System.out::println);
	}
	public ConsoleServer stopServer() {
		serverCore.closeServer();
		return this;
	}
	public Config getConfig() {
		return config;
	}
}
