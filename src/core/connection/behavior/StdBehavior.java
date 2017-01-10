package core.connection.behavior;

import core.connection.ServerConnector;
import core.database.IBase;
import core.utils.Utils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Arrays;


public class StdBehavior implements ConnectBehavior {

	/*
		INTERFACE:
		SEND:	(SEND_REG)×(TO_UID)×(MESSAGE)
		ADD:	(ADD_REG)×(FROM_UID)×(WHO_UID)
		UPD:	(UPD_REG)
		STAT:	(STAT_REG)
		INFO:	(INFO_REG)×(TARGET_UID)
		CLOSE:	(CLOSE_REG)
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
			String[] dataIn = line.split(SPLIT_REG);

			if (debug) System.out.println(Arrays.toString(dataIn));

			if (connector.login(dataIn) && dataIn.length > 0) {
				String flag = dataIn[0];



				if (flag.equalsIgnoreCase(SEND_REG)) {
					int sendUID = Integer.parseInt(dataIn[1]);
					String pushMsg = uid + SPLIT_REG + dataIn[2];
					dataBase.pushMessage(sendUID, pushMsg);
				}



				else if (flag.equalsIgnoreCase(UPD_REG)) {
					for (String s : dataBase.popMessages(uid.integer)) {
						out.println(s);
						if (debug) System.out.println(s);
					}
					out.println(STOP_REG);
					out.flush();
				}



				else if (flag.equalsIgnoreCase(STAT_REG)) {
					String friend = SPLIT_REG + FRIEND;
					String strang = SPLIT_REG + STRANGER;
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
					out.println(STOP_REG);
					out.flush();
				}



				else if (flag.equalsIgnoreCase(INFO_REG)) {
					int target = Integer.parseInt(dataIn[1]);
					String[] info = dataBase.readUserData(target);
					String msg = dataIn[1];
					for (String s : info) msg += (SPLIT_REG + s);
					out.println(msg);
					if (debug) System.out.println(msg);
					out.flush();
				}


				else if (flag.equalsIgnoreCase(ADD_CONTACT)) {
					int uidFrom = Integer.parseInt(dataIn[1]);
					int uidWho = Integer.parseInt(dataIn[2]);
					dataBase.addContact(uidFrom, uidWho);
				}


				else if (flag.equalsIgnoreCase(CLOSE_REG)) {
					connector.forceClose();
				}


			} else ConnectBehavior.outPrintln(ABORT_REG, out).flush();
		}
	}


	private static String infoUser(String[] inf, int uid) {
		String msg = Integer.toString(uid);
		for (String s : inf) msg += (SPLIT_REG + s);
		return msg;
	}

}
