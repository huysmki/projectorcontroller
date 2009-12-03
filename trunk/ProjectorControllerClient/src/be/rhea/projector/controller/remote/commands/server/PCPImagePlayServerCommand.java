package be.rhea.projector.controller.remote.commands.server;

import javax.swing.JFrame;

import be.rhea.projector.controller.client.ui.ImagePanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPImagePlayServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final ImagePanel mediaPanel;
	private final JFrame frame;

	public PCPImagePlayServerCommand(JFrame frame, ImagePanel imagePanel) {
		this.frame = frame;
		this.mediaPanel = imagePanel;
	}

	public String execute(String[] parameters) {
		int fadeInTime = Integer.valueOf(parameters[0]);
		frame.getContentPane().removeAll();
		frame.getContentPane().add(mediaPanel);
		mediaPanel.setDoubleBuffered(true);
		mediaPanel.play(fadeInTime);
		
		return "Image Started";
	}

}
