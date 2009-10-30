package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.remote.PCP;

public class StopVideoAction extends AbstractAction {
	private static final long serialVersionUID = -210805641078411328L;

	public StopVideoAction() {
		super(null,0);
	}
	public StopVideoAction(String name, int clientId) {
		super(name, clientId);
	}

	@Override
	public String getCommand() {
		return PCP.STOP_VIDEO_MEDIA;
	}

	@Override
	public String[] getParameters() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Stop Video" + super.toString();
	}
}
