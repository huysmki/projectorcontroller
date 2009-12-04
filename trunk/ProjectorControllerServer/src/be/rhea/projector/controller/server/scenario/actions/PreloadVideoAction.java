package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.remote.PCP;

public class PreloadVideoAction extends AbstractVideoAction {
	private static final long serialVersionUID = -1524974021847781782L;

	public PreloadVideoAction() {
		super(null, 0, null);
	}

	public PreloadVideoAction(String name, int clientId, String fileName) {
		super(name, clientId, fileName);
	}

	@Override
	public String getCommand() {
		return PCP.PRELOAD_VIDEO_MEDIA;
	}

	@Override
	public String toString() {
		return "Preload Video " + super.toString();
	}
}
