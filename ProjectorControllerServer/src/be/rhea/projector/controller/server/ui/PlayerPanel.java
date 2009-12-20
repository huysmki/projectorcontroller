package be.rhea.projector.controller.server.ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import be.rhea.projector.controller.server.scenario.Scenario;

public class PlayerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Scenario scenario;

	public PlayerPanel(Scenario scenario) {
		this.scenario = scenario;
		this.setLayout(new GridLayout(1,3));
		this.add(new JButton("Test"));
		this.add(new JButton("Test"));
	}
}
