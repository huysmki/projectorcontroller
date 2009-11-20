package be.rhea.projector.controller.server.scenario.actions;

import java.util.ArrayList;
import java.util.List;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.projector.controller.server.annotation.EditableProperty.Type;

public class ArtNetAction extends AbstractClientAction {
	private static final long serialVersionUID = -83159837672501967L;
	@EditableProperty(name = "Values", type=Type.ARTNET)
	private List<Integer> values = new ArrayList<Integer>();

	public ArtNetAction() {
		super(null, 0);
	}
	
	public ArtNetAction(String name, int clientId) {
		super(name, clientId);
	}

	@Override
	public String getCommand() {
		return null;
	}

	@Override
	public String[] getParameters() {
		return null;
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}

	public List<Integer> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return "ArtNet " + super.toString();
	}
}
