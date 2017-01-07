package app.gui.client.controllers;

import core.connection.BaseClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Henry on 04/01/17.
 */
public class FXLoginController extends FXClientController {

	@FXML private PasswordField passField;
	@FXML private Pane imgPane;
	@FXML private Button loginButton;
	@FXML private TextField textField;
	@FXML private ImageView animRain;
	@FXML private ImageView animCat;

	@FXML void initialize() {
		assert passField != null : "fx:id=\"passField\" was not injected: check your FXML file 'LOGIN_SCENE.fxml'.";
		assert imgPane != null : "fx:id=\"imgPane\" was not injected: check your FXML file 'LOGIN_SCENE.fxml'.";
		assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'LOGIN_SCENE.fxml'.";
		assert textField != null : "fx:id=\"textField\" was not injected: check your FXML file 'LOGIN_SCENE.fxml'.";
		assert animCat != null : "fx:id=\"animCat\" was not injected: check your FXML file 'LOGIN_SCENE.fxml'.";
		assert animRain != null : "fx:id=\"animRain\" was not injected: check your FXML file 'LOGIN_SCENE.fxml'.";


		new ImageView[]{animRain, animCat}
				[new int[]{0,1,1,0,0,1,1,0,1,0,1,0}
				[new Random(System.currentTimeMillis()).nextInt(2)]]
				.setVisible(true);

		loginButton.setOnAction(event -> {

			String pass = passField.getText();
			String uid = textField.getText();

			String[] connector = client.setUID(uid).connect(pass).getInfo(uid);
			try {
				System.out.println(Arrays.toString(connector));
			} catch (Exception ex) {
				aborter.abort();
			}

			if (!connector[0].equalsIgnoreCase(BaseClient.ABORT_REG)) {
				behavior.setMainScene();
			} else aborter.abort();
		});
	}


}
