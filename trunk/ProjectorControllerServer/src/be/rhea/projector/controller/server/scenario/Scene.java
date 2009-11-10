package be.rhea.projector.controller.server.scenario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import be.rhea.projector.controller.server.annotation.EditableProperty;

public class Scene implements Serializable {
	private static final long serialVersionUID = -2077219662539280778L;
	@EditableProperty(name = "Name")
	private String name;
	private List<ScenePart> sceneParts = new ArrayList<ScenePart>();
	
	public Scene() {
	}
	
	public Scene(String name) {
		this.setName(name);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Scene addScenePart(ScenePart scenePart) {
		sceneParts.add(scenePart);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setSceneParts(List<ScenePart> sceneParts) {
		this.sceneParts = sceneParts;
	}

	public List<ScenePart> getSceneParts() {
		return sceneParts;
	}
	
	@Override
	public String toString() {
		return "Scene " + name;
	}

}
