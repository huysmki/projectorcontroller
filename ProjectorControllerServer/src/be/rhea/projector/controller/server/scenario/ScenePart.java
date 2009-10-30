package be.rhea.projector.controller.server.scenario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import be.rhea.projector.controller.server.scenario.actions.AbstractAction;

public class ScenePart implements Serializable {
	private static final long serialVersionUID = -9038476037498350402L;
	private String name;
	private List<AbstractAction> actions = new ArrayList<AbstractAction>();

	public ScenePart() {
	}

	public ScenePart(String name) {
		this.name = name;
	}

	public ScenePart addAction(AbstractAction action) {
		actions.add(action);
		return this;
	}
	public List<AbstractAction> getActions() {
		return actions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AbstractAction> getEffects() {
		return actions;
	}

	public void setActions(List<AbstractAction> actions) {
		this.actions = actions;
	}
	
	@Override
	public String toString() {
		return "ScenePart " + name;
	}
}
