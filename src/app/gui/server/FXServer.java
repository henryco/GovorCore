package app.gui.server;

import app.console.server.ConsoleServer;
import conf.Config;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Henry on 10/01/17.
 */
public class FXServer {

	private static final String SERVER_FORM = "forms/serverForm.fxml";

	public FXServer(Stage stage, Config config) throws Exception {
		stage.setTitle(config.guiTitle);
		stage.setResizable(false);
		stage.setOnCloseRequest(event -> System.exit(0));
		FXMLLoader loader = new FXMLLoader(FXServer.class.getResource(SERVER_FORM));
		Parent root = loader.load();
		FXServerController controller = loader.getController();
		stage.setScene(new Scene(root));
		stage.show();

		new ConsoleServer(config).startServer(controller::appendText);
	}
}
