package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.remote.PCP;

public class PlayVideoAction extends AbstractClientAction {
	private static final long serialVersionUID = -210805641078411328L;

	public PlayVideoAction() {
		super(null,0);
	}
	public PlayVideoAction(String name, int clientId) {
		super(name, clientId);
	}

	@Override
	public String getCommand() {
		return PCP.START_VIDEO_MEDIA;
	}

	@Override
	public String[] getParameters() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Show Video" + super.toString();
	}
}
