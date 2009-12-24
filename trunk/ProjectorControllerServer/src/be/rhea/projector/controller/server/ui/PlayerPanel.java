package be.rhea.projector.controller.server.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.rhea.projector.controller.client.ui.ClientPanel;
import be.rhea.projector.controller.server.player.ScenarioPlayer;
import be.rhea.projector.controller.server.player.StateChangedEvent;
import be.rhea.projector.controller.server.player.StateChangedListener;
import be.rhea.projector.controller.server.player.StateChangedEvent.State;
import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;

public class PlayerPanel extends JPanel implements ActionListener, StateChangedListener {
	private static final String STOP = "STOP";
	private static final String PAUSE = "PAUSE";
	private static final String PLAY = "PLAY:";
	private static final long serialVersionUID = 1L;
	private List<ClientPanel> clientPanels = new ArrayList<ClientPanel>();
	private JButton stopButton;
	private JButton pauseButton;
	private JLabel statusLabel;

	public PlayerPanel(Scenario scenario) {
		this.setLayout(new GridLayout(1,3));

		JScrollPane createPlayersPanel = createPlayersPanel(scenario);
		this.add(createPlayersPanel);
		
		JScrollPane midPanel = createPauseStopPanel();
		this.add(midPanel);
		
		JScrollPane createClientPanelPreviewPanel = createClientPanelPreviewPanel(scenario);
		this.add(createClientPanelPreviewPanel);
		
		ScenarioPlayer.addStateChangeListener(this);
		ScenarioPlayer.setScenario(scenario);
	}

	private JScrollPane createPlayersPanel(Scenario scenario) {
		JPanel panel = new JPanel();
		List<Scene> scenes = scenario.getScenes();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		int index = 0;
		for (Scene scene : scenes) {
			panel.add(createPlayPanelForScene(scene, index++));
		}
		JScrollPane scrollPane = new JScrollPane(panel);
		return scrollPane;
	}

	private JScrollPane createPauseStopPanel() {
		JPanel midPanel = new JPanel(); 
		midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));
		midPanel.add(Box.createRigidArea(new Dimension(0,50)));

		JPanel stopPausePanel = new JPanel();
		stopPausePanel.setLayout(new BoxLayout(stopPausePanel, BoxLayout.X_AXIS));
		
		stopPausePanel.add(Box.createHorizontalGlue());
		
		pauseButton = new JButton(new ImageIcon(this.getClass().getResource("/pause.png")));
		pauseButton.setFocusable(false);
		pauseButton.setActionCommand(PAUSE);
		pauseButton.addActionListener(this);
		pauseButton.setPreferredSize(new Dimension(150,150));
		pauseButton.setMinimumSize(new Dimension(150,150));
		pauseButton.setMaximumSize(new Dimension(150,150));
		stopPausePanel.add(pauseButton);
		
		stopPausePanel.add(Box.createRigidArea(new Dimension(40,0)));

		stopButton = new JButton(new ImageIcon(this.getClass().getResource("/stop.png")));
		stopButton.setFocusable(false);
		stopButton.setActionCommand(STOP);
		stopButton.addActionListener(this);
		stopButton.setPreferredSize(new Dimension(150,150));
		stopButton.setMinimumSize(new Dimension(150,150));
		stopButton.setMaximumSize(new Dimension(150,150));
		stopPausePanel.add(stopButton);
		stopPausePanel.add(Box.createHorizontalGlue());

		midPanel.add(stopPausePanel);

		midPanel.add(Box.createRigidArea(new Dimension(0,30)));
		
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new BoxLayout(testPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("");
		statusLabel.setAlignmentX(LEFT_ALIGNMENT);
		statusLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		testPanel.add(statusLabel);
		testPanel.setAlignmentX(CENTER_ALIGNMENT);
		midPanel.add(testPanel);
		
		
		JScrollPane scrollPane = new JScrollPane(midPanel);
		return scrollPane;
	}

	private JScrollPane createClientPanelPreviewPanel(Scenario scenario) {
		JPanel overviewClientsPanel = new JPanel();
		overviewClientsPanel.setLayout(new BoxLayout(overviewClientsPanel, BoxLayout.Y_AXIS));
		List<Client> clients = scenario.getClients();
		for (Client client : clients) {
			JLabel label = new JLabel(client.getName());
			label.setAlignmentX(CENTER_ALIGNMENT);
			overviewClientsPanel.add(label);
			try {
				ClientPanel clientPanel = new ClientPanel("C:/temp/gekko/show/", client.getPort());
				clientPanel.setPreferredSize(new Dimension(200,150));
				clientPanel.setMinimumSize(new Dimension(200,150));
				clientPanel.setMaximumSize(new Dimension(200,150));
				clientPanel.setAlignmentX(CENTER_ALIGNMENT);
				overviewClientsPanel.add(clientPanel);
				clientPanels.add(clientPanel);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JScrollPane scrollPane2 = new JScrollPane(overviewClientsPanel);
		return scrollPane2;
	}
	
	public void stopMediaPanels() {
		for (ClientPanel clientPanel : clientPanels) {
			clientPanel.stopListening();
		}
	}
	
	private JPanel createPlayPanelForScene(Scene scene, int index) {
		JPanel playPanel = new JPanel();
		playPanel.setLayout(new BoxLayout(playPanel, BoxLayout.X_AXIS));
		playPanel.add(Box.createRigidArea(new Dimension(5,0)));
		JLabel label = new JLabel(scene.getName());
		label.setFont(new Font("Arial", Font.PLAIN, 25));
		label.setPreferredSize(new Dimension(300,50));
		label.setMinimumSize(new Dimension(300,50));
		label.setMaximumSize(new Dimension(300,50));
		label.setAlignmentX(LEFT_ALIGNMENT);
		playPanel.add(label);
		playPanel.add(Box.createHorizontalGlue());
		JButton playButton = new JButton(new ImageIcon(this.getClass().getResource("/play.png")));
		playButton.setFocusable(false);
		playButton.setActionCommand(PLAY + index);
		playButton.addActionListener(this);
		
		playButton.setPreferredSize(new Dimension(50,50));
		playButton.setMinimumSize(new Dimension(50,50));
		playButton.setMaximumSize(new Dimension(50,50));
		playButton.setAlignmentX(RIGHT_ALIGNMENT);
		playPanel.add(playButton);
		playPanel.add(Box.createRigidArea(new Dimension(20,0)));
		
		return playPanel;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().startsWith(PLAY)) {
			if (ScenarioPlayer.isPlaying()) {
				ScenarioPlayer.stop();
			}
			
			int indexOf = Integer.valueOf(actionEvent.getActionCommand().split(":")[1]);
			if (indexOf >= 0) {
				ScenarioPlayer.play(indexOf);
				pauseButton.setEnabled(true);
				stopButton.setEnabled(true);
			}
		} else if (PAUSE.equals(actionEvent.getActionCommand())) {
				ScenarioPlayer.pause();
		} else if (STOP.equals(actionEvent.getActionCommand())) {
				ScenarioPlayer.stop();
				pauseButton.setEnabled(false);
				stopButton.setEnabled(false);				
		}
	}
	
	public void stateChanged(StateChangedEvent e) {
		if (e.getNewState().equals(State.STOP)) {
			pauseButton.setEnabled(false);
			stopButton.setEnabled(false);
			statusLabel.setText("");
		} else if (e.getNewState().equals(State.PLAY)) {
			statusLabel.setText("Playing");
		} else if (e.getNewState().equals(State.PAUSE)) {
			statusLabel.setText("Paused");
		} else if (e.getNewState().equals(State.MANUAL_ACKNOWLEDGE)) {
			statusLabel.setText("Pause : " + e.getMessage());
		} 
	}	
}
