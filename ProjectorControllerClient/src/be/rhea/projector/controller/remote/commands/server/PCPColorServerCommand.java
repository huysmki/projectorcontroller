package be.rhea.projector.controller.remote.commands.server;

import java.awt.Color;

import javax.swing.JFrame;

import be.rhea.projector.controller.client.ui.ColorPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPColorServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final ColorPanel mediaPanel;
	private final JFrame frame;

	public PCPColorServerCommand(JFrame frame, ColorPanel colorPanel) {
		this.frame = frame;
		this.mediaPanel = colorPanel;
	}

	public String execute(String[] parameters) {
		frame.getContentPane().removeAll();
		frame.getContentPane().add(mediaPanel);
		
		mediaPanel.setBackground(new Color(Integer.valueOf(parameters[0]), Integer.valueOf(parameters[1]), Integer.valueOf(parameters[2])));
		mediaPanel.setVisible(true);
		mediaPanel.repaint();
		mediaPanel.revalidate();
		
		
		return "Color Loaded";
	}

}
