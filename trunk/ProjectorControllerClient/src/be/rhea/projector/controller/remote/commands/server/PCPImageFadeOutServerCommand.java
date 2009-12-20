package be.rhea.projector.controller.remote.commands.server;

import javax.swing.JPanel;

import be.rhea.projector.controller.client.ui.ImagePanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPImageFadeOutServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final ImagePanel mediaPanel;

	public PCPImageFadeOutServerCommand(JPanel panel, ImagePanel imagePanel) {
		this.mediaPanel = imagePanel;
	}

	public String execute(String[] parameters) {
		int fadeOutTime = Integer.valueOf(parameters[0]);
		mediaPanel.fadeOut(fadeOutTime);
		
		return "Image FadeOut";
	}

}
