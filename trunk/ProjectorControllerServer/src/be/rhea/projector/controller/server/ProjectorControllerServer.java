package be.rhea.projector.controller.server;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import be.rhea.projector.controller.remote.commands.client.PCPMediaTarFileTransferClientCommand;
import be.rhea.projector.controller.server.player.ScenarioPlayer;
import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.scenario.ScenePart;
import be.rhea.projector.controller.server.scenario.actions.AbstractAction;
import be.rhea.projector.controller.server.scenario.actions.ColorAction;
import be.rhea.projector.controller.server.scenario.actions.LoadImageAction;
import be.rhea.projector.controller.server.scenario.actions.LoadVideoAction;
import be.rhea.projector.controller.server.scenario.actions.ManualAcknownledgeAction;
import be.rhea.projector.controller.server.scenario.actions.PlayImageAction;
import be.rhea.projector.controller.server.scenario.actions.PlayVideoAction;
import be.rhea.projector.controller.server.scenario.actions.SleepAction;
import be.rhea.projector.controller.server.scenario.actions.StopVideoAction;
import be.rhea.projector.controller.server.scenario.actions.TransitionColorAction;
import be.rhea.projector.controller.server.ui.BeanEditor;
import be.rhea.remote.PCP;
import be.rhea.remote.client.SimpleProtocolTCPClient;


// http://www.eclipse.org/swt/snippets/
// http://www.cs.umanitoba.ca/~eclipse/2-Basic.pdf
public class ProjectorControllerServer {
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws Exception {
		
	  Client client1 = new Client("Client1", "localhost", 9000);
	  Client client2 = new Client("Client2", "localhost", 9001);
	  Client client3 = new Client("Client3", "localhost", 9002);
	  final Scenario scenario1 = new Scenario("Demo 2");
	  scenario1.addClient(1, client1);
	  scenario1.addClient(2, client2);
	  scenario1.addClient(3, client3);
	  
	  scenario1.addScene(new Scene("Intro").addScenePart(new ScenePart("Scene 1").addAction(new ColorAction("wit",1, new Color(255,255,255))).addAction(new ColorAction("rood",2, new Color(255,0,0)))));
	  scenario1.addScene(new Scene("Intro").addScenePart(new ScenePart("Scene 2")));
	  scenario1.addScene(new Scene("Intro").addScenePart(new ScenePart("Scene 3")));
	  Scene scene4 = new Scene("Scene 4");
	  ScenePart scene4Part1 = new ScenePart("Intro");
	  scene4.addScenePart(scene4Part1);
	  
	  scene4Part1.addAction(new ColorAction("rood", 1, new Color(255,0,0)));
	  scene4Part1.addAction(new ColorAction("blauw", 2, new Color(0,0,255)));
	  scene4Part1.addAction(new ColorAction("groen", 3, new Color(0,255,0)));
	  scene4Part1.addAction(new SleepAction("Slaap 2 sec", 2000));
	  scene4Part1.addAction(new ColorAction("blauw", 1, new Color(0,0,255)));
	  scene4Part1.addAction(new ColorAction("groen", 2, new Color(0,255,0)));
	  scene4Part1.addAction(new ColorAction("rood", 3, new Color(255,0,0)));
	  scene4Part1.addAction(new SleepAction("Slaap 2 sec", 2000));
	  scene4Part1.addAction(new ColorAction("groen", 1, new Color(0,255,0)));
	  scene4Part1.addAction(new ColorAction("rood", 2, new Color(255,0,0)));
	  scene4Part1.addAction(new ColorAction("blauw", 3, new Color(0,0,255)));
	  scene4Part1.addAction(new SleepAction("Slaap 2 sec", 2000));
	  
	  ScenePart scene4Part2 = new ScenePart("Part 1");
	  scene4.addScenePart(scene4Part2);
	  
	  scene4Part2.addAction(new TransitionColorAction("groen naar blauw", 1, new Color(0,255,0), new Color(0,0,255), 4000));
	  scene4Part2.addAction(new TransitionColorAction("rood naar groen", 2, new Color(255,0,0), new Color(0,255,0), 4000));
	  scene4Part2.addAction(new TransitionColorAction("blauw naar rood", 3, new Color(0,0,255), new Color(255,0,0), 4000));
	  scene4Part2.addAction(new SleepAction("Slaap 4 sec", 4000));
	  scene4Part2.addAction(new LoadImageAction("ocean10.jpg", 1, "ocean10.jpg"));
	  scene4Part2.addAction(new LoadImageAction("ocean20.jpg", 2, "ocean20.jpg"));
	  scene4Part2.addAction(new LoadImageAction("ocean30.jpg", 3, "ocean30.jpg"));
	  scene4Part2.addAction(new ManualAcknownledgeAction("Manueel"));
	  scene4Part2.addAction(new PlayImageAction("play ocean10.jpg", 1));
	  scene4Part2.addAction(new PlayImageAction("play ocean20.jpg", 2));
	  scene4Part2.addAction(new PlayImageAction("play ocean30.jpg", 3));
	  scenario1.addScene(scene4);

	  ScenePart scene4Part3 = new ScenePart("Video");
	  scene4.addScenePart(scene4Part3);
	  scene4Part3.addAction(new LoadVideoAction("8pounder.mpg", 1, "8pounder.mpg"));
	  scene4Part3.addAction(new LoadVideoAction("bailey.mpg", 2, "bailey.mpg"));
	  scene4Part3.addAction(new LoadVideoAction("toycom13.mpg", 3, "toycom13.mpg"));
	  scene4Part3.addAction(new SleepAction("Slaap 3 sec", 3000));
	  scene4Part3.addAction(new PlayVideoAction("play video", 1));
	  scene4Part3.addAction(new PlayVideoAction("play video", 2));
	  scene4Part3.addAction(new PlayVideoAction("play video", 3));
	  scene4Part3.addAction(new ManualAcknownledgeAction("Manueel"));
	  scene4Part3.addAction(new StopVideoAction("stop video", 1));
	  scene4Part3.addAction(new StopVideoAction("stop video", 2));
	  scene4Part3.addAction(new StopVideoAction("stop video", 3));
	  
	  XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
			  	new FileOutputStream(new File("c:/temp/Scenario1.xml"))));
	  
	  encoder.writeObject(scenario1);
	  encoder.close();
//		
//	  XMLDecoder decoder = new XMLDecoder(new FileInputStream(new File("c:/temp/Scenario1.xml")));
//	  final Scenario scenario1 = (Scenario) decoder.readObject();
//	  decoder.close();
		
	  JFrame display = new JFrame();
	  display.setTitle("Projector Controller");
	  display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  
	  JPanel buttonPanel = new JPanel();
	  buttonPanel.setLayout(new GridLayout(1, 4));
	  JButton button1 = new JButton();
	  button1.setText("Demo 1");
	  JButton button2 = new JButton();
	  button2.setText("Demo 2");
	  JButton button3 = new JButton();
	  button3.setText("Upload Media");
	  JButton button4 = new JButton();
	  button4.setText("Play Scene");
	  buttonPanel.add(button1);
	  buttonPanel.add(button2);
	  buttonPanel.add(button3);
	  buttonPanel.add(button4);
	  
	  Container contentPane = display.getContentPane();
	  contentPane.setLayout(new BorderLayout());
	  contentPane.add(buttonPanel, BorderLayout.NORTH);
	  
	  JSplitPane splitpane = new JSplitPane();
	  contentPane.add(splitpane, BorderLayout.CENTER);
	  
	  JScrollPane scrollPane = new JScrollPane();
	  final JTree tree = new JTree();
	  tree.setSize(600, 300);
	  scrollPane.getViewport().add(tree);
	  tree.setScrollsOnExpand(true);
	  splitpane.setLeftComponent(scrollPane);
	  splitpane.setDividerLocation(400);
	  
	  final BeanEditor beanEditor = new BeanEditor();
	  splitpane.setRightComponent(beanEditor);
	  
	  tree.addMouseListener(new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent event) {
			if (event.getButton() == MouseEvent.BUTTON1) {
				TreePath selectionPath = tree.getSelectionPath();
				if (selectionPath != null) {
					Object lastPathComponent = selectionPath.getLastPathComponent();
					if (lastPathComponent != null) {
						if (lastPathComponent instanceof DefaultMutableTreeNode) {
							DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) lastPathComponent;
							beanEditor.setObject(treeNode.getUserObject());
						}
					}
				}
			}
			
		}

	  });
	  DefaultMutableTreeNode scenarioItem = new DefaultMutableTreeNode(scenario1);

	  DefaultMutableTreeNode clientsItem = new DefaultMutableTreeNode("Clients");
	  scenarioItem.add(clientsItem);
	  Set<Integer> clientKeys = scenario1.getClients().keySet();
	  for (Integer clientId : clientKeys) {
		  DefaultMutableTreeNode newClientItem = new DefaultMutableTreeNode(scenario1.getClients().get(clientId));
		  clientsItem.add(newClientItem);
	}

	  DefaultMutableTreeNode sceneItem = new DefaultMutableTreeNode("Scenes");
	  scenarioItem.add(sceneItem);

		List<Scene> scenes = scenario1.getScenes();
		for (Scene scene: scenes) {
			DefaultMutableTreeNode newSceneItem = new DefaultMutableTreeNode(scene);
			sceneItem.add(newSceneItem);
			List<ScenePart> sceneParts = scene.getSceneParts();
			for (ScenePart scenePart : sceneParts) {
				DefaultMutableTreeNode newScenePartItem = new DefaultMutableTreeNode(scenePart);
				newSceneItem.add(newScenePartItem);
				
				List<AbstractAction> actions = scenePart.getActions();
				for (AbstractAction action : actions) {
					DefaultMutableTreeNode newActionItem = new DefaultMutableTreeNode(action);
					newScenePartItem.add(newActionItem);
				}
			}
		}

	  tree.setModel(new DefaultTreeModel(scenarioItem));
	  
	  button1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
			  try {
				  SimpleProtocolTCPClient client1 = new SimpleProtocolTCPClient("localhost", 9000, PCP.PROTOCOL);
				  SimpleProtocolTCPClient client2 = new SimpleProtocolTCPClient("localhost", 9001, PCP.PROTOCOL);
				  SimpleProtocolTCPClient client3 = new SimpleProtocolTCPClient("localhost", 9002, PCP.PROTOCOL);
				  client1.connect();
				  client2.connect();
				  client3.connect();
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean1.jpg"}));
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean2.jpg"}));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","0","0", "0","255","0", "6000"}));
				  Thread.sleep(2000);
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean3.jpg"}));
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  Thread.sleep(2000);
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean4.jpg"}));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  Thread.sleep(2000);
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean5.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean6.jpg"}));
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","255","0", "0","0","255", "6000"}));
				  Thread.sleep(3000);
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean7.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean8.jpg"}));
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  Thread.sleep(3000);
				  
				  System.out.println(client1.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","255","0", "255","255","255", "5000"}));
				  System.out.println(client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","255", "255","0","0","255", "5000"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean8.jpg"}));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  Thread.sleep(2000);
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean6.jpg"}));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  Thread.sleep(2000);
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean4.jpg"}));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  Thread.sleep(2000);
				  System.out.println(client1.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"bailey.mpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"8pounder.mpg"}));
				  System.out.println(client1.sendCommand(PCP.START_VIDEO_MEDIA));
				  System.out.println(client2.sendCommand(PCP.START_VIDEO_MEDIA));
				  System.out.println(client3.sendCommand(PCP.START_COLOR, new String[] {"255","0", "0"}));
				  Thread.sleep(3000);
				  System.out.println(client3.sendCommand(PCP.START_COLOR, new String[] {"255","255", "0"}));
				  Thread.sleep(3000);
				  System.out.println(client3.sendCommand(PCP.START_COLOR, new String[] {"255","0", "255"}));
				  Thread.sleep(3000);
				  System.out.println(client1.sendCommand(PCP.STOP_VIDEO_MEDIA));
				  System.out.println(client1.sendCommand(PCP.START_COLOR, new String[] {"50","100", "20"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"toycom13.mpg"}));
				  System.out.println(client3.sendCommand(PCP.START_VIDEO_MEDIA));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_COLOR, new String[] {"50","10", "220"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_COLOR, new String[] {"250","100", "120"}));
				  Thread.sleep(1000);
				  System.out.println(client2.sendCommand(PCP.STOP_VIDEO_MEDIA));
				  System.out.println(client1.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"8pounder.mpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"bailey.mpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_VIDEO_MEDIA));
				  System.out.println(client2.sendCommand(PCP.START_VIDEO_MEDIA));
				  Thread.sleep(10000);
				  System.out.println(client1.sendCommand(PCP.STOP_VIDEO_MEDIA));
				  System.out.println(client2.sendCommand(PCP.STOP_VIDEO_MEDIA));
				  System.out.println(client3.sendCommand(PCP.STOP_VIDEO_MEDIA));
				  System.out.println(client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","0","0", "0","0","255", "5000"}));
				  System.out.println(client1.sendCommand(PCP.START_COLOR, new String[] {"255","255", "255"}));
				  
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_COLOR, new String[] {"255","0", "0"}));
				  System.out.println(client3.sendCommand(PCP.START_COLOR, new String[] {"255","0", "0"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_COLOR, new String[] {"0","255", "0"}));
				  System.out.println(client3.sendCommand(PCP.START_COLOR, new String[] {"0","255", "0"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_COLOR, new String[] {"0","0", "255"}));
				  System.out.println(client3.sendCommand(PCP.START_COLOR, new String[] {"0","0", "255"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_COLOR, new String[] {"255","0", "255"}));
				  System.out.println(client3.sendCommand(PCP.START_COLOR, new String[] {"255","0", "255"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_COLOR, new String[] {"0","255", "255"}));
				  System.out.println(client3.sendCommand(PCP.START_COLOR, new String[] {"0","255", "255"}));
				  System.out.println(client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","0","255", "255","0","0", "10000"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_COLOR, new String[] {"255","0", "255"}));
				  System.out.println(client3.sendCommand(PCP.START_COLOR, new String[] {"255","0", "255"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","255","255", "255","0","255", "8000"}));
				  System.out.println(client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","0","255", "255","0","0", "8000"}));
				  Thread.sleep(8000);
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  Thread.sleep(1000);
				  System.out.println(client1.sendCommand(PCP.START_IMAGE));
				  System.out.println(client2.sendCommand(PCP.START_IMAGE));
				  System.out.println(client3.sendCommand(PCP.START_IMAGE));
				  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
				  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
				  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
	
				  client1.disconnect();
				  client2.disconnect();
				  client3.disconnect();		
			  } catch (Exception e) {
				  e.printStackTrace();
			  }

				
			}});
	  
	  button2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				  try {
					  SimpleProtocolTCPClient client1 = new SimpleProtocolTCPClient("localhost", 9000, PCP.PROTOCOL);
					  SimpleProtocolTCPClient client2 = new SimpleProtocolTCPClient("localhost", 9001, PCP.PROTOCOL);
					  SimpleProtocolTCPClient client3 = new SimpleProtocolTCPClient("localhost", 9002, PCP.PROTOCOL);
					  client1.connect();
					  client2.connect();
					  client3.connect();

					  client1.sendCommand(PCP.START_COLOR, new String[] {"255","255","255"});
					  client2.sendCommand(PCP.START_COLOR, new String[] {"255","255","255"});
					  client3.sendCommand(PCP.START_COLOR, new String[] {"255","255","255"});
					  Thread.sleep(5000);
					  client1.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","255","255", "255","0","0", "4000"});
					  client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","255","255", "255","0","0", "4000"});
					  client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","255","255", "255","0","0", "4000"});
					  Thread.sleep(4000);
					  client1.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","0","0", "0","255","0", "4000"});
					  client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","0","0", "0","255","0", "4000"});
					  client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","0","0", "0","255","0", "4000"});
					  Thread.sleep(4000);
					  client1.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","255","0", "0","0","255", "4000"});
					  client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","255","0", "0","0","255", "4000"});
					  client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","255","0", "0","0","255", "4000"});
					  Thread.sleep(4000);
					  client1.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","0","255", "0","0","255", "4000"});
					  client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","0","255", "0","255","0", "4000"});
					  client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","0","255", "255","0","0", "4000"});
					  Thread.sleep(4000);
					  client1.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","0","255", "255","0","0", "4000"});
					  client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","255","0", "0","0","255", "4000"});
					  client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","0","0", "0","255","0", "4000"});
					  Thread.sleep(4000);
					  client1.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","0","0", "0","255","0", "4000"});
					  client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","0","255", "255","0","0", "4000"});
					  client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","255","0", "0","0","255", "4000"});
					  Thread.sleep(4000);					  
					  client1.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","255","0", "128","128","128", "4000"});
					  client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"255","0","0", "128","128","128", "4000"});
					  client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"0","0","255", "128","128","128", "4000"});
					  Thread.sleep(4000);					  
					  client1.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"128","128","128", "0","0","0", "4000"});
					  client2.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"128","128","128", "0","0","0", "4000"});
					  client3.sendCommand(PCP.START_TRANSITION_COLOR, new String[] {"128","128","128", "0","0","0", "4000"});
					  Thread.sleep(4000);					  
					  client1.sendCommand(PCP.START_COLOR, new String[] {"255","10","10"});
					  client2.sendCommand(PCP.START_COLOR, new String[] {"10","255","10"});
					  client3.sendCommand(PCP.START_COLOR, new String[] {"10","10","255"});
					  Thread.sleep(2000);					  
					  client3.sendCommand(PCP.START_COLOR, new String[] {"255","10","10"});
					  client1.sendCommand(PCP.START_COLOR, new String[] {"10","255","10"});
					  client2.sendCommand(PCP.START_COLOR, new String[] {"10","10","255"});
					  Thread.sleep(2000);					  
					  client2.sendCommand(PCP.START_COLOR, new String[] {"255","10","10"});
					  client3.sendCommand(PCP.START_COLOR, new String[] {"10","255","10"});
					  client1.sendCommand(PCP.START_COLOR, new String[] {"10","10","255"});
					  Thread.sleep(2000);					  
					  client1.sendCommand(PCP.START_COLOR, new String[] {"0","0","0"});
					  client2.sendCommand(PCP.START_COLOR, new String[] {"0","0","0"});
					  client3.sendCommand(PCP.START_COLOR, new String[] {"0","0","0"});
					  Thread.sleep(2000);					  
					  System.out.println(client1.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"toycom13.mpg"}));
					  System.out.println(client2.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"toycom13.mpg"}));
					  System.out.println(client3.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"toycom13.mpg"}));

					  System.out.println(client1.sendCommand(PCP.START_VIDEO_MEDIA));
					  System.out.println(client2.sendCommand(PCP.START_VIDEO_MEDIA));
					  System.out.println(client3.sendCommand(PCP.START_VIDEO_MEDIA));
					  Thread.sleep(10000);
					  System.out.println(client1.sendCommand(PCP.STOP_VIDEO_MEDIA));
					  System.out.println(client2.sendCommand(PCP.STOP_VIDEO_MEDIA));
					  System.out.println(client3.sendCommand(PCP.STOP_VIDEO_MEDIA));

					  System.out.println(client1.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"toycom13.mpg"}));
					  System.out.println(client2.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"8pounder.mpg"}));
					  System.out.println(client3.sendCommand(PCP.LOAD_VIDEO_MEDIA, new String[] {"bailey.mpg"}));

					  System.out.println(client1.sendCommand(PCP.START_VIDEO_MEDIA));
					  System.out.println(client2.sendCommand(PCP.START_VIDEO_MEDIA));
					  System.out.println(client3.sendCommand(PCP.START_VIDEO_MEDIA));
					  Thread.sleep(10000);
					  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean10.jpg"}));
					  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean20.jpg"}));
					  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"ocean30.jpg"}));
					  Thread.sleep(1000);
					  System.out.println(client1.sendCommand(PCP.STOP_VIDEO_MEDIA));
					  System.out.println(client2.sendCommand(PCP.STOP_VIDEO_MEDIA));
					  System.out.println(client3.sendCommand(PCP.STOP_VIDEO_MEDIA));
					  
					  System.out.println(client1.sendCommand(PCP.START_IMAGE));
					  System.out.println(client2.sendCommand(PCP.START_IMAGE));
					  System.out.println(client3.sendCommand(PCP.START_IMAGE));
					  System.out.println(client1.sendCommand(PCP.LOAD_IMAGE, new String[] {"mountain10.jpg"}));
					  System.out.println(client2.sendCommand(PCP.LOAD_IMAGE, new String[] {"mountain20.jpg"}));
					  System.out.println(client3.sendCommand(PCP.LOAD_IMAGE, new String[] {"mountain30.jpg"}));
					  Thread.sleep(5000);
					  System.out.println(client1.sendCommand(PCP.START_IMAGE));
					  System.out.println(client2.sendCommand(PCP.START_IMAGE));
					  System.out.println(client3.sendCommand(PCP.START_IMAGE));
					  Thread.sleep(5000);
					  client1.sendCommand(PCP.START_COLOR, new String[] {"0","0","0"});
					  client2.sendCommand(PCP.START_COLOR, new String[] {"0","0","0"});
					  client3.sendCommand(PCP.START_COLOR, new String[] {"0","0","0"});

					  client1.disconnect();
					  client2.disconnect();
					  client3.disconnect();		
				  } catch (Exception e) {
					  e.printStackTrace();
				  }
				  }});	  

	  button3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				  try {
					  SimpleProtocolTCPClient client1 = new SimpleProtocolTCPClient("localhost", 9000, PCP.PROTOCOL);
					  SimpleProtocolTCPClient client2 = new SimpleProtocolTCPClient("localhost", 9001, PCP.PROTOCOL);
					  SimpleProtocolTCPClient client3 = new SimpleProtocolTCPClient("localhost", 9002, PCP.PROTOCOL);
					  client1.connect();
					  client2.connect();
					  client3.connect();
					  PCPMediaTarFileTransferClientCommand command = new PCPMediaTarFileTransferClientCommand();
					  command.setSourceDir(new File("C:/Custom/workspace/PlayMovieServer/media"));
					  client1.executeStreamingCommand(PCP.UPLOAD_MEDIA, command);
					  client2.executeStreamingCommand(PCP.UPLOAD_MEDIA, command);
					  client3.executeStreamingCommand(PCP.UPLOAD_MEDIA, command);

					  client1.disconnect();
					  client2.disconnect();
					  client3.disconnect();		
				  } catch (Exception e) {
					  e.printStackTrace();
				  }
				  }});	 
	  
	  button4.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				  try {
					  new ScenarioPlayer(scenario1).play(3);
				  } catch (Exception e) {
					  e.printStackTrace();
				  }
				  }});	 
	  
	  display.setSize(800, 600);
	  display.setVisible(true);
	  
	}
}
