package be.rhea.remote.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class SimpleProtocolTCPServer extends SimpleProtocolServer implements Runnable {

	private static final String HELLO = "HELLO";

	private static final String OK = "OK";

	private static final String DONE = "DONE";

	private static final String NOK = "NOK";

	private static final String BYE = "BYE";

	private static final String UNKNOWN_COMMAND = "UNKNOWN_COMMAND";

	int port;

	Thread serverThread;

	boolean running = true;

	ServerSocket serverSocket;

	private String protocol;

	private InputStream inputStream;

	public SimpleProtocolTCPServer(int port, String protocol) {
		this.port = port;
		this.protocol = protocol;
	}

	public void start() throws IOException {
		serverSocket = new ServerSocket(port);
	
		serverThread = new Thread(this);
		serverThread.start();
	}

	public void stop() throws IOException {
		running = false;
		serverThread = null;
		serverSocket.close();
	}
	
	public void run() {
		try {
			while (running) {
				Socket socket = serverSocket.accept();
				Thread sockethandletThread = new Thread(new SocketHandler(
						socket));
				sockethandletThread.start();

			}
		} catch (IOException e) {
			if (running) {
				logger.log(Level.SEVERE,
						   "Exception occurred when starting SimpleProtocolServer",
						   e);
				throw new RuntimeException(e);
			}
		}

	}

	private void send(PrintWriter out, String lineToSend) {
		out.println(lineToSend);
		out.flush();
	}

	private class UnClosableInputStream extends BufferedInputStream {

		protected UnClosableInputStream(InputStream arg0) {
			super(arg0);
		}

		public void close() throws IOException {
		}

	}

	private class SocketHandler implements Runnable {

		private final Socket socket;

		public SocketHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			PrintWriter out = null;
			BufferedReader in = null;
			try {
				socket.setReceiveBufferSize(32 * 1024);
				out = new PrintWriter(socket.getOutputStream(), true);
				inputStream = socket.getInputStream();
				in = new BufferedReader(new InputStreamReader(inputStream));

				String line = in.readLine();

				if ((protocol + ":" + HELLO).equals(line)) {
					send(out, protocol + ":" + OK);
					while ((line = in.readLine()) != null) {
						if ((protocol + ":" + BYE).equals(line)) {
							send(out, protocol + ":" + OK);
							close(socket, out, in);
							break;
						} else {
							String[] lineParts = line.split(" ");
							SimpleProtocolServerCommand command = (SimpleProtocolServerCommand) commandMap
									.get(lineParts[0]);
							String[] parameters = null;
							if (lineParts.length > 1) {
								parameters = new String[lineParts.length - 1];
								System.arraycopy(lineParts, 1, parameters, 0, lineParts.length - 1);
							}
							 
							
							if (command != null) {
								if (command instanceof SimpleProtocolServerExecuteCommand) {
									send(
											out,
											((SimpleProtocolServerExecuteCommand) command)
													.execute(parameters));
								} else if (command instanceof SimpleProtocolServerStreamingCommand) {
									send(out, protocol + ":" + OK);
									send(
											out,
											((SimpleProtocolServerStreamingCommand) command)
													.execute(new UnClosableInputStream(
															inputStream)));
									while (!in.readLine().endsWith(
											protocol + ":" + DONE))
										;
									send(out, protocol + ":" + OK);
								}
							} else {
								send(out, protocol + ":" + UNKNOWN_COMMAND);
							}
						}
					}
				} else {
					send(out, protocol + ":" + NOK);
					close(socket, out, in);
				}
			} catch (Throwable t) {
				logger.log(Level.SEVERE,
						"Exception occurred in SimpleProtocolServer", t);
				try {
					close(socket, out, in);
				} catch (IOException e) {
				}
			}

		}

		private void close(Socket socket, PrintWriter out, BufferedReader in)
				throws IOException {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (socket != null) {
				socket.close();
			}
		}
	}

}
