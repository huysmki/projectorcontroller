package be.rhea.projector.controller.server;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.jdesktop.swingx.JXErrorDialog;

import be.rhea.projector.controller.server.filefilter.XMLFileFilter;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.ui.EditPanel;
import be.rhea.projector.controller.server.ui.PlayerPanel;
import be.rhea.projector.controller.server.util.LimitedSet;
import be.rhea.projector.controller.server.util.StatusHolder;

public class ProjectorControllerServer extends JFrame implements ActionListener, WindowListener, MenuListener {
	private static final String PLAYER_MODE = "PLAYER_MODE";
	private static final String EDITOR_MODE = "EDITOR_MODE";
	private static final String TITLE = "Projector Controller";
	private static final long serialVersionUID = 1L;
	private static final String OPEN = "OPEN";
	private static final String SAVE = "SAVE";
	private static final String SAVE_AS = "SAVE_AS";
	private static final String NEW_SCENARIO = "NEW_SCENARIO";
	private static final String EXIT = "EXIT";
	private static final String LRU = "LRU|";
	private static JFileChooser fileChooser;
	private File selectedFile;
	private EditPanel editPanel;
	private PlayerPanel playerPanel;
	private JMenu fileMenu;
	private JMenu modeMenu;
	private JRadioButtonMenuItem playerModeMenu;
	private JMenu recentlyUsed;

	public static void main(String[] args) throws Exception {
		ProjectorControllerServer server = new ProjectorControllerServer();
		server.start();
	}
	
	public static void showError(Exception e) {
		JXErrorDialog.showDialog(null, "Unexpected error occured", e);
	}

	private void start() {
		try {
			
			//UIManager.setLookAndFeel(LookAndFeelAddons.getBestMatchAddonClassName());
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);		
		} catch (Exception e) {
			ProjectorControllerServer.showError(e);
		}
		fileChooser = new JFileChooser();
		File lastAccessedDir = StatusHolder.getInstance().getLastAccessedDir();
		if (lastAccessedDir != null) {
			fileChooser.setCurrentDirectory(lastAccessedDir);
		}
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(this);
		createMenu();
		
		Container contentPane = this.getContentPane();
		editPanel = new EditPanel();
		contentPane.add(editPanel);

		this.setSize(1000, 600);
		this.setVisible(true);
	}


	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		JMenuItem newScenario = new JMenuItem("New");
		newScenario.setActionCommand(NEW_SCENARIO);
		JMenuItem open = new JMenuItem("Open...");
		open.setActionCommand(OPEN);
		recentlyUsed = new JMenu("Recently Used");
		recentlyUsed.addMenuListener(this);
		JMenuItem save = new JMenuItem("Save");
		save.setActionCommand(SAVE);
		JMenuItem saveAs = new JMenuItem("Save As...");
		saveAs.setActionCommand(SAVE_AS);
		JMenuItem exit = new JMenuItem("Exit");
		exit.setActionCommand(EXIT);
		menuBar.add(fileMenu);
		fileMenu.add(newScenario);
		fileMenu.add(open);
		fileMenu.add(recentlyUsed);
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
		modeMenu = new JMenu("Mode");
		modeMenu.addMenuListener(this);
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem editModeMenu = new JRadioButtonMenuItem("Editor");
		group.add(editModeMenu);
		editModeMenu.setSelected(true);
		editModeMenu.setActionCommand(EDITOR_MODE);
		editModeMenu.addActionListener(this);
		playerModeMenu = new JRadioButtonMenuItem("Player");
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
			askForSavingScenario();
			editPanel.setScenario(new Scenario());
			this.setTitle(TITLE + " " + "NewScenario");
			selectedFile = null;
		} else if (EXIT.equals(actionEvent.getActionCommand())) {
			windowClosing(null);
			StatusHolder.saveSettings();
			System.exit(0);
		} else if (OPEN.equals(actionEvent.getActionCommand())) {
			askForSavingScenario();
			fileChooser.setFileFilter(new XMLFileFilter());
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			if (fileChooser.showDialog(this, "Open") == JFileChooser.APPROVE_OPTION) {
				try {
					selectedFile = fileChooser.getSelectedFile();
					XMLDecoder decoder = new XMLDecoder(new FileInputStream(
							selectedFile));
					currentScenario = (Scenario) decoder.readObject();
					decoder.close();
					editPanel.setScenario(currentScenario);
					this.setTitle(TITLE + " " + selectedFile);
					StatusHolder statusHolder = StatusHolder.getInstance();
					statusHolder.addRecentlyUsedFile(selectedFile);
					statusHolder.setLastAccessedDir(selectedFile.getParentFile());
				} catch (FileNotFoundException e) {
					ProjectorControllerServer.showError(e);
				}
			}
		} else if (SAVE_AS.equals(actionEvent.getActionCommand())) {
			File cachedFile = selectedFile;
			selectedFile = null;
			saveScenario();
			if (selectedFile == null) {
				selectedFile = cachedFile;
				this.setTitle(TITLE + " " + selectedFile);
			}
		} else if (SAVE.equals(actionEvent.getActionCommand())) {
			saveScenario();
		} else if (EDITOR_MODE.equals(actionEvent.getActionCommand())) {
			if (playerPanel != null) {
				playerPanel.stopMediaPanels();
				playerPanel = null;
			}
			this.getContentPane().removeAll();
			this.getContentPane().add(editPanel);
			SwingUtilities.updateComponentTreeUI(this);
		} else if (PLAYER_MODE.equals(actionEvent.getActionCommand())) {
			this.getContentPane().removeAll();
			playerPanel = new PlayerPanel(editPanel.getScenario());
			this.getContentPane().add(playerPanel);
			SwingUtilities.updateComponentTreeUI(this);
		} else if (actionEvent.getActionCommand().startsWith(LRU)) {
			askForSavingScenario();
			try {
				String fileAsString = actionEvent.getActionCommand().split("\\|")[1];
				selectedFile = new File(fileAsString);
				XMLDecoder decoder  = new XMLDecoder(new FileInputStream(
							selectedFile));
				currentScenario = (Scenario) decoder.readObject();
				decoder.close();
				editPanel.setScenario(currentScenario);
				this.setTitle(TITLE + " " + selectedFile);
				StatusHolder statusHolder = StatusHolder.getInstance();
				statusHolder.addRecentlyUsedFile(selectedFile);
			statusHolder.setLastAccessedDir(selectedFile.getParentFile());
			} catch (FileNotFoundException e) {
				ProjectorControllerServer.showError(e);
			}
		}
	}

	private void saveScenario() {
		Scenario scenario = editPanel.getScenario();
		if (scenario != null) {
			if (selectedFile == null) {
				try {
					fileChooser.setFileFilter(new XMLFileFilter());
					fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
					if (fileChooser.showDialog(this, "Save") == JFileChooser.APPROVE_OPTION) {
						selectedFile = fileChooser.getSelectedFile();
						XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
								new FileOutputStream(selectedFile)));
						encoder.writeObject(scenario);
						encoder.close();
						this.setTitle(TITLE + " " + selectedFile);
						StatusHolder statusHolder = StatusHolder.getInstance();
						statusHolder.addRecentlyUsedFile(selectedFile);
						statusHolder.setLastAccessedDir(selectedFile.getParentFile());
					}
				} catch (FileNotFoundException e) {
					ProjectorControllerServer.showError(e);
				}
			} else {
				try {
					XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
							new FileOutputStream(selectedFile)));
					encoder.writeObject(scenario);
					encoder.close();
					this.setTitle(TITLE + " " + selectedFile);
				} catch (FileNotFoundException e) {
					ProjectorControllerServer.showError(e);
				}
			}
		}
	}

	public void windowActivated(WindowEvent windowEvent) {
	}

	public void windowClosed(WindowEvent windowEvent) {
	}

	public void windowClosing(WindowEvent windowEvent) {
		askForSavingScenario();
		StatusHolder.saveSettings();
	}

	private void askForSavingScenario() {
		boolean shouldSave = false;
		Scenario currentScenario = editPanel.getScenario();
		if (currentScenario != null) {
			if (selectedFile == null) {
				shouldSave = true;
			} else {
				try {
					XMLDecoder decoder = new XMLDecoder(new FileInputStream(
							selectedFile));
					Scenario originalScenario = (Scenario) decoder.readObject();
					decoder.close();	
					if (!currentScenario.equals(originalScenario)) {
						shouldSave = true;
					}
				} catch (FileNotFoundException e) {
					ProjectorControllerServer.showError(e);
				}
				
			}
		}		
		if (shouldSave) {
			if (JOptionPane.showConfirmDialog(this, "Save scenario?", "Save scenario?",  JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				saveScenario();
			}
		}
	}

	public void windowDeactivated(WindowEvent windowEvent) {
	}

	public void windowDeiconified(WindowEvent windowEvent) {
	}

	public void windowIconified(WindowEvent windowEvent) {
	}

	public void windowOpened(WindowEvent windowEvent) {
	}

	public void menuCanceled(MenuEvent menuevent) {
	}

	public void menuDeselected(MenuEvent menuevent) {
	}

	public void menuSelected(MenuEvent menuevent) {
		if (recentlyUsed.equals(menuevent.getSource())) {
			LimitedSet<File> recentlyUsedFiles = StatusHolder.getInstance().getRecentlyUsedFiles();
			File[] recentlyUsedFilesAsArray = (File[]) recentlyUsedFiles.toArray(new File[]{});
			
			recentlyUsed.removeAll();
			for (int i = recentlyUsedFilesAsArray.length; i > 0; i--) {
				File file = recentlyUsedFilesAsArray[i - 1];
				JMenuItem ruMenuItem = new JMenuItem(file.toString());
				ruMenuItem.setActionCommand(LRU + file);
				ruMenuItem.addActionListener(this);
				recentlyUsed.add(ruMenuItem);
			
			}
		} else if (modeMenu.equals(menuevent.getSource())) {
			playerModeMenu.setEnabled(editPanel.getScenario() != null);
		} 
		
	}

}
