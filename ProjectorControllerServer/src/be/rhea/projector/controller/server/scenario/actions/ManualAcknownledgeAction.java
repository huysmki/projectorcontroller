package be.rhea.projector.controller.server.scenario.actions;

public class ManualAcknownledgeAction extends AbstractAction {
	private static final long serialVersionUID = 4463488314619618843L;
	public static final String MANUAL_ACKNOWLEDGE = "MANUAL_ACKNOWLEDGE";
	int time;
	
	public ManualAcknownledgeAction() {
		super(null, 0);
	}

	public ManualAcknownledgeAction(String name) {
		super(name, -1);
		this.time = time;
	}

	@Override
	public String getCommand() {
		return MANUAL_ACKNOWLEDGE;
	}

	@Override
	public String[] getParameters() {
		return null;
	}

	@Override
	public String toString() {
		return "Manual Acknowledge " + super.toString();
	}
}
