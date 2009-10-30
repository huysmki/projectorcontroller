package be.rhea.projector.controller.server.scenario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scenario implements Serializable {
	private static final long serialVersionUID = -8814846705485562162L;
	private List<Scene> scenes = new ArrayList<Scene>();
	private Map<Integer, Client> clients = new HashMap<Integer, Client>();
	private String name;

	public Scenario() {
	}

	public Scenario(String name) {
		this.name = name;
	}

	public Scenario addScene(Scene scene) {
		scenes.add(scene);
		return this;
	}

	public Scenario addClient(int clientId, Client client) {
		clients.put(clientId, client);
		return this;
	}
	
	public List<Scene> getScenes() {
		return scenes;
	}

	public void setScenes(List<Scene> scenes) {
		this.scenes = scenes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, Client> getClients() {
		return clients;
	}

	public void setClients(Map<Integer, Client> clients) {
		this.clients.putAll(clients);
	}
	
	@Override
	public String toString() {
		return "Scenario " + name;
	}
}
