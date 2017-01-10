import app.gui.server.FXServer;
import conf.Config;
import app.console.server.ConsoleServer;
import app.gui.client.FXClientManager;
import core.ClientCore;
import core.connection.ServerConnector;
import javafx.application.Application;
import javafx.stage.Stage;
import net.henryco.struct.Struct;
import net.henryco.struct.container.tree.StructTree;



public class Main extends Application {

	private static final String defName = "config.struct";
	private static final String [] defPath = new String[]{"Enter", "Config"};
	private static String[] args = null;

	/*	START ARGS:
	 * 		[loc_path] [mode] ([ip] [port]) || ([db_type] [db_path])
	 *		[loc_path] [mode]
	 *		[loc_path]
	 *		[*void*]
	 */

	@Override
	public void start(Stage primaryStage) throws Exception {

		String prefix = "";
		if (args != null && args.length > 0 && !args[0].equalsIgnoreCase("-")) {
			prefix = args[0];
			Struct.setLIB_PATH_PREFIX(prefix);
			Config.localPathPrefix = args[0];
		}
		Config config = new StructTree(prefix + defName).mainNode.getPath(defPath).instanceAndInvokeObject(null, true, true);
		config.inLineInit(args);
		ServerConnector.debug = config.serverDebug;

		if (config.isClientMode())
			new FXClientManager(primaryStage, new ClientCore(config.server), config.guiTitle).setLoginScene();
		else if (config.isServerConsMode()) new ConsoleServer(config).startServer();
		else if (config.isServerGuiMode()) new FXServer(primaryStage, config);
	}

	public static void main(String ... args) {
		launch(setArgs(args));
	}

	private static String[] setArgs(String[] args) {
		Main.args = args;
		return args;
	}
}
