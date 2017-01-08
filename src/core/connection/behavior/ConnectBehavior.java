package core.connection.behavior;

import core.CoreInterface;
import core.database.IBase;

import java.io.PrintWriter;


/**
 * @author Henry on 01/01/17.
 */
@FunctionalInterface
public interface ConnectBehavior extends CoreInterface {
	void process(IBase dataBase, Object ... args) throws Exception;

	static PrintWriter outPrintln(String line, PrintWriter writer) {
		writer.println(line);
		return writer;
	}
}
