package be.rhea.projector.controller.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

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

public class ClientPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public ClientPanel(String mediaDir, int port) throws IOException {
		this.setBackground(Color.BLACK);
		this.setLayout(new BorderLayout());
		
		ColorPanel colorPanel = new ColorPanel();
		ColorPanel transitionColorPanel = new ColorPanel();
		ImagePanel imagePanel = new ImagePanel();
		Map<String,VideoMediaPanel> mediaPanelMap = Collections.synchronizedMap(new HashMap<String,VideoMediaPanel>());
		
		Map<String, SimpleProtocolServerCommand> commandMap = new HashMap<String, SimpleProtocolServerCommand>();
		
		PCPMediaTarFileTransferServerCommand mediaTarFileTransferServerCommand = new PCPMediaTarFileTransferServerCommand();
		mediaTarFileTransferServerCommand.setMediaDir(mediaDir);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.UPLOAD_MEDIA, mediaTarFileTransferServerCommand);
		
		PCPVideoMediaStartServerCommand videoMediaStartServerCommand = new PCPVideoMediaStartServerCommand(this, mediaPanelMap);
		videoMediaStartServerCommand.setMediaDir(mediaDir);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.START_VIDEO_MEDIA, videoMediaStartServerCommand);
		
		PCPVideoMediaPreloadServerCommand videoMediaPreloadServerCommand = new PCPVideoMediaPreloadServerCommand(mediaPanelMap);
		videoMediaPreloadServerCommand.setMediaDir(mediaDir);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.PRELOAD_VIDEO_MEDIA, videoMediaPreloadServerCommand);
	
		PCPColorServerCommand colorLoadServerCommand = new PCPColorServerCommand(this, colorPanel);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.START_COLOR, colorLoadServerCommand);
	
		PCPTransitionColorServerCommand transitionColorLoadServerCommand = new PCPTransitionColorServerCommand(this, transitionColorPanel);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.START_TRANSITION_COLOR, transitionColorLoadServerCommand);
	
		PCPImageLoadServerCommand imageLoadServerCommand = new PCPImageLoadServerCommand(imagePanel);
		imageLoadServerCommand.setMediaDir(mediaDir);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.LOAD_IMAGE, imageLoadServerCommand);
	
		PCPImagePlayServerCommand imagePlayServerCommand = new PCPImagePlayServerCommand(this, imagePanel);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.START_IMAGE, imagePlayServerCommand);
	
		PCPImageFadeOutServerCommand imageFadeOutServerCommand = new PCPImageFadeOutServerCommand(this, imagePanel);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.FADE_OUT_IMAGE, imageFadeOutServerCommand);
	
		PCPVideoMediaStopServerCommand videoMediaStopServerCommand = new PCPVideoMediaStopServerCommand(this, mediaPanelMap);
		commandMap.put(PCP.PROTOCOL + ":" + PCP.STOP_VIDEO_MEDIA, videoMediaStopServerCommand);
	
	//	SimpleProtocolServer server = new SimpleProtocolTCPServer(port, PCP.PROTOCOL);
	//	SimpleProtocolServer server = new SimpleProtocolUDPServer(port, PCP.PROTOCOL);
		SimpleProtocolServer server = new SimpleProtocolUDPWithRetryServer(port, PCP.PROTOCOL);
		server.setCommandMap(commandMap);
	    Logger logger = Logger.getLogger("be.rhea.remote.server.SimpleProtocolServer");
	    logger.setLevel(Level.ALL);
	    logger.log(Level.INFO, "Client started");
		server.setLogger(logger);
	    server.start();
	}

}
