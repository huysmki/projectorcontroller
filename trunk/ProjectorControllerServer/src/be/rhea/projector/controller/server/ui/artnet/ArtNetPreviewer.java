package be.rhea.projector.controller.server.ui.artnet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ArtNetPreviewer extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private DatagramSocket datagramSocket;
	private boolean listening = false;
	private JPanel valuePanel;

	public ArtNetPreviewer(int port) throws IOException {
		this.setBackground(Color.YELLOW);
		datagramSocket = new DatagramSocket(port);
		datagramSocket.setSoTimeout(0);
		this.setLayout(new BorderLayout());
		valuePanel = new JPanel();
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane(valuePanel);
		this.add(scrollPane, BorderLayout.CENTER);
		
		startListening();
	}

	private void startListening() {
		listening = true;
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		byte[] oldPacket = null;
		while (listening ) {
			byte[] data = new byte[530];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				datagramSocket.receive(packet);
				byte[] receivedData = packet.getData();
				if (oldPacket == null || !Arrays.equals(receivedData, oldPacket)) {
					if (receivedData[0] == (byte)0x41 &&
						receivedData[1] == (byte)0x72 &&
						receivedData[2] == (byte)0x74 &&
						receivedData[3] == (byte)0x2d &&
						receivedData[4] == (byte)0x4e &&
						receivedData[5] == (byte)0x65 &&
						receivedData[6] == (byte)0x74) {
						byte lengthHigh = data[16];
						byte lengthLow = data[17];
						int length = (unsignedByteToInt(lengthHigh) << 8) | unsignedByteToInt(lengthLow);
						valuePanel.removeAll();
						for (int i = 0; i < length; i++) {
							int value = unsignedByteToInt(data[18 + i]);
							valuePanel.add(new ArtNetValuePanel(i, value));
						}
						valuePanel.repaint();
						valuePanel.revalidate();
					}
				}
				oldPacket = receivedData;

			} catch (IOException e) {
			}
		}
	}
	
	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}	

	public void stopListening() {
		listening = false;
		datagramSocket.close();
	}
	
	private class ArtNetValuePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int index;
		private final int value;

		public ArtNetValuePanel(int index, int value) {
			this.index = index;
			this.value = value;
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawString(String.valueOf(index + 1), 1, 10);
			float percentage = (float)value / 255;
			g.setColor(Color.RED);
			g.fillRect(30, 0, (int)((this.getWidth() - 40) * percentage), 10);
			g.setColor(Color.BLACK);
			g.drawRect(30, 0, this.getWidth() - 40, 10);
			g.drawString((int)(percentage * 100) + "%", (this.getWidth() / 2), 10);
			
		}
		
	}
}
