package be.rhea.projector.controller.server.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.scenario.ScenePart;
import be.rhea.projector.controller.server.scenario.actions.AbstractAction;
import be.rhea.projector.controller.server.ui.beaneditor.BeanEditor;

public class ScenarioViewer extends JTree implements MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String REMOVE_CLIENT = "REMOVE_CLIENT";
	private final BeanEditor beanEditor;
	private DefaultMutableTreeNode clientsItem;
	private Object selectedObject;
	private DefaultMutableTreeNode sceneItem;
	private DefaultMutableTreeNode selectedTreeNode;

	public ScenarioViewer(BeanEditor beanEditor) {
		this.beanEditor = beanEditor;
		this.setScrollsOnExpand(true);
		this.addMouseListener(this);
	}

	@SuppressWarnings("unchecked")
	public Scenario getScenario() {
		TreeModel model = this.getModel();
		if (model != null) {
			Object root = model.getRoot();
			Scenario scenario = (Scenario) ((DefaultMutableTreeNode) root).getUserObject();
			scenario.setClients(new ArrayList<Client>());
			scenario.setScenes(new ArrayList<Scene>());
			Enumeration<DefaultMutableTreeNode> clientEnumeration = clientsItem
					.children();
			while (clientEnumeration.hasMoreElements()) {
				scenario.addClient((Client) clientEnumeration.nextElement()
						.getUserObject());
			}
			Enumeration<DefaultMutableTreeNode> sceneEnumeration = sceneItem
					.children();
			while (sceneEnumeration.hasMoreElements()) {
				DefaultMutableTreeNode sceneTreeNode = sceneEnumeration
						.nextElement();
				Scene scene = (Scene) sceneTreeNode.getUserObject();
				scenario.addScene(scene);
				scene.setSceneParts(new ArrayList<ScenePart>());
				Enumeration<DefaultMutableTreeNode> scenePartEnumeration = sceneTreeNode
						.children();
				while (scenePartEnumeration.hasMoreElements()) {
					DefaultMutableTreeNode scenePartTreeNode = scenePartEnumeration
							.nextElement();
					ScenePart scenePart = (ScenePart) scenePartTreeNode
							.getUserObject();
					scene.addScenePart(scenePart);
					scenePart.setActions(new ArrayList<AbstractAction>());
					Enumeration<DefaultMutableTreeNode> actionsEnumeration = scenePartTreeNode
							.children();
					while (actionsEnumeration.hasMoreElements()) {
						DefaultMutableTreeNode actionTreeNode = actionsEnumeration
								.nextElement();
						AbstractAction action = (AbstractAction) actionTreeNode
								.getUserObject();
						scenePart.addAction(action);
					}
				}
			}
			return scenario;
		}
		return null;
		
	}

	public void setScenario(Scenario scenario) {
		DefaultMutableTreeNode scenarioItem = new DefaultMutableTreeNode(
				scenario);
		clientsItem = new DefaultMutableTreeNode("Clients");
		scenarioItem.add(clientsItem);
		List<Client> clients = scenario.getClients();
		for (Client client : clients) {
			DefaultMutableTreeNode newClientItem = new DefaultMutableTreeNode(
					client);
			clientsItem.add(newClientItem);
		}
		sceneItem = new DefaultMutableTreeNode("Scenes");
		scenarioItem.add(sceneItem);
		List<Scene> scenes = scenario.getScenes();
		for (Scene scene : scenes) {
			DefaultMutableTreeNode newSceneItem = new DefaultMutableTreeNode(
					scene);
			sceneItem.add(newSceneItem);
			List<ScenePart> sceneParts = scene.getSceneParts();
			for (ScenePart scenePart : sceneParts) {
				DefaultMutableTreeNode newScenePartItem = new DefaultMutableTreeNode(
						scenePart);
				newSceneItem.add(newScenePartItem);
				List<AbstractAction> actions = scenePart.getActions();
				for (AbstractAction action : actions) {
					DefaultMutableTreeNode newActionItem = new DefaultMutableTreeNode(
							action);
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

	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		
		TreePath selectionPath = this.getSelectionPath();
		if (selectionPath != null) {
			Object lastPathComponent = selectionPath.getLastPathComponent();
			if (lastPathComponent != null) {
				if (lastPathComponent instanceof DefaultMutableTreeNode) {
					selectedTreeNode = (DefaultMutableTreeNode) lastPathComponent;
					selectedObject = selectedTreeNode.getUserObject();
				}
			}
		}
		if (selectedObject != null) {
			if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
					ArrayList<Client> clients = populateClientsArray();
					beanEditor.setObject(selectedObject, clients);
			} else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
				JPopupMenu popupMenu = new JPopupMenu();
				if (selectedObject instanceof Client) {
					JMenuItem removeMenuItem = new JMenuItem("Remove");
					removeMenuItem.setActionCommand(REMOVE_CLIENT);
					removeMenuItem.addActionListener(this);
					popupMenu.add(removeMenuItem);
				}
				popupMenu.show(this, mouseEvent.getX(), mouseEvent.getY());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Client> populateClientsArray() {
		ArrayList<Client> clients = new ArrayList<Client>();
		Enumeration<DefaultMutableTreeNode> children = clientsItem
				.children();
		while (children.hasMoreElements()) {
			clients.add((Client) children.nextElement()
					.getUserObject());
		}
		return clients;
	}

	@Override
	public void mouseReleased(MouseEvent mouseevent) {
	}

	public Object getSelectedObject() {
		return selectedObject;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (REMOVE_CLIENT.equals(event.getActionCommand())) {
			//TODO check if client is not in use anymore
			DefaultTreeModel model = (DefaultTreeModel) this.getModel();
			model.removeNodeFromParent(selectedTreeNode);
		}
	}
}
