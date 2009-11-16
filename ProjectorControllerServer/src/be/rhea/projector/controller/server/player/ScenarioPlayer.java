package be.rhea.projector.controller.server.player;

import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.scenario.ScenePart;
import be.rhea.projector.controller.server.scenario.actions.AbstractAction;
import be.rhea.projector.controller.server.scenario.actions.AbstractClientAction;
import be.rhea.projector.controller.server.scenario.actions.ManualAcknownledgeAction;
import be.rhea.projector.controller.server.scenario.actions.SleepAction;
import be.rhea.remote.PCP;
import be.rhea.remote.client.SimpleProtocolClient;
import be.rhea.remote.client.SimpleProtocolUDPClient;

public class ScenarioPlayer implements Runnable {

	private static Scenario scenarioToPlay;
	private static int sceneIdToPlay;
	private static boolean isPlaying = false;
	private static boolean isPaused = false;
	
	private static Thread playerThread;
	
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
		return true;
	}
	
	public static boolean pause() {
		if (!isPlaying) {
			return false;
		}
		isPaused = !isPaused;
		return true;
	}

	public static boolean stop() {
		if (!isPlaying) {
			return false;
		}
		isPaused = false;
		isPlaying = false;
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

	private void sendCommand(Client client, String command, String[] parameters) {
		
		try {
			System.out.println("Send command " + command + " to " + client.getHost() + ":" + client.getPort());
//			SimpleProtocolTCPClient socketClient = new SimpleProtocolTCPClient(client.getHost(), client.getPort(), PCP.PROTOCOL);
			SimpleProtocolClient socketClient = new SimpleProtocolUDPClient(client.getHost(), client.getPort(), PCP.PROTOCOL);
			socketClient.connect();
			socketClient.sendCommand(command, parameters);
			socketClient.disconnect();
		} catch (IOException e) {
			//TODO log
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		List<Client> clients = scenarioToPlay.getClients();
		
		Scene scene = scenarioToPlay.getScenes().get(sceneIdToPlay);
		
		List<ScenePart> sceneParts = scene.getSceneParts();
		for (ScenePart scenePart : sceneParts) {
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
					JOptionPane.showMessageDialog(null, "Please Aknowledge");
				}
				 else if (action instanceof AbstractClientAction){
					int clientId = ((AbstractClientAction) action).getClientId();
					Client client = getClientForId(clients, clientId);
					sendCommand(client, command, action.getParameters());
				}
			}
		}
		isPlaying = false;
		isPaused = false;
	}
}
