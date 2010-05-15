package be.rhea.projector.controller.server.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.rhea.projector.controller.server.ProjectorControllerServer;
import be.rhea.projector.controller.server.player.StateChangedEvent.State;
import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.ClientType;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.scenario.ScenePart;
import be.rhea.projector.controller.server.scenario.actions.AbstractAction;
import be.rhea.projector.controller.server.scenario.actions.AbstractArtNetClientAction;
import be.rhea.projector.controller.server.scenario.actions.AbstractProjectorClientAction;
import be.rhea.projector.controller.server.scenario.actions.ArtNetAction;
import be.rhea.projector.controller.server.scenario.actions.ManualAcknownledgeAction;
import be.rhea.projector.controller.server.scenario.actions.RepeatScenePartAction;
import be.rhea.projector.controller.server.scenario.actions.SleepAction;
import be.rhea.remote.PCP;
import be.rhea.remote.client.ArtNetProtocolUDPWithRetryClient;
import be.rhea.remote.client.SimpleProtocolClient;
import be.rhea.remote.client.SimpleProtocolUDPWithRetryClient;

public class ScenarioPlayer implements Runnable {

	private static Scenario scenarioToPlay;
	private static int sceneIdToPlay;
	private static boolean isPlaying = false;
	private static boolean isPaused = false;
	private static Thread playerThread;
	private static List<StateChangedListener> stateChangeListeners = new ArrayList<StateChangedListener>();
	
	//TODO implement Listener to track changing of events
	private ScenarioPlayer() {
	}

	public static boolean setScenario(Scenario scenario) {
		if (isPlaying) {
			return false;
		}
		scenarioToPlay = scenario;
		return true;
	}
	
	public static boolean play(int sceneId) {
		if (isPlaying) {
			return false;
		}
		sceneIdToPlay = sceneId;
		if (playerThread != null) {
			while (playerThread.isAlive()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
		}
		playerThread = new Thread(new ScenarioPlayer());
		isPlaying = true;
		isPaused = false;
		playerThread.start();
		fireStateChangeListeners(new StateChangedEvent(State.PLAY));
		return true;
	}
	
	public static boolean pause() {
		if (!isPlaying) {
			return false;
		}
		isPaused = !isPaused;
		if (isPaused) {
			fireStateChangeListeners(new StateChangedEvent(State.PAUSE));
		} else {
			fireStateChangeListeners(new StateChangedEvent(State.PLAY));
		}
		return true;
	}

	public static boolean stop() {
		if (!isPlaying) {
			return false;
		}
		isPaused = false;
		isPlaying = false;
		List<Integer> artNetData = new ArrayList<Integer>();
		for (int i = 0; i < 512; i++) {
			artNetData.add(new Integer(0));
		}
		
		List<Client> clients = scenarioToPlay.getClients();
		for (Client client : clients) {
			if (client.getType() == ClientType.PROJECTOR) {
				sendSimpleProtocolCommand(client, PCP.STOP, null);
			} else if (client.getType() == ClientType.ARTNET) {
				sendArtNetCommand(client, artNetData);
			}
		}
		fireStateChangeListeners(new StateChangedEvent(State.STOP));
		return true;
	}

	private static Client getClientForId(List<Client> clients, int clientId) {
		for (Client client : clients) {
			if (client.getId() == clientId) {
				return client;
			}
		}
		return null;
	}

	private static void sendSimpleProtocolCommand(Client client, String command, String[] parameters) {
		
		try {
			SimpleProtocolClient socketClient = new SimpleProtocolUDPWithRetryClient(client.getHost(), client.getPort(), PCP.PROTOCOL);
			((SimpleProtocolUDPWithRetryClient)socketClient).setSendRetryTimes(client.getSendRetryTimes());
			((SimpleProtocolUDPWithRetryClient)socketClient).setWaitTimeBetweenRetries(client.getWaitTimeBetweenRetries());

			socketClient.connect();
			socketClient.sendCommand(command, parameters);
			socketClient.disconnect();
		} catch (IOException e) {
			ProjectorControllerServer.showError(e);
		}
	}
	
	private static void sendArtNetCommand(Client client, List<Integer> data) {
		try {
			//System.out.println("Send ArtNet package to " + client.getHost() + ":" + client.getPort());
			ArtNetProtocolUDPWithRetryClient artNetClient = new ArtNetProtocolUDPWithRetryClient(client.getHost(), client.getPort());
			artNetClient.setSendRetryTimes(client.getSendRetryTimes());
			artNetClient.setWaitTimeBetweenRetries(client.getWaitTimeBetweenRetries());
			artNetClient.sendData(data);
		} catch (IOException e) {
			ProjectorControllerServer.showError(e);
		}
	}	

	public void run() {
		List<Client> clients = scenarioToPlay.getClients();
		
		Scene scene = scenarioToPlay.getScenes().get(sceneIdToPlay);
//		Map<Integer, ConcurrentLinkedQueue<byte[]>> queueMap = new HashMap<Integer, ConcurrentLinkedQueue<byte[]>>();
//		for (Client client : clients) {
//			ConcurrentLinkedQueue<byte[]> queue = new ConcurrentLinkedQueue<byte[]>();
//		}
		
		List<ScenePart> sceneParts = scene.getSceneParts();
		for (ScenePart scenePart : sceneParts) {
			boolean repeatScenePart = false;
			int repeatCount = 0;
			do {
				List<AbstractAction> actions = scenePart.getActions();
				for (AbstractAction action : actions) {
					if (!isPlaying) {
						return;
					}
					while (isPaused) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							return;
						}
						if (!isPlaying) {
							return;
						}						
					}
					fireStateChangeListeners(new StateChangedEvent(State.ACTION_EXECUTED, action));
					String command = action.getCommand();
					if (action instanceof SleepAction) {
						int time = ((SleepAction)action).getTime();
						long now = System.currentTimeMillis();
						while (isPlaying && System.currentTimeMillis() < (now + time)) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								ProjectorControllerServer.showError(e);
							}
						}
					} else if (action instanceof ManualAcknownledgeAction) {
						ManualAcknownledgeAction manualAcknowledgeAction = (ManualAcknownledgeAction) action; 
						String message = manualAcknowledgeAction.getMessage()!= null?manualAcknowledgeAction.getMessage():"Please Aknowledge";
						isPaused = true;
						fireStateChangeListeners(new StateChangedEvent(State.MANUAL_ACKNOWLEDGE, message));
					} else if (action instanceof RepeatScenePartAction && !repeatScenePart) {
						RepeatScenePartAction repreatScenePartAction = (RepeatScenePartAction) action;
						repeatScenePart = true;
						repeatCount = repreatScenePartAction.getCount();
					} else if (action instanceof ArtNetAction) {
						int clientId = ((AbstractArtNetClientAction) action).getClientId();
						Client client = getClientForId(clients, clientId);
						sendArtNetCommand(client, ((ArtNetAction)action).getValues());
					} else if (action instanceof AbstractProjectorClientAction){
						int clientId = ((AbstractProjectorClientAction) action).getClientId();
						Client client = getClientForId(clients, clientId);
						sendSimpleProtocolCommand(client, command, action.getParameters());
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} while (repeatScenePart && (repeatCount < 0 || repeatCount-- > 0));
		}
		isPlaying = false;
		isPaused = false;
		fireStateChangeListeners(new StateChangedEvent(State.STOP));
	}

	public static boolean isPlaying() {
		return isPlaying;
	}

	public static boolean isPaused() {
		return isPaused;
	}
	
	public static void addStateChangeListener(StateChangedListener listener) {
		stateChangeListeners.add(listener);
	}
	
	private static void fireStateChangeListeners(StateChangedEvent event) {
		for (StateChangedListener listener : stateChangeListeners) {
			listener.stateChanged(event);
		}
	}

	public static void playActions(List<AbstractAction> actionList) {
		List<Client> clients = scenarioToPlay.getClients();
		for (AbstractAction action : actionList) {
			String command = action.getCommand();
			if (action instanceof ArtNetAction) {
				int clientId = ((AbstractArtNetClientAction) action).getClientId();
				Client client = getClientForId(clients, clientId);
				sendArtNetCommand(client, ((ArtNetAction)action).getValues());
			} else if (action instanceof AbstractProjectorClientAction){
				int clientId = ((AbstractProjectorClientAction) action).getClientId();
				Client client = getClientForId(clients, clientId);
				sendSimpleProtocolCommand(client, command, action.getParameters());
			}	
			fireStateChangeListeners(new StateChangedEvent(State.ACTION_EXECUTED, action));
		}
		
	}
}
