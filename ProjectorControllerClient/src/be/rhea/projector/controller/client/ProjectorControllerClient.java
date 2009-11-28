package be.rhea.projector.controller.client;

// Fig. 21.7: MediaTest.java
// A simple media player
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import be.rhea.projector.controller.client.listener.FrameMouseListener;
import be.rhea.projector.controller.client.ui.ColorPanel;
import be.rhea.projector.controller.client.ui.ImagePanel;
import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.projector.controller.remote.commands.server.PCPColorServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPImageFadeOutServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPImageLoadServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPImagePlayServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPMediaTarFileTransferServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPTransitionColorServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPVideoMediaStartServerCommand;
import be.rhea.projector.controller.remote.commands.server.PCPVideoMediaStopServerCommand;
import be.rhea.remote.PCP;
import be.rhea.remote.server.SimpleProtocolServer;
import be.rhea.remote.server.SimpleProtocolServerCommand;
import be.rhea.remote.server.SimpleProtocolUDPServer;

public class ProjectorControllerClient {
	public static void main(String args[]) throws IOException {

        int port = Integer.valueOf(args[0]);
 
		JFrame frame = new JFrame("Media Tester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FrameMouseListener frameMouseListener = new FrameMouseListener(frame);
		frame.addMouseListener(frameMouseListener);
		frame.addMouseMotionListener(frameMouseListener);
		frame.setUndecorated(true);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setSize(400, 300);
		
		ColorPanel colorPanel = new ColorPanel();
		ColorPanel transitionColorPanel = new ColorPanel();
		ImagePanel imagePanel = new ImagePanel();
		VideoMediaPanel mediaPanel = new VideoMediaPanel();
		
		Map<String, SimpleProtocolServerCommand> commandMap = new HashMap<String, SimpleProtocolServerCommand>();
		
		PCPMediaTarFileTransferServerCommand mediaTarFileTransferServerCommand = new PCPMediaTarFileTransferServerCommand();
		mediaTarFileTransferServerCommand.setMediaDir("c:/temp/" + port);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.UPLOAD_MEDIA, mediaTarFileTransferServerCommand);
		
		PCPVideoMediaStartServerCommand videoMediaStartServerCommand = new PCPVideoMediaStartServerCommand(frame, mediaPanel);
		videoMediaStartServerCommand.setMediaDir("c:/temp/" + port);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.START_VIDEO_MEDIA, videoMediaStartServerCommand);
		
		PCPColorServerCommand colorLoadServerCommand = new PCPColorServerCommand(frame, colorPanel);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.START_COLOR, colorLoadServerCommand);

		PCPTransitionColorServerCommand transitionColorLoadServerCommand = new PCPTransitionColorServerCommand(frame, transitionColorPanel);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.START_TRANSITION_COLOR, transitionColorLoadServerCommand);

		PCPImageLoadServerCommand imageLoadServerCommand = new PCPImageLoadServerCommand(imagePanel);
		imageLoadServerCommand.setMediaDir("c:/temp/" + port);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.LOAD_IMAGE, imageLoadServerCommand);

		PCPImagePlayServerCommand imagePlayServerCommand = new PCPImagePlayServerCommand(frame, imagePanel);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.START_IMAGE, imagePlayServerCommand);

		PCPImageFadeOutServerCommand imageFadeOutServerCommand = new PCPImageFadeOutServerCommand(frame, imagePanel);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.FADE_OUT_IMAGE, imageFadeOutServerCommand);

		PCPVideoMediaStopServerCommand videoMediaStopServerCommand = new PCPVideoMediaStopServerCommand(mediaPanel);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.STOP_VIDEO_MEDIA, videoMediaStopServerCommand);

//		SimpleProtocolServer server = new SimpleProtocolTCPServer(port, PCP.PROTOCOL);
		SimpleProtocolServer server = new SimpleProtocolUDPServer(port, PCP.PROTOCOL);
		server.setCommandMap(commandMap);
        Logger logger = Logger.getLogger("be.rhea.remote.server.SimpleProtocolServer");
        logger.setLevel(Level.ALL);
        logger.log(Level.INFO, "Client started");
		server.setLogger(logger);
        server.start();
        
		frame.setVisible(true);
		

	}
}