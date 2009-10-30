package be.rhea.remote.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class SimpleProtocolTCPClient extends SimpleProtocolClient {

	private int port;
	private String host;
	private static final String HANDSHAKE = "HELLO";
	private static final String BYE = "BYE";
	private static final String DONE = "DONE";
	private static final String OK = "OK";
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String protocol;
	private OutputStream outputStream;

	public SimpleProtocolTCPClient(String host, int port, String protocol) {
		this.port = port;
		this.host = host;
		this.protocol = protocol;
	}

	@Override
	public void connect() throws IOException {
		socket = new Socket(host, port);
		socket.setSoTimeout(0);
		socket.setTcpNoDelay(true);
		socket.setKeepAlive(false);
		socket.setSendBufferSize(1024 * 32);
		outputStream = socket.getOutputStream();
		out = new PrintWriter(outputStream, true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		if (!(protocol + ":" + OK).equals(send(protocol + ":" + HANDSHAKE))) {
			protocolViolated();
		}
	}

	@Override
	public String sendCommand(String command) throws IOException {
		return send(protocol + ":" + command);
	}

	@Override
	public String sendCommand(String command, String[] parameter) throws IOException {
		StringBuilder commandString = new StringBuilder();
		commandString.append(protocol + ":" + command);
		if (parameter != null) {
			for (int i = 0; i < parameter.length; i++) {
				commandString.append(" ");
				commandString.append(parameter[i]);
				
			}
		}
		return send(commandString.toString());
	}	
	
	@Override
	public String executeStreamingCommand(String command, SimpleProtocolClientStreamingCommand clientCommand) throws IOException {
		if (!(protocol + ":" + OK).equals(send(protocol + ":" + command))) {
			protocolViolated();
		}
		clientCommand.execute(new UnClosableOutputStream(outputStream));
		String returnValue = in.readLine();
		
		String returnOfDoneCommand = send(protocol + ":" + DONE);
		if (!(protocol + ":" + OK).equals(returnOfDoneCommand)) {
			protocolViolated();
		}
		return returnValue;
	}

	@Override
	public void disconnect() throws IOException {
		String send = send(protocol + ":" + BYE);
		if (!(protocol + ":" + OK).equals(send)) {
			protocolViolated();
		}
		out.close();
		in.close();
		socket.close();
	}

	private String send(String line) throws IOException {
		out.write(line + "\n");
		out.flush();
		return in.readLine();
	}

	private void protocolViolated() throws IOException {
		throw new IOException("Protocol violation");
	}
	
	private class UnClosableOutputStream extends BufferedOutputStream {

		public UnClosableOutputStream(OutputStream arg0) {
			super(arg0);
		}
		
		public void close() throws IOException {
		}
	}
}
