package be.rhea.projector.controller.server.scenario.actions;

import java.io.Serializable;

public abstract class AbstractAction implements Serializable {
	private static final long serialVersionUID = 4979938776387446471L;
	protected String name;
	private int clientId;
	
	public AbstractAction(String name, int clientId) {
		this.name = name;
		this.clientId = clientId;
	}

	public abstract String getCommand();

	public abstract String[] getParameters();

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return " : " + name + (clientId > 0?" on client " + clientId:"");
	}
}
