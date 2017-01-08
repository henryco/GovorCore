package core.utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Henry on 31/12/16.
 */
public final class Utils {


	public static final class FIFO_QUEUE<T> extends ArrayList<T> {

		private final int cap;
		public FIFO_QUEUE(int cap) {
			this.cap = cap;
		}

		@Override
		public boolean add(T t) {
			if (size() + 1> cap) remove(0);
			return super.add(t);
		}
	}

	public static final class SERVER_INFO {
		public final String ip4;
		public final int port;
		public SERVER_INFO(String ip4, String port) {
			this.ip4 = ip4;
			this.port = Integer.parseInt(port);
		}
		public final Socket createSocket() {
			try {
				return new Socket(ip4, port);
			} catch (Exception e) {e.printStackTrace();}
			return null;
		}
		public static void closeSocket(Socket socket) {
			try {
				socket.close();
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	public static final class UID {
		public final int integer;
		public final String string;
		public UID(int i) {
			integer = i;
			string = Integer.toString(i);
		}
		public UID(String i) {
			string = i;
			integer = Integer.parseInt(i);
		}
		public String toString(){
			return string;
		}
	}
	public static ServerSocket createServerSocket(int port) {
		try {
			return new ServerSocket(port);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	public static void closeServerSocket(ServerSocket socket) {
		try {
			socket.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	public static Socket acceptSocket(ServerSocket serverSocket) {
		try {
			return serverSocket.accept();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static PrintWriter writeToStream(String msg, Socket socket) {
		try {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.println(msg);
			return writer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void closeNFlushStream(PrintWriter pw) {
		if (pw != null) pw.flush();
	}
	public static List<String> readLinesFromStream(Socket socket, String stopReg) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			List<String> list = new ArrayList<>();
			while ((line = in.readLine()) != null) {
				if (line.equalsIgnoreCase(stopReg)) break;
				list.add(line);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String readLineFromStream(Socket socket) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return in.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static List<String[]> getDataList(Socket socket, String to, String stopReg, String splitReg) {
		Utils.closeNFlushStream(Utils.writeToStream(to, socket));
		List<String> lines = Utils.readLinesFromStream(socket, stopReg);
		List<String[]> retList = new ArrayList<>();
		if (lines != null)
			retList.addAll(lines.stream().map(s -> s.split(splitReg)).collect(Collectors.toList()));
		return retList;
	}

	public static boolean arrContainsStr(String[] arr, String val) {
		for (String s : arr) if (s.equalsIgnoreCase(val)) return true;
		return false;
	}
	public static boolean arrContainsInt(String[] arr, int val) {
		return arrContainsStr(arr, Integer.toString(val));
	}
}

