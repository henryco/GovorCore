package core.connection.behavior;

import core.CoreProtocol;
import core.database.IBase;

import java.io.PrintWriter;


/**
 * @author Henry on 01/01/17.
 */
@FunctionalInterface
public interface ConnectBehavior extends CoreProtocol {
	void process(IBase dataBase, Object ... args) throws Exception;

	static PrintWriter outPrintln(String line, PrintWriter writer) {
		writer.println(line);
		return writer;
	}
}
