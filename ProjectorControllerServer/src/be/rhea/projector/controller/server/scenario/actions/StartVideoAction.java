package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.remote.PCP;

public class StartVideoAction extends AbstractVideoAction {
	private static final long serialVersionUID = -1524974021847781782L;

	public StartVideoAction() {
		super(null, 0, null);
	}

	public StartVideoAction(String name, int clientId, String fileName) {
		super(name, clientId, fileName);
	}

	@Override
	public String getCommand() {
		return PCP.START_VIDEO_MEDIA;
	}

	@Override
	public String toString() {
		return "Start Video " + super.toString();
	}
}
