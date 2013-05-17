package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.projector.controller.server.annotation.EditableProperty.Type;

public class PlayMP3Action extends AbstractAction {
	private static final long serialVersionUID = 4979938776387446455L;

	
	@EditableProperty(name = "Filename", type = Type.FILE)
	private String fileName;

	@EditableProperty(name = "Wait Until End of MP3", type = Type.BOOLEAN)
	private boolean waitUntilEnd ;
	

	public PlayMP3Action() {
		super(null);
	}
	
	public PlayMP3Action(String name, boolean waitUntilEnd, String fileName) {
		super(name);
		this.setWaitUntilEnd(waitUntilEnd);
		this.setFileName(fileName);
	}
	
	@Override
	public String getCommand() {
		return null;
	}

	@Override
	public String[] getParameters() {
		return null;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isWaitUntilEnd() {
		return waitUntilEnd;
	}

	public void setWaitUntilEnd(boolean waitUntilEnd) {
		this.waitUntilEnd = waitUntilEnd;
	}
	
	@Override
	public String toString() {
		return "Play MP3 " + super.toString();
	}

}
