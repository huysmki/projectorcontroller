package be.rhea.projector.controller.server.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.scenario.ScenePart;
import be.rhea.projector.controller.server.scenario.actions.AbstractAction;
import be.rhea.projector.controller.server.scenario.actions.AbstractClientAction;
import be.rhea.projector.controller.server.ui.beaneditor.BeanEditor;

public class ScenarioViewer extends JTree implements MouseListener, ActionListener, TreeSelectionListener {
	private static final long serialVersionUID = 1L;
	private static final String REMOVE_CLIENT = "REMOVE_CLIENT";
	private static final String ADD_CLIENT = "ADD_CLIENT";
	private final BeanEditor beanEditor;
	private DefaultMutableTreeNode clientsItem;
	private Object selectedObject;
	private DefaultMutableTreeNode sceneItem;
	private DefaultMutableTreeNode selectedTreeNode;

	public ScenarioViewer(BeanEditor beanEditor) {
		this.beanEditor = beanEditor;
		this.setScrollsOnExpand(true);
		this.addMouseListener(this);
		this.addTreeSelectionListener(this);
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

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		if (selectedObject != null) {
			if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
				JPopupMenu popupMenu = new JPopupMenu();
				populatePopupMenu(popupMenu);
				popupMenu.show(this, mouseEvent.getX(), mouseEvent.getY());
			}
		}
	}

	private void populatePopupMenu(JPopupMenu popupMenu) {
		if (selectedObject instanceof Client) {
			JMenuItem removeClientMenuItem = new JMenuItem("Remove");
			removeClientMenuItem.setActionCommand(REMOVE_CLIENT);
			removeClientMenuItem.addActionListener(this);
			popupMenu.add(removeClientMenuItem);
		}
		if (selectedObject instanceof Client || clientsItem == selectedTreeNode) {
			JMenuItem addClientMenuItem = new JMenuItem("Add Client");
			addClientMenuItem.setActionCommand(ADD_CLIENT);
			addClientMenuItem.addActionListener(this);
			popupMenu.add(addClientMenuItem);
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
			Client clientToRemove = (Client)selectedObject;
			int idToRemove = clientToRemove.getId();
			List<Scene> scenes = getScenario().getScenes();
			for (Scene scene : scenes) {
				List<ScenePart> sceneParts = scene.getSceneParts();
				for (ScenePart scenePart : sceneParts) {
					List<AbstractAction> actions = scenePart.getActions();
					for (AbstractAction action : actions) {
						if (action instanceof AbstractClientAction && ((AbstractClientAction)action).getClientId() == idToRemove) {
							JOptionPane.showMessageDialog(null, "Client still used by an Action. Please remove first all usages.", "Error", JOptionPane.ERROR_MESSAGE); 
							return;
						}
					}
				}
			}
			
			DefaultTreeModel model = (DefaultTreeModel) this.getModel();
			model.removeNodeFromParent(selectedTreeNode);
		} else if (ADD_CLIENT.equals(event.getActionCommand())) {
			int id = 1;
			ArrayList<Client> clients = populateClientsArray();
			for (Client client : clients) {
				if (client.getId() == id) {
					id++;
				}
			}
			DefaultMutableTreeNode newClientNode = new DefaultMutableTreeNode(new Client(id, "New Client", "host", 9999));
			clientsItem.add(newClientNode);
			DefaultTreeModel model = (DefaultTreeModel) this.getModel();
			if (model != null) {
				model.nodeStructureChanged(clientsItem);
			}
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		beanEditor.stopEditing();
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		if (model != null && selectedTreeNode != null) {
			model.nodeChanged(selectedTreeNode);
		}
		TreePath selectionPath = event.getNewLeadSelectionPath();
		if (selectionPath != null) {
			Object lastPathComponent = selectionPath.getLastPathComponent();
			if (lastPathComponent != null) {
				if (lastPathComponent instanceof DefaultMutableTreeNode) {
					selectedTreeNode = (DefaultMutableTreeNode) lastPathComponent;
					selectedObject = selectedTreeNode.getUserObject();
					ArrayList<Client> clients = populateClientsArray();
					beanEditor.setObject(selectedObject, clients);
				}
			}
		}		
		
	}
}
