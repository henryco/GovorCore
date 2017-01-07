package core.connection;

import core.connection.behavior.ConnectBehavior;
import core.connection.behavior.StdBehavior;
import core.utils.Utils;
import core.database.IBase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Henry on 01/01/17.
 */
public class ServerConnector extends Thread {

	public static boolean debug = false;

	private volatile boolean connection = true;
	private boolean logged = false;
	private Socket socket;
	private IBase dataBase;
	private Utils.UID uid;
	private ConnectBehavior behavior = new StdBehavior();

	public ServerConnector(Socket socket, IBase dataBase) {
		this.socket = socket;
		this.dataBase = dataBase;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (connection)
				behavior.process(dataBase, in, out, this);
			Thread.currentThread().interrupt();
			socket.close();
			if (uid != null) dataBase.removeFromOnline(uid.integer);
		} catch (Exception e) {e.printStackTrace();}
	}


	public ServerConnector forceClose() {
		connection = false;
		return this;
	}

	public boolean login(String[] args) {
		if (!logged)
			try {
				int uid = Integer.parseInt(args[0]);
				int pass = Integer.parseInt(args[1]);
				logged = dataBase.checkPassword(uid, pass);
				this.uid = new Utils.UID(uid);
				if (!logged) forceClose();
				else dataBase.addToOnline(uid);
			} catch (Exception exp) {
				forceClose();
			}
		return logged;
	}

	public ServerConnector setBehavior(ConnectBehavior behavior) {
		this.behavior = behavior;
		return this;
	}

	public Utils.UID getConnectedUID(){
		return uid;
	}
}
