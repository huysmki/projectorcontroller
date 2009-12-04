package be.rhea.projector.controller.server.scenario.actions;

import java.io.Serializable;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.projector.controller.server.annotation.EditableProperty.Type;

public abstract class AbstractVideoAction extends AbstractClientAction implements Serializable {
	private static final long serialVersionUID = 4979938776387446471L;
	@EditableProperty(name = "Filename", type = Type.FILE)
	private String fileName;
	
	public AbstractVideoAction(String name, int clientId, String fileName) {
		super(name, clientId);
		this.fileName = fileName;
	}

	public abstract String getCommand();

	public String[] getParameters() {
		return new String[]{fileName};
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
	
	@Override
	public String toString() {
		return  (fileName != null?fileName + " ":"") + super.toString();
	}
}
