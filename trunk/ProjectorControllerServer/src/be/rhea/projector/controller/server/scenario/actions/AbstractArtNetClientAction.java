package be.rhea.projector.controller.server.scenario.actions;

import java.io.Serializable;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.projector.controller.server.annotation.EditableProperty.Type;
import be.rhea.projector.controller.server.scenario.ClientType;

public abstract class AbstractArtNetClientAction extends AbstractAction implements Serializable {
	private static final long serialVersionUID = 4979938776387446471L;
	@EditableProperty(name = "Client", type=Type.CLIENTS, allowedClientType=ClientType.ARTNET)
	private int clientId;
	
	public AbstractArtNetClientAction(String name, int clientId) {
		super(name);
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
		return  (clientId > 0?": on client " + clientId:"");
	}
}
