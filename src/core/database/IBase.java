package core.database;

import core.CoreProtocol;

/**
 * @author Henry on 31/12/16.
 */
public interface IBase extends CoreProtocol {

	String[] readUserData(int uid);
	String[] popMessages(int uid);
	IBase pushMessage(int uid, String msg);
	IBase addToOnline(int uid);
	IBase addContact(int uidFrom, int uidWho);
	IBase removeFromOnline(int uid);
	boolean checkPassword(int uid, int pass);
	int[] getContacts(int uid);
	int[] getStrangers(int uid);


	static IBase create(String type, String file) {
		if (type.equalsIgnoreCase("struct")) return new StructBaseTemp(file);
		return null;
	}
}
