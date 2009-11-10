package be.rhea.projector.controller.server.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.scenario.ScenePart;
import be.rhea.projector.controller.server.scenario.actions.AbstractAction;

public class ScenarioTree extends JTree implements MouseListener {

	private static final long serialVersionUID = 1L;
	private final BeanEditor beanEditor;
	private DefaultMutableTreeNode clientsItem;

	public ScenarioTree(BeanEditor beanEditor) {
		this.beanEditor = beanEditor;
		this.setScrollsOnExpand(true);
		this.addMouseListener(this);

	}
	
	public void setScenario(Scenario scenario) {
		  DefaultMutableTreeNode scenarioItem = new DefaultMutableTreeNode(scenario);

		  clientsItem = new DefaultMutableTreeNode("Clients");
		  scenarioItem.add(clientsItem);
		  List<Client> clients = scenario.getClients();
		  for (Client client : clients) {
			  DefaultMutableTreeNode newClientItem = new DefaultMutableTreeNode(client);
			  clientsItem.add(newClientItem);
		}

		  DefaultMutableTreeNode sceneItem = new DefaultMutableTreeNode("Scenes");
		  scenarioItem.add(sceneItem);

			List<Scene> scenes = scenario.getScenes();
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

		  this.setModel(new DefaultTreeModel(scenarioItem));
	}

	@Override
	public void mouseClicked(MouseEvent mouseevent) {
		
	}

	@Override
	public void mouseEntered(MouseEvent mouseevent) {
		
	}

	@Override
	public void mouseExited(MouseEvent mouseevent) {
		
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
			TreePath selectionPath = this.getSelectionPath();
			if (selectionPath != null) {
				Object lastPathComponent = selectionPath.getLastPathComponent();
				if (lastPathComponent != null) {
					if (lastPathComponent instanceof DefaultMutableTreeNode) {
						DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) lastPathComponent;
//						TreeModel model = tree.getModel();
						ArrayList<Client> clients = new ArrayList<Client>();
						Enumeration<DefaultMutableTreeNode> children = clientsItem.children();
						while (children.hasMoreElements()) {
							clients.add((Client) children.nextElement().getUserObject());
						}
						
						beanEditor.setObject(treeNode.getUserObject(), clients);
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent mouseevent) {
		
	}
}
