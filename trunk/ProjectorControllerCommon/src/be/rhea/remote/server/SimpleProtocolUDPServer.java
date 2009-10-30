package be.rhea.remote.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimpleProtocolUDPServer extends SimpleProtocolServer implements Runnable {

	int port;

	Thread serverThread;

	boolean running = true;

	DatagramSocket datagramSocket;

	private final String protocol;

	public SimpleProtocolUDPServer(int port, String protocol) {
		this.port = port;
		this.protocol = protocol;
	}

	public void start() throws IOException {
		datagramSocket = new DatagramSocket(port);
		datagramSocket.setSoTimeout(0);
	
		serverThread = new Thread(this);
		serverThread.start();
	}

	public void stop() throws IOException {
		running = false;
		serverThread = null;
		datagramSocket.close();
	}
	
	public void run() {
		try {
			while (running) {
				System.out.println("Waiting for message");
				byte[] buffer = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(packet);
				System.out.println("Message received");
				Thread sockethandletThread = new Thread(new SocketHandler(
						packet));
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

	private class SocketHandler implements Runnable {

		private final DatagramPacket packet;

		public SocketHandler(DatagramPacket packet) {
			this.packet = packet;
		}

		public void run() {
			try {
				String line = new String(packet.getData(), 0, packet.getLength());
				System.out.println("Data = " + line);
				String[] lineParts = line.split(" ");
				System.out.println("CommandName = " + lineParts[0]);
				SimpleProtocolServerCommand command = (SimpleProtocolServerCommand) commandMap
						.get(lineParts[0]);
				String[] parameters = null;
				if (lineParts.length > 1) {
					parameters = new String[lineParts.length - 1];
					System.arraycopy(lineParts, 1, parameters, 0, lineParts.length - 1);
				}
				 
				System.out.println("Command = " + command);
				if (command != null) {
					if (command instanceof SimpleProtocolServerExecuteCommand) {
						((SimpleProtocolServerExecuteCommand) command).execute(parameters);
					} else if (command instanceof SimpleProtocolServerStreamingCommand) {
						throw new NotImplementedException();
					}
				}
			} catch (Throwable t) {
				logger.log(Level.SEVERE,
						"Exception occurred in SimpleProtocolServer", t);
			}

		}
	}

}
