package core;

import core.connection.BaseClient;
import core.utils.Utils;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;


public class ClientCore implements BaseClient {

	private boolean connected = false;
	private Utils.SERVER_INFO server;
	private Utils.UID uid;
	private Socket socket;

	public ClientCore(Utils.SERVER_INFO server_info, Utils.UID uid) {
		this(server_info);
		setUID(uid.string);
	}
	public ClientCore(Utils.SERVER_INFO server_info) {
		if (server_info != null)
			setServer(server_info.ip4, Integer.toString(server_info.port));
	}
	public ClientCore() {
	}


	@Override public synchronized BaseClient setServer(String ip4, String port) {
		this.server = new Utils.SERVER_INFO(ip4, port);
		System.out.println(server.ip4 + " :: " + server.port);
		return this;
	}

	@Override public synchronized ClientCore connect(String password) {
		if (!connected) {
			socket = server.createSocket();
			PrintWriter pw = Utils.writeToStream(uid + SPLIT_REG + password, socket);
			Utils.closeNFlushStream(pw);
			return setConnected(true);
		}	return this;
	}


	@Override public synchronized ClientCore setConnected(boolean connected) {
		this.connected = connected;
		return this;
	}



	@Override public synchronized boolean isConnected() {
		return connected;
	}



	@Override public synchronized ClientCore sendMessage(String uid, String message) {
		Utils.closeNFlushStream(Utils.writeToStream(SEND_REG + SPLIT_REG + uid + SPLIT_REG + message, socket));
		return this;
	}



	@Override public synchronized ClientCore close() {
		if (connected) {
			Utils.closeNFlushStream(Utils.writeToStream(CLOSE_REG, socket));
			connected = false;
		}	return this;
	}




	@Override public synchronized List<String[]> update() {
		return Utils.getDataList(socket, UPD_REG, STOP_REG, SPLIT_REG);
	}


	@Override public synchronized String[] getInfo(String uid) {
		Utils.closeNFlushStream(Utils.writeToStream(INFO_REG + SPLIT_REG + uid, socket));
		return Utils.readLineFromStream(socket).split(SPLIT_REG);
	}


	@Override public synchronized String getUID() {
		return uid.string;
	}



	@Override public synchronized List<String[]> getStatus() {
		return Utils.getDataList(socket, STAT_REG, STOP_REG, SPLIT_REG);
	}


	@Override public synchronized BaseClient setUID(String uid) {
		this.uid = new Utils.UID(uid);
		return this;
	}


	@Override public synchronized BaseClient addUser(String uid) {
		Utils.closeNFlushStream(Utils.writeToStream(ADD_CONTACT + SPLIT_REG
				+ this.uid.string + SPLIT_REG + uid, socket));
		return this;
	}
}
