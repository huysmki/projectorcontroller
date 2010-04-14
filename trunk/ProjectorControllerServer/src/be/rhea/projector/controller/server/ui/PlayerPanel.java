package be.rhea.projector.controller.server.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import be.rhea.projector.controller.client.ui.ClientPanel;
import be.rhea.projector.controller.server.ProjectorControllerServer;
import be.rhea.projector.controller.server.player.ScenarioPlayer;
import be.rhea.projector.controller.server.player.StateChangedEvent;
import be.rhea.projector.controller.server.player.StateChangedListener;
import be.rhea.projector.controller.server.player.StateChangedEvent.State;
import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.ClientType;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.ui.artnet.ArtNetPreviewer;
import be.rhea.projector.controller.server.util.StatusHolder;

public class PlayerPanel extends JPanel implements ActionListener, StateChangedListener {
	private static final String STOP = "STOP";
	private static final String PAUSE = "PAUSE";
	private static final String PLAY = "PLAY:";
	private static final long serialVersionUID = 1L;
	private List<ClientPanel> clientPanels = new ArrayList<ClientPanel>();
	private List<ArtNetPreviewer> artNetPanels = new ArrayList<ArtNetPreviewer>();
	private JButton stopButton;
	private JButton pauseButton;
	private JLabel statusLabel;
	private String mediaDir;

	public PlayerPanel(Scenario scenario) {
		this.setLayout(new BorderLayout());
		if (scenario != null) {
			JPanel panel = new JPanel(); 
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			mediaDir = StatusHolder.getInstance().getMediaDir();
			mediaDir = JOptionPane.showInputDialog(this, "Please provide media directory : ", mediaDir);

			StatusHolder.getInstance().setMediaDir(mediaDir);

			JScrollPane createPlayersPanel = createPlayersPanel(scenario);
			panel.add(createPlayersPanel);
			
			JScrollPane midPanel = createPauseStopPanel();
			panel.add(midPanel);
			this.add(panel, BorderLayout.CENTER);
			
			//JScrollPane createClientPanelPreviewPanel = createClientPanelPreviewPanel(scenario);
			JToolBar createClientPanelPreviewPanel = createClientPanelPreviewToolbar(scenario);
			this.add(createClientPanelPreviewPanel, BorderLayout.PAGE_END);
			
			ScenarioPlayer.addStateChangeListener(this);
			ScenarioPlayer.setScenario(scenario);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, "No scenario available.", "Error", JOptionPane.ERROR_MESSAGE);
				}});
		}
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
		JPanel startStopPanel = new JPanel(); 
		startStopPanel.setLayout(new BoxLayout(startStopPanel, BoxLayout.Y_AXIS));
//		startStopPanel.add(Box.createRigidArea(new Dimension(0,50)));
		startStopPanel.add(Box.createVerticalGlue());

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
		pauseButton.setEnabled(false);
		stopPausePanel.add(pauseButton);
		
		
		stopPausePanel.add(Box.createRigidArea(new Dimension(40,0)));

		stopButton = new JButton(new ImageIcon(this.getClass().getResource("/stop.png")));
		stopButton.setFocusable(false);
		stopButton.setActionCommand(STOP);
		stopButton.addActionListener(this);
		stopButton.setPreferredSize(new Dimension(150,150));
		stopButton.setMinimumSize(new Dimension(150,150));
		stopButton.setMaximumSize(new Dimension(150,150));
		stopButton.setEnabled(false);
		stopPausePanel.add(stopButton);
		stopPausePanel.add(Box.createHorizontalGlue());

		startStopPanel.add(stopPausePanel);

		startStopPanel.add(Box.createRigidArea(new Dimension(0,30)));
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("");
		statusLabel.setAlignmentX(LEFT_ALIGNMENT);
		statusLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		labelPanel.add(statusLabel);
		labelPanel.setAlignmentX(CENTER_ALIGNMENT);
		startStopPanel.add(labelPanel);
		
		startStopPanel.add(Box.createVerticalGlue());
		
		JScrollPane scrollPane = new JScrollPane(startStopPanel);
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
			if (client.getType() == ClientType.PROJECTOR) {
				try {
					ClientPanel clientPanel = new ClientPanel(mediaDir, client.getPort());
					clientPanel.setPreferredSize(new Dimension(200,150));
					clientPanel.setMinimumSize(new Dimension(200,150));
					clientPanel.setMaximumSize(new Dimension(200,150));
					clientPanel.setAlignmentX(CENTER_ALIGNMENT);
					overviewClientsPanel.add(clientPanel);
					clientPanels.add(clientPanel);
				} catch (IOException e) {
					ProjectorControllerServer.showError(e);
				}
			} else if (client.getType() == ClientType.ARTNET) {
				ArtNetPreviewer artNetPreviewer;
				try {
					artNetPreviewer = new ArtNetPreviewer(client.getPort());
					artNetPreviewer.setPreferredSize(new Dimension(200,150));
					artNetPreviewer.setMinimumSize(new Dimension(200,150));
					artNetPreviewer.setMaximumSize(new Dimension(200,150));				
					artNetPreviewer.setAlignmentX(CENTER_ALIGNMENT);
					overviewClientsPanel.add(artNetPreviewer);
					artNetPanels.add(artNetPreviewer);
				} catch (IOException e) {
					ProjectorControllerServer.showError(e);
				}			
			}
		}
		JScrollPane scrollPane2 = new JScrollPane(overviewClientsPanel);
		return scrollPane2;
	}
	
	private JToolBar createClientPanelPreviewToolbar(Scenario scenario) {
		JToolBar overviewClientsToolbar = new JToolBar();
		overviewClientsToolbar.setRollover(true);
		List<Client> clients = scenario.getClients();
		for (Client client : clients) {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			JLabel label = new JLabel(client.getName());
			label.setAlignmentX(CENTER_ALIGNMENT);
			panel.add(label);
			if (client.getType() == ClientType.PROJECTOR) {
				try {
					ClientPanel clientPanel = new ClientPanel(mediaDir, client.getPort());
					clientPanel.setPreferredSize(new Dimension(200,150));
					clientPanel.setMinimumSize(new Dimension(200,150));
					clientPanel.setMaximumSize(new Dimension(200,150));
					clientPanel.setAlignmentX(CENTER_ALIGNMENT);
					panel.add(clientPanel);
					clientPanels.add(clientPanel);
				} catch (IOException e) {
					ProjectorControllerServer.showError(e);
				}
			} else if (client.getType() == ClientType.ARTNET) {
				ArtNetPreviewer artNetPreviewer;
				try {
					artNetPreviewer = new ArtNetPreviewer(client.getPort());
					artNetPreviewer.setPreferredSize(new Dimension(200,150));
					artNetPreviewer.setMinimumSize(new Dimension(200,150));
					artNetPreviewer.setMaximumSize(new Dimension(200,150));				
					artNetPreviewer.setAlignmentX(CENTER_ALIGNMENT);
					panel.add(artNetPreviewer);
					artNetPanels.add(artNetPreviewer);
				} catch (IOException e) {
					ProjectorControllerServer.showError(e);
				}			
			}
			overviewClientsToolbar.add(panel);
			overviewClientsToolbar.addSeparator(new Dimension(5,5));
		}
		return overviewClientsToolbar;
	}	
	
	public void stopMediaPanels() {
		for (ClientPanel clientPanel : clientPanels) {
			clientPanel.stopListening();
		}
		for (ArtNetPreviewer artNetPanel : artNetPanels) {
			artNetPanel.stopListening();
		}
	}
	
	private JPanel createPlayPanelForScene(Scene scene, int index) {
		JPanel playPanel = new JPanel();
		playPanel.setLayout(new BoxLayout(playPanel, BoxLayout.X_AXIS));
		playPanel.add(Box.createRigidArea(new Dimension(5,0)));
		JLabel label = new JLabel(scene.getName());
		label.setFont(new Font("Arial", Font.PLAIN, 25));
		label.setPreferredSize(new Dimension(600,50));
		label.setMinimumSize(new Dimension(100,50));
		label.setMaximumSize(new Dimension(600,50));
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
