package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.remote.PCP;

public class StopVideoAction extends AbstractVideoAction {
	private static final long serialVersionUID = -210805641078411328L;

	public StopVideoAction() {
		super(null,0, null);
	}
	public StopVideoAction(String name, int clientId, String fileName) {
		super(name, clientId, fileName);
	}

	@Override
	public String getCommand() {
		return PCP.STOP_VIDEO_MEDIA;
	}

	@Override
	public String toString() {
		return "Stop Video " + super.toString();
	}
}
