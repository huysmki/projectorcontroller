package be.rhea.projector.controller.server.scenario.actions;

import java.io.Serializable;

import be.rhea.projector.controller.server.annotation.EditableProperty;

public abstract class AbstractAction implements Serializable {
	private static final long serialVersionUID = 4979938776387446471L;
	@EditableProperty(name = "Name")
	protected String name;
	
	public AbstractAction(String name) {
		this.name = name;
	}

	public abstract String getCommand();

	public abstract String[] getParameters();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return " : " + name;
	}
}
