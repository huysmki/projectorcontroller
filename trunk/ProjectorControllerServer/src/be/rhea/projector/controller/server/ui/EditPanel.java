package be.rhea.projector.controller.server.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import be.rhea.projector.controller.server.player.ScenarioPlayer;
import be.rhea.projector.controller.server.player.StateChangedEvent;
import be.rhea.projector.controller.server.player.StateChangedListener;
import be.rhea.projector.controller.server.player.StateChangedEvent.State;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.ui.beaneditor.BeanEditor;
import be.rhea.projector.controller.server.ui.scenarioviewer.ScenarioViewer;

public class EditPanel extends JPanel implements StateChangedListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String PLAY_SCENE = "PLAY_SCENE";
	private static final String PAUSE_SCENE = "PAUSE_SCENE";
	private static final String STOP_SCENE = "STOP_SCENE";
	private static final String REFRESH_SCENES = "REFRESH_SCENES";
	
	private ScenarioViewer scenarioViewer;
	private JButton playButton;
	private JButton pauseButton;
	private JButton stopButton;
	private JLabel statusLabel;
	private JComboBox selectedSceneForPlayerComboBox;
	
	public EditPanel() {
		this.setLayout(new BorderLayout());
		JToolBar toolbar = createToolBar();
		toolbar.setFloatable(true);
		toolbar.setRollover(true);
		this.add(toolbar, BorderLayout.PAGE_END);
		JSplitPane splitpane = new JSplitPane();
		this.add(splitpane, BorderLayout.CENTER);
		BeanEditor beanEditor = new BeanEditor();
		splitpane.setRightComponent(beanEditor);
		JScrollPane scrollPane = new JScrollPane();
		scenarioViewer = new ScenarioViewer(beanEditor);
		scenarioViewer.setSize(600, 300);
		scrollPane.getViewport().add(scenarioViewer);
		splitpane.setLeftComponent(scrollPane);
		splitpane.setDividerLocation(400);
		splitpane.setResizeWeight(1);
		scenarioViewer.setModel(null);

		ScenarioPlayer.addStateChangeListener(this);
	}
	
	private JToolBar createToolBar() {
		ImageIcon playIcon = new ImageIcon(this.getClass().getResource("/play.png"));
		ImageIcon refreshIcon = new ImageIcon(this.getClass().getResource("/refresh.png"));
		ImageIcon pauseIcon = new ImageIcon(this.getClass().getResource("/pause.png"));
		ImageIcon stopIcon = new ImageIcon(this.getClass().getResource("/stop.png"));
		JToolBar toolbar = new JToolBar();
		JButton refreshButton = new JButton();
		refreshButton.setIcon(refreshIcon);
		refreshButton.setActionCommand(REFRESH_SCENES);
		refreshButton.addActionListener(this);
		playButton = new JButton();
		playButton.setIcon(playIcon);
		playButton.setActionCommand(PLAY_SCENE);
		playButton.addActionListener(this);
		playButton.setEnabled(false);
		pauseButton = new JButton();
		pauseButton.setIcon(pauseIcon);
		pauseButton.setActionCommand(PAUSE_SCENE);
		pauseButton.addActionListener(this);
		pauseButton.setEnabled(false);
		stopButton = new JButton();
		stopButton.setIcon(stopIcon);
		stopButton.setActionCommand(STOP_SCENE);
		stopButton.addActionListener(this);
		stopButton.setEnabled(false);
		toolbar.add(new JLabel("Scene : "));
		selectedSceneForPlayerComboBox = new JComboBox();
		selectedSceneForPlayerComboBox.setMaximumSize(new Dimension(200,25));
		toolbar.add(selectedSceneForPlayerComboBox);
		toolbar.add(refreshButton);
		toolbar.addSeparator(new Dimension(10,10));
		toolbar.add(playButton);
		toolbar.add(pauseButton);
		toolbar.addSeparator(new Dimension(10,10));
		toolbar.add(stopButton);
		toolbar.addSeparator(new Dimension(10,10));
		statusLabel = new JLabel("");
		toolbar.add(statusLabel);
		return toolbar;
	}

	public void stateChanged(StateChangedEvent e) {
		if (e.getNewState().equals(State.STOP)) {
			playButton.setEnabled(true);
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
	
	private void populateSelectedSceneForPlayerComboBox() {
		selectedSceneForPlayerComboBox.removeAllItems();
		Scenario scenario = scenarioViewer.getScenario();
		if (scenario != null) {
			List<Scene> scenes = scenario.getScenes();
			playButton.setEnabled(scenes.size() > 0);
			for (Iterator<Scene> iterator = scenes.iterator(); iterator.hasNext();) {
				Scene scene = iterator.next();
				selectedSceneForPlayerComboBox.addItem(scene);
			}
		}
	}

	public void actionPerformed(ActionEvent actionEvent) {
		Scenario currentScenario = scenarioViewer.getScenario();
		if (PLAY_SCENE.equals(actionEvent.getActionCommand())) {
				int indexOf = selectedSceneForPlayerComboBox.getSelectedIndex();
				if (indexOf >= 0) {
					ScenarioPlayer.setScenario(currentScenario);
					ScenarioPlayer.play(indexOf);
					playButton.setEnabled(false);
					pauseButton.setEnabled(true);
					stopButton.setEnabled(true);
				}
		} else if (PAUSE_SCENE.equals(actionEvent.getActionCommand())) {
				ScenarioPlayer.pause();
		} else if (STOP_SCENE.equals(actionEvent.getActionCommand())) {
				ScenarioPlayer.stop();
				playButton.setEnabled(true);
				pauseButton.setEnabled(false);
				stopButton.setEnabled(false);				
		} else if (REFRESH_SCENES.equals(actionEvent.getActionCommand())) {
				populateSelectedSceneForPlayerComboBox();
		}
	}
	
	public Scenario getScenario() {
		return scenarioViewer.getScenario();
	}
	
	public void setScenario(Scenario scenario) {
		scenarioViewer.setScenario(scenario);
		populateSelectedSceneForPlayerComboBox();
	}

	
}
