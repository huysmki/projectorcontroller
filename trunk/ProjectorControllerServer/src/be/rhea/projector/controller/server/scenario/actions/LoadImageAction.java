package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.remote.PCP;

public class LoadImageAction extends AbstractAction {
	private static final long serialVersionUID = -1524974021847781782L;
	private String fileName;

	public LoadImageAction() {
		super(null, 0);
	}

	public LoadImageAction(String name, int clientId, String fileName) {
		super(name, clientId);
		this.setFileName(fileName);
	}

	@Override
	public String getCommand() {
		return PCP.LOAD_IMAGE;
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
		return "Load Image " + fileName + super.toString(); 
	}
}
