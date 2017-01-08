package app.gui.client.controllers;

import core.connection.BaseClient;
import core.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Henry on 04/01/17.
 */
public class FXLoginController extends FXClientController {

	@FXML private PasswordField passField;
	@FXML private Button loginButton;
	@FXML private TextField textField;
	@FXML private ImageView animRain;
	@FXML private ImageView animCat;


	@Override
	public FXClientController action() {

		new ImageView[]{animRain, animCat}
				[new int[]{0,1,1,0,0,1,1,0,1,0,1,0}
				[new Random(System.currentTimeMillis()).nextInt(2)]]
				.setVisible(true);

		loginButton.setOnAction(event -> {

			String pass = passField.getText();
			String uid = textField.getText();

			boolean isNumb = Utils.checkNumb(uid);

			if (uid.length() == 9 && isNumb) {
				String[] connector = client.setUID(uid).connect(pass).getInfo(uid);
				try {
					System.out.println(Arrays.toString(connector));
				} catch (Exception ex) {
					aborter.abort("SERVER NOT FOUND");
				}

				if (!connector[0].equalsIgnoreCase(BaseClient.ABORT_REG)) {
					behavior.setMainScene();
				} else aborter.abort("SERVER NOT FOUND");
			}


		});

		return this;
	}
}
