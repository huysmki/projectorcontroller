package be.rhea.projector.controller.server.scenario.actions;

public class SleepAction extends AbstractAction {
	private static final long serialVersionUID = 4463488314619618843L;
	public static final String SLEEP = "SLEEP";
	int time;
	
	public SleepAction() {
		super(null, 0);
	}

	public SleepAction(String name, int time) {
		super(name, -1);
		this.time = time;
	}

	@Override
	public String getCommand() {
		return SLEEP;
	}

	@Override
	public String[] getParameters() {
		return null;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "Sleep " + time + " ms" + super.toString();
	}
}
