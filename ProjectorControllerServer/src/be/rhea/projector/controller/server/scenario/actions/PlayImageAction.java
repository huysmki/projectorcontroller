package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.remote.PCP;

public class PlayImageAction extends AbstractProjectorClientAction {
	private static final long serialVersionUID = -210805641078411328L;
	
	@EditableProperty(name="FadeIn Time (ms)")
	private int fadeInTime = 0;

	public PlayImageAction() {
		super(null,0);
	}
	public PlayImageAction(String name, int clientId) {
		super(name, clientId);
	}

	@Override
	public String getCommand() {
		return PCP.START_IMAGE;
	}

	@Override
	public String[] getParameters() {
		return new String[]{String.valueOf(fadeInTime)};
	}
	
	public void setFadeInTime(int fadeInTime) {
		this.fadeInTime = fadeInTime;
	}
	public int getFadeInTime() {
		return fadeInTime;
	}
	
	@Override
	public String toString() {
		return "Show Image" + super.toString();
	}
}
