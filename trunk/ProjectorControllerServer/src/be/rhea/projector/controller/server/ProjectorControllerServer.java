package be.rhea.projector.controller.server;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import be.rhea.projector.controller.server.filefilter.XMLFileFilter;
import be.rhea.projector.controller.server.player.ScenarioPlayer;
import be.rhea.projector.controller.server.player.StateChangedEvent;
import be.rhea.projector.controller.server.player.StateChangedListener;
import be.rhea.projector.controller.server.player.StateChangedEvent.State;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.ui.ScenarioViewer;
import be.rhea.projector.controller.server.ui.beaneditor.BeanEditor;

public class ProjectorControllerServer extends JFrame implements ActionListener, TreeSelectionListener, StateChangedListener {
	private static final String TITLE = "Projector Controller";
	private static final long serialVersionUID = 1L;
	private static final String OPEN = "OPEN";
	private static final String SAVE = "SAVE";
	private static final String SAVE_AS = "SAVE_AS";
	private static final String PLAY_SCENE = "PLAY_SCENE";
	private static final String PAUSE_SCENE = "PAUSE_SCENE";
	private static final String STOP_SCENE = "STOP_SCENE";
	private static final String NEW_SCENARIO = "NEW_SCENARIO";
	private static final String EXIT = "EXIT";
	private ScenarioViewer scenarioViewer;
	private File selectedFile;
	private JButton playButton;
	private JButton pauseButton;
	private JButton stopButton;
	private JLabel statusLabel;

	public static void main(String[] args) throws Exception {
		ProjectorControllerServer server = new ProjectorControllerServer();
		server.start();
	}

	private void start() {
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JToolBar toolbar = createToolBar();
		toolbar.setFloatable(true);
		toolbar.setRollover(true);
		panel.setLayout(new BorderLayout());
		panel.add(toolbar, BorderLayout.PAGE_END);
		JSplitPane splitpane = new JSplitPane();
		panel.add(splitpane, BorderLayout.CENTER);
		BeanEditor beanEditor = new BeanEditor();
		splitpane.setRightComponent(beanEditor);
		JScrollPane scrollPane = new JScrollPane();
		scenarioViewer = new ScenarioViewer(beanEditor);
		scenarioViewer.setSize(600, 300);
		scenarioViewer.addTreeSelectionListener(this);
		scrollPane.getViewport().add(scenarioViewer);
		splitpane.setLeftComponent(scrollPane);
		splitpane.setDividerLocation(400);
		splitpane.setResizeWeight(1);
		scenarioViewer.setModel(null);
		Container contentPane = this.getContentPane();
		contentPane.add(panel);
		
		ScenarioPlayer.addStateChangeListener(this);

		this.setSize(800, 600);
		this.setVisible(true);
	}

	private JToolBar createToolBar() {
		ImageIcon playIcon = new ImageIcon(this.getClass().getResource("/play.png"));
		ImageIcon pauseIcon = new ImageIcon(this.getClass().getResource("/pause.png"));
		ImageIcon stopIcon = new ImageIcon(this.getClass().getResource("/stop.png"));
		JToolBar toolbar = new JToolBar();
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
		toolbar.add(playButton);
		toolbar.add(pauseButton);
		toolbar.add(stopButton);
		statusLabel = new JLabel("");
		toolbar.add(statusLabel);
		return toolbar;
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newScenario = new JMenuItem("New");
		newScenario.setActionCommand(NEW_SCENARIO);
		JMenuItem open = new JMenuItem("Open...");
		open.setActionCommand(OPEN);
		JMenuItem save = new JMenuItem("Save");
		save.setActionCommand(SAVE);
		JMenuItem saveAs = new JMenuItem("Save As...");
		saveAs.setActionCommand(SAVE_AS);
		JMenuItem exit = new JMenuItem("Exit");
		exit.setActionCommand(EXIT);
		menuBar.add(fileMenu);
		fileMenu.add(newScenario);
		fileMenu.add(open);
		fileMenu.addSeparator();
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		this.setJMenuBar(menuBar);
		newScenario.addActionListener(this);
		open.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		exit.addActionListener(this);
	}

	public void actionPerformed(ActionEvent actionEvent) {
		Scenario currentScenario = scenarioViewer.getScenario();
		if (NEW_SCENARIO.equals(actionEvent.getActionCommand())) {
			scenarioViewer.setScenario(new Scenario());
			this.setTitle(TITLE + " " + "NewScenario");
		} else if (EXIT.equals(actionEvent.getActionCommand())) {
			System.exit(0);
		} else if (OPEN.equals(actionEvent.getActionCommand())) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new XMLFileFilter());
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			if (fileChooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION) {
				try {
					selectedFile = fileChooser.getSelectedFile();
					XMLDecoder decoder = new XMLDecoder(new FileInputStream(
							selectedFile));
					currentScenario = (Scenario) decoder.readObject();
					decoder.close();
					scenarioViewer.setScenario(currentScenario);
					this.setTitle(TITLE + " " + selectedFile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (PLAY_SCENE.equals(actionEvent.getActionCommand())) {
			if (scenarioViewer.getSelectedObject() instanceof Scene) {
				int indexOf = currentScenario.getScenes().indexOf(
						scenarioViewer.getSelectedObject());
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
		} else if (SAVE_AS.equals(actionEvent.getActionCommand())) {
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new XMLFileFilter());
				fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
				if (fileChooser.showDialog(this, "Save") == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChooser.getSelectedFile();
					Scenario scenario = scenarioViewer.getScenario();
					XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
							new FileOutputStream(selectedFile)));
					encoder.writeObject(scenario);
					encoder.close();
					this.setTitle(TITLE + " " + selectedFile);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (SAVE.equals(actionEvent.getActionCommand())) {
			try {
				Scenario scenario = scenarioViewer.getScenario();
				XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
						new FileOutputStream(selectedFile)));
				encoder.writeObject(scenario);
				encoder.close();
				this.setTitle(TITLE + " " + selectedFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		if (!ScenarioPlayer.isPlaying() && !ScenarioPlayer.isPaused()) {
			TreePath selectionPath = e.getNewLeadSelectionPath();
			if (selectionPath != null) {
				Object lastPathComponent = selectionPath.getLastPathComponent();
				if (lastPathComponent != null) {
					if (lastPathComponent instanceof DefaultMutableTreeNode)
						if (((DefaultMutableTreeNode) lastPathComponent).getUserObject() instanceof Scene) {
							playButton.setEnabled(true);
						} else {
							playButton.setEnabled(false);
						}
				}
			}
		}
	}

	public void stateChanged(StateChangedEvent e) {
		if (e.getNewState().equals(State.STOP)) {
			playButton.setEnabled(scenarioViewer.getSelectedObject() instanceof Scene);
			pauseButton.setEnabled(false);
			stopButton.setEnabled(false);
			statusLabel.setText("");
		} else if (e.getNewState().equals(State.PLAY)) {
			statusLabel.setText("Playing");
		} else if (e.getNewState().equals(State.PAUSE)) {
			statusLabel.setText("Paused");
		}
	}
}
