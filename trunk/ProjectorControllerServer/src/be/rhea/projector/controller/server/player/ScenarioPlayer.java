package be.rhea.projector.controller.server.player;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.Scenario;
import be.rhea.projector.controller.server.scenario.Scene;
import be.rhea.projector.controller.server.scenario.ScenePart;
import be.rhea.projector.controller.server.scenario.actions.AbstractAction;
import be.rhea.projector.controller.server.scenario.actions.ManualAcknownledgeAction;
import be.rhea.projector.controller.server.scenario.actions.SleepAction;
import be.rhea.remote.PCP;
import be.rhea.remote.client.SimpleProtocolClient;
import be.rhea.remote.client.SimpleProtocolUDPClient;

public class ScenarioPlayer {

	private final Scenario scenario;

	public ScenarioPlayer(Scenario scenario) {
		this.scenario = scenario;
	}

	public void play(int sceneId) {
		Map<Integer, Client> clients = scenario.getClients();
		
		Scene scene = scenario.getScenes().get(sceneId);
		
		List<ScenePart> sceneParts = scene.getSceneParts();
		for (ScenePart scenePart : sceneParts) {
			List<AbstractAction> actions = scenePart.getActions();
			for (AbstractAction action : actions) {
				Client client = clients.get(action.getClientId());
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
				 else {
					sendCommand(client, command, action.getParameters());
				}
			}
		}
		
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
}
