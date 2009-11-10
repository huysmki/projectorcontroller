package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.remote.PCP;

public class PlayImageAction extends AbstractClientAction {
	private static final long serialVersionUID = -210805641078411328L;

	public PlayImageAction() {
		super(null,0);
	}
	public PlayImageAction(String name, int clientId) {
		super(name, clientId);
	}

	@Override
	public String getCommand() {
		return PCP.START_IMAGE;
	}

	@Override
	public String[] getParameters() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Show Image" + super.toString();
	}
}
