package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.remote.PCP;

public class LoadVideoAction extends AbstractAction {
	private static final long serialVersionUID = -1524974021847781782L;
	private String fileName;

	public LoadVideoAction() {
		super(null, 0);
	}

	public LoadVideoAction(String name, int clientId, String fileName) {
		super(name, clientId);
		this.setFileName(fileName);
	}

	@Override
	public String getCommand() {
		return PCP.LOAD_VIDEO_MEDIA;
	}

	@Override
	public String[] getParameters() {
		return new String[]{fileName};
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
	
	@Override
	public String toString() {
		return "Load Video " + fileName + super.toString();
	}
}
