package core.database;

import core.utils.Utils;
import net.henryco.struct.container.tree.StructNode;
import net.henryco.struct.container.tree.StructTree;

import java.util.*;

/**
 * @author Henry on 31/12/16.
 */
public class StructBaseTemp implements IBase {

	private static final String[] nickNames = new String[]{"name", "user", "nick", "nickname", "nickName", "0"};
	private static final String[] baseNames = new String[]{"data", "base"};
	private static final String[] passNames = new String[]{"password", "pass", "1", "key"};
	private static final String[] contactsNames = new String[]{"contacts", "friends", "2"};
	private static final String[] strangerNames = new String[]{"strangers", "others", "3"};

	private StructNode dataBaseNode;
	private HashMap<Integer, List<String>> queMap;
	private HashMap<Integer, String> statMap;

	public StructBaseTemp(String baseName) {
		dataBaseNode = new StructTree(baseName).mainNode.getStructSafe(baseNames);
		queMap = new HashMap<>();
		statMap = new HashMap<>();
		StructNode[] uidNode = dataBaseNode.getStructArray();
		for (StructNode s : uidNode) {
			queMap.put(Integer.parseInt(s.name), new LinkedList<>());
			statMap.put(Integer.parseInt(s.name), OFFLINE);
		}
		new Thread(() -> System.out.println(dataBaseNode)).start();
	}

	@Override
	public String[] readUserData(int uid) {
		String userName = dataBaseNode.getStructSafe(Integer.toString(uid)).getString("unknown", nickNames);
		return new String[]{userName, statMap.get(uid)};
	}

	@Override
	public String[] popMessages(int uid) {

		List<String> stringQueue = queMap.get(uid);
		String[] msg = new String[stringQueue.size()];
		for (int i = 0; i < msg.length; i++) msg[i] = stringQueue.get(i);
		stringQueue.clear();
		return msg;
	}

	@Override
	public IBase pushMessage(int uid, String msg) {
		queMap.get(uid).add(msg);
		return this;
	}

	@Override
	public boolean checkPassword(int uid, int pass) {
		return pass == dataBaseNode.getStructSafe(Integer.toString(uid)).getInt(-1, passNames);
	}

	@Override
	public IBase addToOnline(int uid) {
		statMap.put(uid, ONLINE);
		return this;
	}

	@Override
	public IBase removeFromOnline(int uid) {
		try {
			statMap.put(uid, OFFLINE);
		} catch (Exception ignored){}
		return this;
	}

	@Override
	public int[] getContacts(int uid) {
		String[] prim = dataBaseNode.getStructSafe(Integer.toString(uid)).getStructSafe(contactsNames).getPrimitiveArray();
		int[] uids = new int[prim.length];
		for (int i = 0; i < prim.length; i++) uids[i] = Integer.parseInt(prim[i]);
		return uids;
	}

	@Override
	public int[] getStrangers(int uid) {
		StructNode strange = dataBaseNode.getStructSafe(Integer.toString(uid)).getStructSafe(strangerNames);
		if (strange == null) return new int[0];
		String[] prim = strange.getPrimitiveArray();
		int[] uids = new int[prim.length];
		for (int i = 0; i < prim.length; i++) uids[i] = Integer.parseInt(prim[i]);
		return uids;
	}

	@Override
	public IBase addContact(int uidFrom, int uidWho) {

		StructNode owner = dataBaseNode.getStructSafe(uidFrom);
		StructNode who = dataBaseNode.getStructSafe(uidWho);
		if (owner != null && who != null) {
			StructNode whoStrangers = who.getStructSafe(strangerNames);
			if (whoStrangers != null && Utils.arrContainsInt(whoStrangers.getPrimitiveArray(), uidFrom)) {
				StructNode whoCont = who.getStructSafe(contactsNames);
				if (whoCont == null) {
					who.addStructure(new StructNode(who, contactsNames[0], who.dirName));
					whoCont = who.getStructSafe(contactsNames);
				}
				whoCont.addPrimitive(Integer.toString(whoCont.getPrimitiveArray().length), Integer.toString(uidFrom));
				whoStrangers.removePrimitive(false, Integer.toString(uidFrom));

				StructNode owCont = owner.getStructSafe(contactsNames);
				if (owCont == null) {
					owner.addStructure(new StructNode(owner, contactsNames[0], owner.dirName));
					owCont = who.getStructSafe(contactsNames);
				}
				owCont.addPrimitive(Integer.toString(owCont.getPrimitiveArray().length), Integer.toString(uidWho));
			} else {
				StructNode strangeNode;
				if ((strangeNode = owner.getStructSafe(strangerNames)) != null)
					strangeNode.addPrimitive(Integer.toString(strangeNode.getPrimitiveArray().length), Integer.toString(uidWho));
				else {
					owner.addStructure(new StructNode(owner, strangerNames[0], owner.dirName));
					owner.getStructSafe(strangerNames).addPrimitive("0", Integer.toString(uidWho));
				}
			}
		}
		return this;
	}
}
