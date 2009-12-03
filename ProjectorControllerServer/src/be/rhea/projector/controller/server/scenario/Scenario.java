package be.rhea.projector.controller.server.scenario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import be.rhea.projector.controller.server.annotation.EditableProperty;

public class Scenario implements Serializable {
	private static final long serialVersionUID = -8814846705485562162L;
	private List<Scene> scenes = new ArrayList<Scene>();
	private List<Client> clients = new ArrayList<Client>();
	@EditableProperty(name = "Name")
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

	public Scenario addClient(Client client) {
		clients.add(client);
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

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients.clear();
		this.clients.addAll(clients);
	}
	
	@Override
	public String toString() {
		return "Scenario " + (name == null?"":name);
	}
}
