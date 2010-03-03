package be.rhea.remote.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import be.rhea.remote.PCP;


public class SimpleProtocolUDPWithRetryClient extends SimpleProtocolClient {

	private int port;
	private String host;
	private final String protocol;
	private static final int SLEEP_TIME_BETWEEN_SEND_REQUESTS = 20;

	public SimpleProtocolUDPWithRetryClient(String host, int port, String protocol) {
		this.port = port;
		this.host = host;
		this.protocol = protocol;
	}

	@Override
	public void connect() throws IOException {
	}

	@Override
	public String sendCommand(String command) throws IOException {
		sendData(protocol + ":" + command);
		
		return "OK";
	}
	
    	

	private void sendData(String data) throws SocketException,
			UnknownHostException, IOException {
		DatagramSocket datagramSocket = new DatagramSocket();
		InetAddress a = InetAddress.getByName(host);
	    byte[] inetaddress = a.getAddress();
	    
		DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getByAddress(inetaddress), port);
		datagramSocket.send(packet);
		try {
			Thread.sleep(SLEEP_TIME_BETWEEN_SEND_REQUESTS);
		} catch (InterruptedException e) {
		}
		datagramSocket.send(packet);
	}

	@Override
	public String sendCommand(String command, String[] parameter) throws IOException {
		StringBuilder commandString = new StringBuilder();
		commandString.append(protocol);
		commandString.append(":");
		commandString.append(command);
		if (parameter != null) {
			for (int i = 0; i < parameter.length; i++) {
				commandString.append(PCP.PARAMETER_DELIMITER);
				commandString.append(parameter[i]);
				
			}
		}
		sendData(commandString.toString());

		return "OK";
	}	
	
	@Override
	public String executeStreamingCommand(String command, SimpleProtocolClientStreamingCommand clientCommand) throws IOException {
		return null;
	}

	@Override
	public void disconnect() throws IOException {
	}
}
