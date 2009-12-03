package be.rhea.remote.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;


public class ArtNetProtocolUDPWithRetryClient {

	private int port;
	private String host;

	public ArtNetProtocolUDPWithRetryClient(String host, int port) {
		this.port = port;
		this.host = host;
	}

	public void sendData(List<Integer> data) throws SocketException,
			UnknownHostException, IOException {
		Thread senderThread = new Thread(new UdpMessagSenderThread(data));
		senderThread.start();
	}
	
	private class UdpMessagSenderThread implements Runnable {

		private static final int SLEEP_TIME_BETWEEN_SEND_REQUESTS = 200;
		private final List<Integer> data;
		public UdpMessagSenderThread(List<Integer> data) {
			this.data = data;
		}

		public void run() {
			try {
				DatagramSocket datagramSocket = new DatagramSocket();
				InetAddress a = InetAddress.getByName(host);
			    byte[] inetaddress = a.getAddress();

				byte[] dataBytes = new byte[18 + data.size()];
			
				dataBytes[0] = (byte)0x41; //ID
				dataBytes[1] = (byte)0x72; //ID
				dataBytes[2] = (byte)0x74; //ID
				dataBytes[3] = (byte)0x2d; //ID
				dataBytes[4] = (byte)0x4e; //ID
				dataBytes[5] = (byte)0x65; //ID
				dataBytes[6] = (byte)0x74; //ID
				dataBytes[7] = (byte)0x00; //ID terminator
				dataBytes[8] = (byte)0x00; //OpCode 
				dataBytes[9] = (byte)0x50; //Opcode
				dataBytes[10] = (byte)0x00; //ProtVerH
				dataBytes[11] = (byte)0x0e; //ProtVer
				dataBytes[12] = (byte)0x08; //Sequence
				dataBytes[13] = (byte)0x00; //Physical
				dataBytes[14] = (byte)0x00; //Universe
				dataBytes[15] = (byte)0x00; //Universe 
				dataBytes[16] = (byte)(data.size()>>8) ; //LengthHi
				dataBytes[17] = (byte)data.size(); //Length	    

				for (int i = 0; i < data.size(); i++) {
					dataBytes[18 + i] = (byte) data.get(i).intValue();
				}
				
				DatagramPacket packet = new DatagramPacket(dataBytes, dataBytes.length, InetAddress.getByAddress(inetaddress), port);
				datagramSocket.send(packet);
				Thread.sleep(SLEEP_TIME_BETWEEN_SEND_REQUESTS);
				datagramSocket.send(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}