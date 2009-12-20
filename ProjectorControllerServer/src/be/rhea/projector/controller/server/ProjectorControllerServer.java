package be.rhea.projector.controller.server;

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

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import be.rhea.projector.controller.server.filefilter.XMLFileFilter;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.ui.EditPanel;
import be.rhea.projector.controller.server.ui.PlayerPanel;

public class ProjectorControllerServer extends JFrame implements ActionListener {
	private static final String PLAYER_MODE = "PLAYER_MODE";
	private static final String EDITOR_MODE = "EDITOR_MODE";
	private static final String TITLE = "Projector Controller";
	private static final long serialVersionUID = 1L;
	private static final String OPEN = "OPEN";
	private static final String SAVE = "SAVE";
	private static final String SAVE_AS = "SAVE_AS";
	private static final String NEW_SCENARIO = "NEW_SCENARIO";
	private static final String EXIT = "EXIT";
	private static JFileChooser fileChooser;
	private File selectedFile;
	private EditPanel editPanel;

	public static void main(String[] args) throws Exception {
		ProjectorControllerServer server = new ProjectorControllerServer();
		server.start();
	}

	private void start() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileChooser = new JFileChooser();
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		
		Container contentPane = this.getContentPane();
		editPanel = new EditPanel();
		contentPane.add(editPanel);

		this.setSize(800, 600);
		this.setVisible(true);
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
		JMenu modeMenu = new JMenu("Mode");
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem editModeMenu = new JRadioButtonMenuItem("Editor");
		group.add(editModeMenu);
		editModeMenu.setSelected(true);
		editModeMenu.setActionCommand(EDITOR_MODE);
		editModeMenu.addActionListener(this);
		JRadioButtonMenuItem playerModeMenu = new JRadioButtonMenuItem("Player");
		group.add(playerModeMenu);
		playerModeMenu.setActionCommand(PLAYER_MODE);
		playerModeMenu.addActionListener(this);
		modeMenu.add(editModeMenu);
		modeMenu.add(playerModeMenu);
		menuBar.add(modeMenu);
		
	}

	public void actionPerformed(ActionEvent actionEvent) {
		Scenario currentScenario = editPanel.getScenario();
		if (NEW_SCENARIO.equals(actionEvent.getActionCommand())) {
			editPanel.setScenario(new Scenario());
			this.setTitle(TITLE + " " + "NewScenario");
		} else if (EXIT.equals(actionEvent.getActionCommand())) {
			System.exit(0);
		} else if (OPEN.equals(actionEvent.getActionCommand())) {
			fileChooser.setFileFilter(new XMLFileFilter());
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			if (fileChooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION) {
				try {
					selectedFile = fileChooser.getSelectedFile();
					XMLDecoder decoder = new XMLDecoder(new FileInputStream(
							selectedFile));
					currentScenario = (Scenario) decoder.readObject();
					decoder.close();
					editPanel.setScenario(currentScenario);
					this.setTitle(TITLE + " " + selectedFile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (SAVE_AS.equals(actionEvent.getActionCommand())) {
			try {
				fileChooser.setFileFilter(new XMLFileFilter());
				fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
				if (fileChooser.showDialog(this, "Save") == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChooser.getSelectedFile();
					Scenario scenario = editPanel.getScenario();
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
				Scenario scenario = editPanel.getScenario();
				XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
						new FileOutputStream(selectedFile)));
				encoder.writeObject(scenario);
				encoder.close();
				this.setTitle(TITLE + " " + selectedFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (EDITOR_MODE.equals(actionEvent.getActionCommand())) {
			this.getContentPane().removeAll();
			this.getContentPane().add(editPanel);
			SwingUtilities.updateComponentTreeUI(this);
		} else if (PLAYER_MODE.equals(actionEvent.getActionCommand())) {
			this.getContentPane().removeAll();
			this.getContentPane().add(new PlayerPanel(editPanel.getScenario()));
			SwingUtilities.updateComponentTreeUI(this);
		}
	}

}
