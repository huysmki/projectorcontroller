package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.projector.controller.server.annotation.EditableProperty;

public class RepeatScenePartAction extends AbstractAction {
	private static final long serialVersionUID = -7166663175812991815L;
	@EditableProperty(name="Repeatcount ( -1 = infinite)")
	private int count;

	public RepeatScenePartAction() {
		super(null);
	}

	public RepeatScenePartAction(String name) {
		super(name);
	}

	@Override
	public String getCommand() {
		return null;
	}

	@Override
	public String[] getParameters() {
		return null;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "Repeat ScenePart " + (count < 0?"infinite":count + "times") + super.toString();
	}
}
