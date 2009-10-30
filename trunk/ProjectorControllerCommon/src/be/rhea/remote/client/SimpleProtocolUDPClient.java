package be.rhea.remote.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class SimpleProtocolUDPClient extends SimpleProtocolClient {

	private int port;
	private String host;
	private final String protocol;

	public SimpleProtocolUDPClient(String host, int port, String protocol) {
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
		byte[] inetaddress = new byte[4];
		inetaddress[0] = (byte) 0xFF;
		inetaddress[1] = (byte) 0xFF;
		inetaddress[2] = (byte) 0xFF;
		inetaddress[3] = (byte) 0xFF;
//		inetaddress[0] = (byte) 10;
//		inetaddress[1] = (byte) 0;
//		inetaddress[2] = (byte) 2;
//		inetaddress[3] = (byte) 240;
		DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getByAddress(inetaddress), port);
		datagramSocket.send(packet);
		System.out.println("Sending " + data + " to " + InetAddress.getByAddress(inetaddress).getHostAddress() + ":" + port);
	}

	@Override
	public String sendCommand(String command, String[] parameter) throws IOException {
		StringBuilder commandString = new StringBuilder();
		commandString.append(protocol);
		commandString.append(":");
		commandString.append(command);
		if (parameter != null) {
			for (int i = 0; i < parameter.length; i++) {
				commandString.append(" ");
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
