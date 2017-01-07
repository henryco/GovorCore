package core.database;

import core.CoreProto;

/**
 * @author Henry on 31/12/16.
 */
public interface IBase extends CoreProto {

	String[] readUserData(int uid);
	String[] popMessages(int uid);
	IBase pushMessage(int uid, String msg);
	IBase addToOnline(int uid);
	IBase removeFromOnline(int uid);
	boolean checkPassword(int uid, int pass);
	int[] getContacts(int uid);
	int[] getStrangers(int uid);


	static IBase create(String type, String file) {
		if (type.equalsIgnoreCase("struct")) return new StructBaseTemp(file);
		return null;
	}
}
