package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.projector.controller.server.annotation.EditableProperty;

public class ManualAcknownledgeAction extends AbstractAction {
	private static final long serialVersionUID = 4463488314619618843L;
	public static final String MANUAL_ACKNOWLEDGE = "MANUAL_ACKNOWLEDGE";
	
	@EditableProperty(name="Message")
	private String message;
	
	public ManualAcknownledgeAction() {
		super(null);
	}

	public ManualAcknownledgeAction(String name) {
		super(name);
	}

	@Override
	public String getCommand() {
		return MANUAL_ACKNOWLEDGE;
	}

	@Override
	public String[] getParameters() {
		return null;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Manual Acknowledge " + super.toString();
	}
}
