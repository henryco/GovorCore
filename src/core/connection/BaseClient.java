package core.connection;


import core.CoreProtocol;

import java.util.List;

/**
 * @author Henry on 01/01/17.
 */
public interface BaseClient extends CoreProtocol {


	boolean isConnected();

	BaseClient sendMessage(String uid, String message);
	BaseClient close();
	BaseClient setServer(String ip4, String port);
	BaseClient setUID(String uid);
	BaseClient connect(String password);
	BaseClient setConnected(boolean connected);
	BaseClient addUser(String uid);

	List<String[]> update();
	List<String[]> getStatus();

	String[] getInfo(String uid);
	String getUID();
}
