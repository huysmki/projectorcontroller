package be.rhea.projector.controller.remote.commands.server;

import javax.swing.JPanel;

import be.rhea.projector.controller.client.ui.ImagePanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPImagePlayServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final ImagePanel mediaPanel;
	private final JPanel panel;

	public PCPImagePlayServerCommand(JPanel panel, ImagePanel imagePanel) {
		this.panel = panel;
		this.mediaPanel = imagePanel;
	}

	public String execute(String[] parameters) {
		int fadeInTime = Integer.valueOf(parameters[0]);
		panel.removeAll();
		panel.add(mediaPanel);
		mediaPanel.setDoubleBuffered(true);
		mediaPanel.play(fadeInTime);
		
		return "Image Started";
	}

}
