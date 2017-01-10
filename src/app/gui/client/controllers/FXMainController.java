package app.gui.client.controllers;

import core.connection.BaseClient;
import core.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Henry on 04/01/17.
 */
public class FXMainController extends FXClientController {

	private static final String spaceSep = " \t ";
	private static final String statusSymb = "◉";

	private static final class CONTACT_PANEL {
		private final Utils.FIFO_QUEUE<String> messageQueue = new Utils.FIFO_QUEUE<>(1000);
		private final Pane contactPane;
		private String name;
		private boolean friend = false;

		private BiConsumer<FXMainController, String> sendAction = (ctrler, uid) -> {
			if (ctrler != null) {
				String msg = ctrler.chatField.getText();
				String urMsg = "\nYou:" + spaceSep + msg;
				ctrler.client.sendMessage(uid, msg);
				messageQueue.add(urMsg);
				ctrler.textField.appendText(urMsg);
				ctrler.chatField.setText("");
			}
		};

		private void chatEvent(FXMainController controller) {
			controller.textField.setText("");
			messageQueue.forEach(m -> controller.textField.appendText(m));
		}

		private CONTACT_PANEL(String uid, String name, String status, FXMainController controller) {
			this.name = name;
			this.contactPane = new HBox(new Label(" " + status + "  "), new Label(" " + name + "  "));
			contactPane.setOnMouseMoved(e -> {
				controller.getScene().setCursor(Cursor.HAND);
				((Labeled) contactPane.getChildren().get(1)).setTextFill(Paint.valueOf("BLUE"));
			});
			contactPane.setOnMouseExited(e -> {
				controller.getScene().setCursor(Cursor.DEFAULT);
				((Labeled) contactPane.getChildren().get(1)).setTextFill(Paint.valueOf("BLACK"));
			});
			contactPane.setOnMouseClicked(event -> {
				if (friend) controller.splitPane.setDisable(false);
				else controller.splitPane.setDisable(true);
				controller.nameLabel.setText(name + ":  " + uid);
				controller.sendButton.setOnAction(e
						-> sendAction.accept(controller, uid));
				controller.chatField.setOnKeyPressed(e
						-> sendAction.accept(e.getCode().getName()
						.equalsIgnoreCase("Enter") ? controller : null, uid));
				controller.actualUID = uid;
				chatEvent(controller);
			});
		}
	}


	@FXML private ScrollPane scrollPane;
	@FXML private TextField chatField;
	@FXML private TextArea textField;
	@FXML private Button sendButton;
	@FXML private SplitPane splitPane;
	@FXML private Label nameLabel;
	@FXML private Label userLabel;
	@FXML private Button addContact;


	private final Map<String, CONTACT_PANEL> contactsMap = new HashMap<>();
	private String actualUID = null;
	private boolean screenActive = false;


	public FXMainController() {
		super.aborter = (strings) -> {
			try {
				client.close();
			} catch (Exception ignored) {}
			this.close();
			System.err.println(Arrays.toString(strings));
			System.exit(1);
		};
	}


	private Consumer<Map<String, CONTACT_PANEL>> dirtAction = messMap
			-> client.update().forEach(m -> {
		synchronized (this) {
			CONTACT_PANEL panel = messMap.get(m[0]);
			panel.messageQueue.add("\n" + panel.name + ":" + spaceSep + m[1]);
			if (actualUID != null) messMap.get(actualUID).chatEvent(this);
		}
	});



	private Consumer<Map<String, CONTACT_PANEL>> updAction = messMap -> {
		boolean[] stopFlag = new boolean[]{false};
		super.client.getStatus().forEach(str -> {
			synchronized (this) {
				String UID = str[0];
				String stat = str[2];
				String range = str[3];
				CONTACT_PANEL panel = messMap.get(UID);

				boolean netStat = stat.equalsIgnoreCase(BaseClient.ONLINE);
				boolean netRange = range.equalsIgnoreCase(BaseClient.STRANGER);
				String color = netStat ? "GREEN" : "RED";
				double opacity = !netRange ? (netStat ? 1 : 0.5) : 0.25;

				if (panel != null) {
					panel.contactPane.setOpacity(opacity);
					Label label = ((Label) panel.contactPane.getChildren().get(0));
					if (!netRange) label.setTextFill(Paint.valueOf(color));
					panel.friend = !netRange;
				}
				if (actualUID != null && !stopFlag[0]) {
					boolean colorize = netStat && UID.equalsIgnoreCase(actualUID);
					String moreColor = !netRange ? (colorize ? "GREEN" : "RED") : "BLACK";
					nameLabel.setTextFill(Paint.valueOf(moreColor));

					stopFlag[0] = UID.equalsIgnoreCase(actualUID);
				}
			}
		});
	};





	@Override
	public FXClientController action() {
		screenActive = true;
		try {
			userLabel.setText(client.getInfo(client.getUID())[1]);
		} catch (Exception e) {
			aborter.abort("Connection lost");
		}
		addContact.setOnAction(event -> {
			behavior.createAddUidDialog();
			//TODO ADD USER TO CONTACT PANEL
		});

		loadContactsPanel();
		new Thread(() -> {
			while (screenActive) try {
				dirtAction.accept(contactsMap);
				Thread.sleep(50);
			} catch (InterruptedException ignore) {
				aborter.abort("STREAM CORRUPTION");
			}
		}).start();
		new Thread(() -> {
			while (screenActive) try {
				updAction.accept(contactsMap);
				Thread.sleep(1000);
			} catch (InterruptedException ignore) {
				aborter.abort("STREAM CORRUPTION");
			}
		}).start();
		return this;
	}



	@Override
	public FXClientController close() {
		this.screenActive = false;
		return super.close();
	}


	@SuppressWarnings("SuspiciousToArrayCall")
	private FXMainController loadContactsPanel() {
		List<Node> panelList = new LinkedList<>();
		super.client.getStatus().forEach(l -> {
			String UID = l[0];
			String name = l[1];
			CONTACT_PANEL panel = new CONTACT_PANEL(UID, name, statusSymb, this);
			((Labeled) panel.contactPane.getChildren().get(0)).setFont(new Font(13));
			panelList.add(panel.contactPane);
			if (!contactsMap.containsKey(UID)) contactsMap.put(UID, panel);
		});
		//new VBox(
		scrollPane.setContent(new VBox(panelList.toArray(new Pane[0])));
		return this;
	}

}
