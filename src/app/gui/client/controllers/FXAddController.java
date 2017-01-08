package app.gui.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * @author Henry on 08/01/17.
 */
public class FXAddController extends FXClientController {

	@FXML private Button okButton;
	@FXML private TextField addUidField;

	@Override
	public FXClientController action() {
		okButton.setOnAction(event -> {
			boolean runFlag = true;
			String uid = addUidField.getText();
			for (String[] s : client.getStatus()) {
				if (s[0].equalsIgnoreCase(uid)
						|| uid.equalsIgnoreCase(client.getUID())
						|| s[0].length() > 9 || s[0].length() == 0) {
					runFlag = false;
					break;
				}
			}
			if (runFlag) {
				client.addUser(addUidField.getText());
				aborter.abort();
			}
		});
		return this;
	}
}
