package be.rhea.projector.controller.client;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import be.rhea.projector.controller.client.listener.FrameMouseListener;
import be.rhea.projector.controller.client.ui.ClientPanel;

public class ProjectorControllerClient {
	private static ClientPanel clientPanel;
	private static int port;
	private static String mediaDir;

	public static void main(String args[]) throws IOException {

		if (args.length < 2) {
			JOptionPane.showMessageDialog(null, "Usage : java -jar ProjectorClient.jar <listenPort> <mediaDir>");
			System.exit(1);
		}
        port = Integer.valueOf(args[0]);
        mediaDir = args[1];
        if (!mediaDir.endsWith("/")) {
        	mediaDir += "/";
        }

		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { clientPanel.stopListening(); }
		});	
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		try {
					createAndShowGUI(port, mediaDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
	}

	private static void createAndShowGUI(int port, String mediaDir)
			throws IOException {
		JFrame frame = new JFrame("Projector Client on port " + port);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FrameMouseListener frameMouseListener = new FrameMouseListener(frame);
		frame.addMouseListener(frameMouseListener);
		frame.addMouseMotionListener(frameMouseListener);
		frame.setUndecorated(true);
		frame.setSize(400, 300);
		clientPanel = new ClientPanel(mediaDir, port);
		frame.getContentPane().add(clientPanel);
		frame.setVisible(true);
	}
}
	