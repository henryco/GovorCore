package core.database;

import core.CoreInterface;


public interface IBase extends CoreInterface {

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
