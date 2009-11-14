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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import be.rhea.projector.controller.server.player.ScenarioPlayer;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.ui.ScenarioViewer;
import be.rhea.projector.controller.server.ui.beaneditor.BeanEditor;

// http://www.eclipse.org/swt/snippets/
// http://www.cs.umanitoba.ca/~eclipse/2-Basic.pdf
public class ProjectorControllerServer extends JFrame implements ActionListener {
	private static final String TITLE = "Projector Controller";
	private static final long serialVersionUID = 1L;
	private JMenuItem open;
	private JMenuItem play;
	private JMenuItem save;
	private ScenarioViewer scenarioViewer;
	private Scenario currentScenario;
	private File selectedFile;

	public static void main(String[] args) throws Exception {
		ProjectorControllerServer server = new ProjectorControllerServer();
		server.start();
	}

	private void start() {
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		JSplitPane splitpane = new JSplitPane();
		contentPane.add(splitpane, BorderLayout.CENTER);
		final BeanEditor beanEditor = new BeanEditor();
		splitpane.setRightComponent(beanEditor);
		JScrollPane scrollPane = new JScrollPane();
		scenarioViewer = new ScenarioViewer(beanEditor);
		scenarioViewer.setSize(600, 300);
		scrollPane.getViewport().add(scenarioViewer);
		splitpane.setLeftComponent(scrollPane);
		splitpane.setDividerLocation(400);
		splitpane.setResizeWeight(1);
		scenarioViewer.setModel(null);
		// ScenarioTree.setScenario(scenario1);
		this.setSize(800, 600);
		this.setVisible(true);
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu playMenu = new JMenu("Play");
		open = new JMenuItem("Open...");
		save = new JMenuItem("Save");
		play = new JMenuItem("Play Scenario");
		menuBar.add(fileMenu);
		fileMenu.add(open);
		fileMenu.add(save);
		playMenu.add(play);
		menuBar.add(playMenu);
		this.setJMenuBar(menuBar);
		open.addActionListener(this);
		play.addActionListener(this);
		save.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource() == open) {
			JFileChooser fileChooser = new JFileChooser();
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
		} else if (actionEvent.getSource() == play) {
			if (scenarioViewer.getSelectedObject() instanceof Scene) {
				int indexOf = currentScenario.getScenes().indexOf(
						scenarioViewer.getSelectedObject());
				System.out.println(indexOf);
				ScenarioPlayer player = new ScenarioPlayer(currentScenario);
				player.play(indexOf);
			}
		} else if (actionEvent.getSource() == save) {
			try {
				Scenario scenario = scenarioViewer.getScenario();
				XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
						new FileOutputStream(new File("c:/temp/Scenario2.xml"))));
				encoder.writeObject(scenario);
				encoder.close();
				System.out.println(scenario);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
