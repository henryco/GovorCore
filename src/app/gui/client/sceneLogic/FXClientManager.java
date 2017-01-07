package app.gui.client.sceneLogic;

import app.gui.client.FXClientSceneFactory;
import app.gui.client.controllers.FXClientController;
import core.connection.BaseClient;
import javafx.stage.Stage;

/**
 * @author Henry on 04/01/17.
 */
public class FXClientManager implements FXClientBehavior {

	private Stage stage;
	private CONTROLLER CONTROLLER;
	private final class CONTROLLER {

		private final FXClientController login;
		private final FXClientController main;

		private CONTROLLER(BaseClient client, FXClientBehavior clientBehavior) throws Exception {
			login = FXClientSceneFactory.createScene(client, clientBehavior, FXClientSceneFactory.LOGIN_SCENE);
			main = FXClientSceneFactory.createScene(client, clientBehavior, FXClientSceneFactory.MAIN_SCENE);
		}
	}
	private FXClientController actualScene;

	public FXClientManager(Stage stage, BaseClient client, String title) throws Exception {
		this.CONTROLLER = new CONTROLLER(client, this);
		this.stage = stage;
		this.stage.setTitle(title);
		this.stage.setResizable(false);
		stage.setOnCloseRequest(event -> {
			client.close();
			System.exit(0);
		});
		this.setLoginScene();
	}

	@Override
	public FXClientBehavior setLoginScene() {
		return setScene(CONTROLLER.login);
	}

	@Override
	public FXClientBehavior setMainScene() {
		return setScene(CONTROLLER.main);
	}

	private void closeScene() {
		if (actualScene != null) actualScene.close();
	}

	private FXClientBehavior setScene(FXClientController scene) {
		closeScene();
		actualScene = scene;
		stage.setScene(scene.action().getScene());
		stage.show();
		return this;
	}

	public Stage getStage() {
		return stage;
	}

}
