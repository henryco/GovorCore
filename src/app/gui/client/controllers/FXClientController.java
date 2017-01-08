package app.gui.client.controllers;

import app.gui.client.sceneLogic.FXClientAborter;
import app.gui.client.sceneLogic.FXClientBehavior;
import core.connection.BaseClient;
import javafx.scene.Scene;

import java.util.Arrays;

/**
 * @author Henry on 04/01/17.
 */
public abstract class FXClientController {

	protected Scene scene;
	protected BaseClient client;
	protected FXClientBehavior behavior;
	protected FXClientAborter aborter = abortMessage -> {
		System.err.println(Arrays.toString(abortMessage));
		System.exit(0);
	};

	public FXClientController setClient(BaseClient client) {
		this.client = client;
		return this;
	}
	public FXClientController setBehavior(FXClientBehavior behavior) {
		this.behavior = behavior;
		return this;
	}
	public FXClientController setScene(Scene scene) {
		this.scene = scene;
		return this;
	}
	public FXClientController setOnAbort(FXClientAborter aborter) {
		this.aborter = aborter;
		return this;
	}
	public abstract FXClientController action();
	public FXClientController close() {
		return this;
	}
	public Scene getScene() {
		return scene;
	}
}
