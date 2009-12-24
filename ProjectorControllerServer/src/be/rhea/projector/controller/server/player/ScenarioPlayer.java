package be.rhea.projector.controller.server.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.rhea.projector.controller.server.player.StateChangedEvent.State;
import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.scenario.ScenePart;
import be.rhea.projector.controller.server.scenario.actions.AbstractAction;
import be.rhea.projector.controller.server.scenario.actions.AbstractClientAction;
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
		List<Client> clients = scenarioToPlay.getClients();
		for (Client client : clients) {
			sendSimpleProtocolCommand(client, PCP.STOP, null);		
		}
		fireStateChangeListeners(new StateChangedEvent(State.STOP));
		return true;
	}

	private Client getClientForId(List<Client> clients, int clientId) {
		for (Client client : clients) {
			if (client.getId() == clientId) {
				return client;
			}
		}
		return null;
	}

	private static void sendSimpleProtocolCommand(Client client, String command, String[] parameters) {
		
		try {
//			//System.out.println("Send command " + command + " to " + client.getHost() + ":" + client.getPort());
//			SimpleProtocolTCPClient socketClient = new SimpleProtocolTCPClient(client.getHost(), client.getPort(), PCP.PROTOCOL);
//			SimpleProtocolClient socketClient = new SimpleProtocolUDPClient(client.getHost(), client.getPort(), PCP.PROTOCOL);
			SimpleProtocolClient socketClient = new SimpleProtocolUDPWithRetryClient(client.getHost(), client.getPort(), PCP.PROTOCOL);
			socketClient.connect();
			socketClient.sendCommand(command, parameters);
			socketClient.disconnect();
		} catch (IOException e) {
			//TODO log
			e.printStackTrace();
		}
	}
	
	private void sendArtNetCommand(Client client, List<Integer> data) {
		try {
			//System.out.println("Send ArtNet package to " + client.getHost() + ":" + client.getPort());
			ArtNetProtocolUDPWithRetryClient socketClient = new ArtNetProtocolUDPWithRetryClient(client.getHost(), client.getPort());
			socketClient.sendData(data);
		} catch (IOException e) {
			//TODO log
			e.printStackTrace();
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
							e.printStackTrace();
							return;
						}
						if (!isPlaying) {
							return;
						}						
					}
					String command = action.getCommand();
					if (action instanceof SleepAction) {
						try {
							Thread.sleep(((SleepAction)action).getTime());
						} catch (InterruptedException e) {
							//TODO log
							e.printStackTrace();
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
						int clientId = ((AbstractClientAction) action).getClientId();
						Client client = getClientForId(clients, clientId);
						sendArtNetCommand(client, ((ArtNetAction)action).getValues());
					} else if (action instanceof AbstractClientAction){
						int clientId = ((AbstractClientAction) action).getClientId();
						Client client = getClientForId(clients, clientId);
						sendSimpleProtocolCommand(client, command, action.getParameters());
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
}
