package conf;

import core.utils.Utils;

/**
 * @author Henry on 01/01/17.
 */
public class Config {

	public static String localPathPrefix = "";

	public Utils.SERVER_INFO server;
	public static final class DataBase {
		public final String type;
		public final String url;
		public DataBase(String t, String u) {
			type = t;
			url = u;
		}
	}
	public DataBase dataBase;
	public boolean serverDebug = false;
	public String wellcomeMessage = "";
	public String mode = "client"; // "server"
	public String guiTitle = "";
	public int serverPort = 80;


	public Config(String mode, String text) {
		this.mode = mode;
		if (isClientMode()) this.guiTitle = text;
		else this.wellcomeMessage = text;
	}


	public void setIP(String ip4, String port) {
		server = new Utils.SERVER_INFO(ip4, port);
	}
	public void setDataBase(String t, String u) {
		this.dataBase = new DataBase(t, localPathPrefix + u);
	}
	public boolean isClientMode() {
		return mode.equalsIgnoreCase("client");
	}
	public void setServerPort(int port) {
		this.serverPort = port;
	}

	/**
	 * <b>START ARGS:</b> <br><br>
	 * &emsp &emsp[loc_path] [mode] ([ip] [port]) || ([db_type] [db_path]) <br>
	 * &emsp &emsp[loc_path] [mode] <br>
	 * &emsp &emsp[loc_path] <br>
	 * &emsp &emsp[*void*]
	 */
	public Config inLineInit(String[] args) {

		if (args != null && args.length != 0) {
			String[] arg = new String[args.length - 1];
			System.arraycopy(args, 1, arg, 0, arg.length);
			if (arg.length >= 1) mode = arg[0];
			if (arg.length >= 3) {
				if (isClientMode()) setIP(arg[1], arg[2]);
				else setDataBase(arg[1], arg[2]);
			}
		}
		return this;
	}
}

