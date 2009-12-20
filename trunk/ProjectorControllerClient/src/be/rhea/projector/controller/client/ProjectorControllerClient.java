package be.rhea.projector.controller.client;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import be.rhea.projector.controller.client.listener.FrameMouseListener;
import be.rhea.projector.controller.client.ui.ClientPanel;
import be.rhea.projector.controller.client.ui.ColorPanel;
import be.rhea.projector.controller.client.ui.ImagePanel;
import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.projector.controller.remote.commands.server.PCPColorServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPImageFadeOutServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPImageLoadServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPImagePlayServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPMediaTarFileTransferServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPTransitionColorServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPVideoMediaPreloadServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPVideoMediaStartServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPVideoMediaStopServerCommand;
import be.rhea.remote.PCP;
import be.rhea.remote.server.SimpleProtocolServer;
import be.rhea.remote.server.SimpleProtocolServerCommand;
import be.rhea.remote.server.SimpleProtocolUDPWithRetryServer;

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
		frame.getContentPane().add(new ClientPanel(mediaDir, port));
		frame.setVisible(true);
		

	}
}