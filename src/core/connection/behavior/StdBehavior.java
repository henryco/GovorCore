package core.connection.behavior;

import core.connection.BaseClient;
import core.connection.ServerConnector;
import core.database.IBase;
import core.utils.Utils;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * @author Henry on 01/01/17.
 */
public class StdBehavior implements ConnectBehavior {

	/*
		INTERFACE:
		SEND: (SEND_REG)×(TO_UID)×(MESSAGE)
		UPD: (UPD_REG)
		STAT: (STAT_REG)
		INFO: (INFO_REG)×(TARGET_UID)
		CLOSE: (CLOSE_REG)
	*/


	@Override
	public void process(IBase dataBase, Object... args) throws Exception {
		BufferedReader in = (BufferedReader) args[0];
		PrintWriter out = (PrintWriter) args[1];
		ServerConnector connector = (ServerConnector) args[2];
		Utils.UID uid = connector.getConnectedUID();
		boolean debug = ServerConnector.debug;


		String line = in.readLine();
		if (line != null) {
			if (debug) System.out.println(": "+line);
			String[] dataIn = line.split(BaseClient.SPLIT_REG);

			if (connector.login(dataIn)) {
				String flag = dataIn[0];



				if (flag.equalsIgnoreCase(BaseClient.SEND_REG)) {
					int sendUID = Integer.parseInt(dataIn[1]);
					String pushMsg = uid + BaseClient.SPLIT_REG + dataIn[2];
					dataBase.pushMessage(sendUID, pushMsg);
					if (debug) System.out.println("SENDING:" + dataIn[1] + " : " + pushMsg);
				}



				else if (flag.equalsIgnoreCase(BaseClient.UPD_REG)) {
					for (String s : dataBase.popMessages(uid.integer)) {
						out.println(s);
						if (debug) System.out.println(s);
					}
					out.println(BaseClient.STOP_REG);
					out.flush();
				}



				else if (flag.equalsIgnoreCase(BaseClient.STAT_REG)) {
					String friend = BaseClient.SPLIT_REG + BaseClient.FRIEND;
					String strang = BaseClient.SPLIT_REG + BaseClient.STRANGER;
					for (int u : dataBase.getContacts(uid.integer)) {
						String msg = infoUser(dataBase.readUserData(u), u);
						out.println(msg + friend);
						if (debug) System.out.println(msg + friend);
					}
					for (int u :dataBase.getStrangers(uid.integer)) {
						String msg = infoUser(dataBase.readUserData(u), u);
						out.println(msg + strang);
						if (debug) System.out.println(msg + strang);
					}
					out.println(BaseClient.STOP_REG);
					out.flush();
				}



				else if (flag.equalsIgnoreCase(BaseClient.INFO_REG)) {
					int target = Integer.parseInt(dataIn[1]);
					String[] info = dataBase.readUserData(target);
					String msg = dataIn[1];
					for (String s : info) msg += (BaseClient.SPLIT_REG + s);
					out.println(msg);
					if (debug) System.out.println(msg);
					out.flush();
				}



				else if (flag.equalsIgnoreCase(BaseClient.CLOSE_REG)) {
					connector.forceClose();
				}



			} else ConnectBehavior.outPrintln(BaseClient.ABORT_REG, out).flush();
		}
	}


	private static String infoUser(String[] inf, int uid) {
		String msg = Integer.toString(uid);
		for (String s : inf) msg += (BaseClient.SPLIT_REG + s);
		return msg;
	}

}
