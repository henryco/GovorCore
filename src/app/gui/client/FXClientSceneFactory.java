package app.gui.client;

import app.gui.client.controllers.FXClientController;
import app.gui.client.sceneLogic.FXClientBehavior;
import core.connection.BaseClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * @author Henry on 02/01/17.
 */
public class FXClientSceneFactory {

	public static final String LOGIN_SCENE = "forms/loginForm.fxml";
	public static final String MAIN_SCENE = "forms/mainForm.fxml";
	public static final String ADD_UID = "forms/addForm.fxml";

	public static FXClientController createScene(BaseClient client, FXClientBehavior clientBehavior, String url) throws Exception {
		FXMLLoader loader = new FXMLLoader(FXClientSceneFactory.class.getResource(url));
		Parent root = loader.load();
		return ((FXClientController)loader.getController()).setClient(client).setBehavior(clientBehavior).setScene(new Scene(root));
	}


}
