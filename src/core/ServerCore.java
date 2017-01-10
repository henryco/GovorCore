package core;

import core.connection.ServerConnector;
import core.database.IBase;
import core.utils.Utils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;


public class ServerCore {

	public String WELLCOME_MESSAGE = "";
	private ServerSocket serverSocket;
	private IBase dataBase;
	private int port;
	private volatile boolean status = false;

	public ServerCore(){
		this(80);
	}
	public ServerCore(int port) {
		this.port = port;
	}

	public ServerCore loadDataBase(String type, String name) {
		dataBase = IBase.create(type, name);
		return this;
	}

	public ServerCore startServer() {
		return startServer(null);
	}
	public ServerCore startServer(Consumer<String> optionalMessageAction) {

		serverSocket = Utils.createServerSocket(port);
		status = true;
		new Thread(() -> {
			while (status) {
				Socket handleSocket = Utils.acceptSocket(serverSocket);
				ServerConnector connection = new ServerConnector(handleSocket, dataBase, optionalMessageAction);
				connection.start();
			}
			Utils.closeServerSocket(serverSocket);
			Thread.currentThread().interrupt();
		}).start();
		return serverInfo();
	}

	public ServerCore closeServer(){
		status = false;
		return this;
	}

	private ServerCore serverInfo() {
		System.out.println("\n"+WELLCOME_MESSAGE+"\n<<SERVER EDITION>>");
		return this;
	}
}
