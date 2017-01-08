package app.gui.client.sceneLogic;

import app.gui.client.FXClientSceneFactory;
import app.gui.client.controllers.FXClientController;
import core.connection.BaseClient;
import javafx.stage.Stage;

/**
 * @author Henry on 04/01/17.
 */
public class FXClientManager implements FXClientBehavior {

	private final class CONTROLLER {
		private final FXClientController login;
		private final FXClientController main;

		private CONTROLLER(BaseClient client, FXClientBehavior clientBehavior) throws Exception {
			login = FXClientSceneFactory.createScene(client, clientBehavior, FXClientSceneFactory.LOGIN_SCENE);
			main = FXClientSceneFactory.createScene(client, clientBehavior, FXClientSceneFactory.MAIN_SCENE);
		}
	}

	private Stage stage;
	private CONTROLLER controller;
	private BaseClient client;
	private FXClientController actualScene;



	public FXClientManager(Stage stage, BaseClient client, String title) throws Exception {
		this.controller = new CONTROLLER(client, this);
		this.stage = stage;
		this.client = client;
		this.stage.setTitle(title);
		this.stage.setResizable(false);
		stage.setOnCloseRequest(event -> {
			client.close();
			System.exit(0);
		});
	}


	@Override
	public FXClientBehavior setLoginScene() {
		return setScene(controller.login);
	}



	@Override
	public FXClientBehavior setMainScene() {
		return setScene(controller.main);
	}



	@Override
	public FXClientBehavior createAddUidDialog() {
		try {
			Stage dialog = new Stage();
			dialog.setScene(FXClientSceneFactory.createScene(client, this, FXClientSceneFactory.ADD_UID).action()
					.setOnAbort(abortMessage -> {
						dialog.close();
						try {
							controller = new CONTROLLER(client, this);
							setMainScene();
						} catch (Exception e) {
							System.err.println("GUI RELOAD RUNTIME FATAL ERROR");
							client.close();
							System.exit(0);
						}
					}).getScene());
			dialog.setResizable(false);
			dialog.show();
		} catch (Exception ignore) {}
		return this;
	}



	private void closeScene() {
		if (actualScene != null) actualScene.close();
	}



	private FXClientBehavior setScene(FXClientController scene) {
		closeScene();
		actualScene = scene;
		stage.setScene(scene.action().getScene());
		stage.setResizable(false);
		stage.show();
		return this;
	}



	public Stage getStage() {
		return stage;
	}

}
