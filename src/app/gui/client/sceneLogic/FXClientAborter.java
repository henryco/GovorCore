package app.gui.client.sceneLogic;

/**
 * @author Henry on 06/01/17.
 */
@FunctionalInterface
public interface FXClientAborter {
	void abort(String ... abortMessage);
}
