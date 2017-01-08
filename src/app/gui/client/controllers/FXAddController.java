package app.gui.client.controllers;

import core.utils.Utils;
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
				String sUid = addUidField.getText();
				if (Utils.checkNumb(sUid) && sUid.length() == 9) {
					client.addUser(sUid);
					aborter.abort();
				}
			}
		});
		return this;
	}
}
