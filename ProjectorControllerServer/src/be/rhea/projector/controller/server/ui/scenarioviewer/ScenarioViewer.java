package be.rhea.projector.controller.server.ui.scenarioviewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
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
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.scenario.ScenePart;
import be.rhea.projector.controller.server.scenario.actions.AbstractAction;
import be.rhea.projector.controller.server.scenario.actions.AbstractClientAction;
import be.rhea.projector.controller.server.scenario.actions.ArtNetAction;
import be.rhea.projector.controller.server.scenario.actions.ColorAction;
import be.rhea.projector.controller.server.scenario.actions.FadeOutImageAction;
import be.rhea.projector.controller.server.scenario.actions.LoadImageAction;
import be.rhea.projector.controller.server.scenario.actions.ManualAcknownledgeAction;
import be.rhea.projector.controller.server.scenario.actions.PlayImageAction;
import be.rhea.projector.controller.server.scenario.actions.PreloadVideoAction;
import be.rhea.projector.controller.server.scenario.actions.RepeatScenePartAction;
import be.rhea.projector.controller.server.scenario.actions.SleepAction;
import be.rhea.projector.controller.server.scenario.actions.StartVideoAction;
import be.rhea.projector.controller.server.scenario.actions.StopVideoAction;
import be.rhea.projector.controller.server.scenario.actions.TransitionColorAction;
import be.rhea.projector.controller.server.ui.beaneditor.BeanEditor;

public class ScenarioViewer extends JTree implements MouseListener, ActionListener, TreeSelectionListener {
	private static final long serialVersionUID = 1L;
	private static final String REMOVE_CLIENT = "REMOVE_CLIENT";
	private static final String ADD_CLIENT = "ADD_CLIENT";
	private static final String ADD_ACTION = "ADD_ACTION";
	private static final String MOVE_DOWN = "MOVE_ACTION_DOWN";
	private static final String MOVE_UP = "MOVE_ACTION_UP";
	private static final String REMOVE = "REMOVE";
	private static final String DUPLICATE = "DUPLICATE";
	private static final String ADD_SCENE = "ADD_SCENE";
	private static final String ADD_SCENEPART = "ADD_SCENEPART";
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

	public void mouseClicked(MouseEvent mouseevent) {
	}

	public void mouseEntered(MouseEvent mouseevent) {
	}

	public void mouseExited(MouseEvent mouseevent) {
	}

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
		if (selectedObject instanceof Client || clientsItem == selectedTreeNode) {
			JMenuItem addClientMenuItem = new JMenuItem("Add Client");
			addClientMenuItem.setActionCommand(ADD_CLIENT);
			addClientMenuItem.addActionListener(this);
			popupMenu.add(addClientMenuItem);
		}
		if (selectedObject instanceof Client) {
			JMenuItem removeClientMenuItem = new JMenuItem("Remove");
			removeClientMenuItem.setActionCommand(REMOVE_CLIENT);
			removeClientMenuItem.addActionListener(this);
			popupMenu.add(removeClientMenuItem);

			addMoveUpDownMenuItems(popupMenu);
		}
		if (selectedObject instanceof ScenePart || selectedObject instanceof AbstractAction) {
			JMenuItem addActionMenuItem = new JMenuItem("Add Action");
			addActionMenuItem.setActionCommand(ADD_ACTION);
			addActionMenuItem.addActionListener(this);
			popupMenu.add(addActionMenuItem);			
		}
		if (selectedObject instanceof AbstractAction) {
			addRemoveMenuItem(popupMenu);			
			addMoveUpDownMenuItems(popupMenu);
			addDuplicateMenuItem(popupMenu);			
		}
		if (selectedObject instanceof Scene || sceneItem == selectedTreeNode) {
			JMenuItem addSceneMenuItem = new JMenuItem("Add Scene");
			addSceneMenuItem.setActionCommand(ADD_SCENE);
			addSceneMenuItem.addActionListener(this);

			popupMenu.add(addSceneMenuItem);
		}
		if (selectedObject instanceof Scene || selectedObject instanceof ScenePart) {
			JMenuItem addSceneMenuItem = new JMenuItem("Add ScenePart");
			addSceneMenuItem.setActionCommand(ADD_SCENEPART);
			addSceneMenuItem.addActionListener(this);
			popupMenu.add(addSceneMenuItem);

			addRemoveMenuItem(popupMenu);			
			addMoveUpDownMenuItems(popupMenu);
		}
	
	}

	private void addDuplicateMenuItem(JPopupMenu popupMenu) {
		JMenuItem removeActionMenuItem = new JMenuItem("Duplicate");
		removeActionMenuItem.setActionCommand(DUPLICATE);
		removeActionMenuItem.addActionListener(this);
		popupMenu.add(removeActionMenuItem);
	}
	private void addRemoveMenuItem(JPopupMenu popupMenu) {
		JMenuItem removeActionMenuItem = new JMenuItem("Remove");
		removeActionMenuItem.setActionCommand(REMOVE);
		removeActionMenuItem.addActionListener(this);
		popupMenu.add(removeActionMenuItem);
	}

	private void addMoveUpDownMenuItems(JPopupMenu popupMenu) {
		TreeNode parent = selectedTreeNode.getParent();
		int childCount = parent.getChildCount();
		int index = parent.getIndex(selectedTreeNode);
		if (index > 0) {
			JMenuItem moveActionDownMenuItem = new JMenuItem("Move Up");
			moveActionDownMenuItem.setActionCommand(MOVE_UP);
			moveActionDownMenuItem.addActionListener(this);
			popupMenu.add(moveActionDownMenuItem);			
			
		}
		if (index < (childCount - 1)) {
			JMenuItem moveActionUpMenuItem = new JMenuItem("Move Down");
			moveActionUpMenuItem.setActionCommand(MOVE_DOWN);
			moveActionUpMenuItem.addActionListener(this);
			popupMenu.add(moveActionUpMenuItem);			
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

	public void mouseReleased(MouseEvent mouseevent) {
	}

	public Object getSelectedObject() {
		return selectedObject;
	}

	public void actionPerformed(ActionEvent event) {
		if (REMOVE_CLIENT.equals(event.getActionCommand())) {
			if (JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
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
			}
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
			setTreeNodeAsSelected(newClientNode);
		} else if (ADD_ACTION.equals(event.getActionCommand())) {
			
			Object[] actions = new Object[]{new SleepAction(), 
					                        new ManualAcknownledgeAction(),
					                        new ColorAction(),
					                        new TransitionColorAction(),
					                        new LoadImageAction(),
					                        new PlayImageAction(),
					                        new FadeOutImageAction(),
					                        new PreloadVideoAction(),
					                        new StartVideoAction(),
					                        new StopVideoAction(),
					                        new ArtNetAction(),
					                        new RepeatScenePartAction()};
			Object selectedAction = JOptionPane.showInputDialog(null, "Select Action to add", "Select action", JOptionPane.QUESTION_MESSAGE, null, actions, null);
			if (selectedAction != null) {
				
				DefaultMutableTreeNode newActionNode = new DefaultMutableTreeNode(selectedAction);
				if (selectedObject instanceof ScenePart) {
					selectedTreeNode.add(newActionNode);
					DefaultTreeModel model = (DefaultTreeModel) this.getModel();
					if (model != null) {
						model.nodeStructureChanged(selectedTreeNode);
					}					
				} else if (selectedObject instanceof AbstractAction) {
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedTreeNode.getParent();
					int index = parent.getIndex(selectedTreeNode);
					parent.insert(newActionNode, index + 1);
					DefaultTreeModel model = (DefaultTreeModel) this.getModel();
					if (model != null) {
						model.nodeStructureChanged(parent);
					}					
				}
				setTreeNodeAsSelected(newActionNode);
			}
		} else if (MOVE_DOWN.equals(event.getActionCommand())) {
			DefaultMutableTreeNode backupTreeNode = selectedTreeNode;
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedTreeNode.getParent();
			int index = parent.getIndex(selectedTreeNode);
			parent.remove(index);
			parent.insert(selectedTreeNode, index + 1);
			DefaultTreeModel model = (DefaultTreeModel) this.getModel();
			if (model != null) {
				model.nodeStructureChanged(parent);
			}		
			setTreeNodeAsSelected(backupTreeNode);
			
		} else if (MOVE_UP.equals(event.getActionCommand())) {
			DefaultMutableTreeNode backupTreeNode = selectedTreeNode;
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedTreeNode.getParent();
			int index = parent.getIndex(selectedTreeNode);
			parent.remove(index);
			parent.insert(selectedTreeNode, index - 1);
			DefaultTreeModel model = (DefaultTreeModel) this.getModel();
			if (model != null) {
				model.nodeStructureChanged(parent);
			}					
			setTreeNodeAsSelected(backupTreeNode);

		} else if (REMOVE.equals(event.getActionCommand())) {
			if (JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
				DefaultTreeModel model = (DefaultTreeModel) this.getModel();
				model.removeNodeFromParent(selectedTreeNode);
			}
		} else if (DUPLICATE.equals(event.getActionCommand())) {
			Object clonedObject = clone(selectedObject);
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(clonedObject);
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedTreeNode.getParent();
			int index = parent.getIndex(selectedTreeNode);
			parent.insert(newNode, index + 1);
			DefaultTreeModel model = (DefaultTreeModel) this.getModel();
			if (model != null) {
				model.nodeStructureChanged(parent);
			}					
			setTreeNodeAsSelected(newNode);

		} else if (ADD_SCENE.equals(event.getActionCommand())) {
			DefaultMutableTreeNode newSceneNode = new DefaultMutableTreeNode(new Scene());
			if (selectedTreeNode == sceneItem) {
				selectedTreeNode.add(newSceneNode);
				DefaultTreeModel model = (DefaultTreeModel) this.getModel();
				if (model != null) {
					model.nodeStructureChanged(selectedTreeNode);
				}					
			} else if (selectedObject instanceof Scene) {
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedTreeNode.getParent();
				int index = parent.getIndex(selectedTreeNode);
				parent.insert(newSceneNode, index + 1);
				DefaultTreeModel model = (DefaultTreeModel) this.getModel();
				if (model != null) {
					model.nodeStructureChanged(parent);
				}					
			}
			setTreeNodeAsSelected(newSceneNode);
		} else if (ADD_SCENEPART.equals(event.getActionCommand())) {
			DefaultMutableTreeNode newScenePartNode = new DefaultMutableTreeNode(new ScenePart());
			if (selectedObject instanceof Scene) {
				selectedTreeNode.add(newScenePartNode);
				DefaultTreeModel model = (DefaultTreeModel) this.getModel();
				if (model != null) {
					model.nodeStructureChanged(selectedTreeNode);
				}					
			} else if (selectedObject instanceof ScenePart) {
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedTreeNode.getParent();
				int index = parent.getIndex(selectedTreeNode);
				parent.insert(newScenePartNode, index + 1);
				DefaultTreeModel model = (DefaultTreeModel) this.getModel();
				if (model != null) {
					model.nodeStructureChanged(parent);
				}					
			}
			setTreeNodeAsSelected(newScenePartNode);
		}
	}

	private Object clone(Object objectToClone) {
		try {
			Object clonedObject = objectToClone.getClass().newInstance();
			copyProperties (clonedObject, objectToClone, objectToClone.getClass());
			return clonedObject; 
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void copyProperties(Object clonedObject, Object objectToClone, Class<? extends Object> clazz) throws IllegalArgumentException, IllegalAccessException {
		Class<?> superclass = clazz.getSuperclass();
		if (!superclass.equals(Object.class)) {
			copyProperties(clonedObject, objectToClone, superclass);
		}
		
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (field.isAnnotationPresent(EditableProperty.class)) {
				field.setAccessible(true);
				Object value = field.get(objectToClone);
				field.set(clonedObject, value);
			}
		}
	}

	private void setTreeNodeAsSelected(DefaultMutableTreeNode newClientNode) {
		TreePath treePath = new TreePath(newClientNode.getPath());
		this.setSelectionPath(treePath);
		this.scrollPathToVisible(treePath);
	}

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
