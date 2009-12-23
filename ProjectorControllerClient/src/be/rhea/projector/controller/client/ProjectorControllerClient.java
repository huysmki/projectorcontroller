package be.rhea.projector.controller.client;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import be.rhea.projector.controller.client.listener.FrameMouseListener;
import be.rhea.projector.controller.client.ui.ClientPanel;

public class ProjectorControllerClient {
	public static void main(String args[]) throws IOException {

		if (args.length < 2) {
			JOptionPane.showMessageDialog(null, "Usage : java -jar ProjectorClient.jar <listenPort> <mediaDir>");
			System.exit(1);
		}
        int port = Integer.valueOf(args[0]);
        String mediaDir = args[1];
        if (!mediaDir.endsWith("/")) {
        	mediaDir += "/";
        }
 
		JFrame frame = new JFrame("Media Tester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FrameMouseListener frameMouseListener = new FrameMouseListener(frame);
		frame.addMouseListener(frameMouseListener);
		frame.addMouseMotionListener(frameMouseListener);
		frame.setUndecorated(true);
		frame.setSize(400, 300);
		final ClientPanel clientPanel = new ClientPanel(mediaDir, port);
		clientPanel.startListening();
		frame.getContentPane().add(clientPanel);
		frame.setVisible(true);

		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { clientPanel.stopListening(); }
		});		
	}
}