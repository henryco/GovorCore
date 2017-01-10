package app.gui.server;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * @author Henry on 10/01/17.
 */
public class FXServerController {

	@FXML private TextArea outPane;
	@FXML private Button okButton;
	@FXML private TextField consoleArea; //	TEMPORARY DISABLED

	@FXML void initialize() {
		okButton.setOnAction(event -> {/* TODO */});
	}

	public synchronized FXServerController appendText(String text) {
		outPane.appendText(text + "\n");
		return this;
	}

}
