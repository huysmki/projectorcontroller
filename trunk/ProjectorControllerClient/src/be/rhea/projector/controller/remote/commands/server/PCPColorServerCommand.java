package be.rhea.projector.controller.remote.commands.server;

import java.awt.Color;

import javax.swing.JPanel;

import be.rhea.projector.controller.client.ui.ColorPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPColorServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final ColorPanel mediaPanel;
	private final JPanel panel;

	public PCPColorServerCommand(JPanel panel, ColorPanel colorPanel) {
		this.panel = panel;
		this.mediaPanel = colorPanel;
	}

	public String execute(String[] parameters) {
		panel.removeAll();
		panel.add(mediaPanel);
		
		mediaPanel.setBackground(new Color(Integer.valueOf(parameters[0]), Integer.valueOf(parameters[1]), Integer.valueOf(parameters[2])));
		mediaPanel.setVisible(true);
		mediaPanel.repaint();
		mediaPanel.revalidate();
		
		
		return "Color Loaded";
	}

}
