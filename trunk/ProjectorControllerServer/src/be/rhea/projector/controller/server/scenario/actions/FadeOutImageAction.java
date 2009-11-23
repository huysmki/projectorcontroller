package be.rhea.projector.controller.server.scenario.actions;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.remote.PCP;

public class FadeOutImageAction extends AbstractClientAction {
	private static final long serialVersionUID = -210805641078411328L;
	
	@EditableProperty(name="FadeOut Time (ms)")
	private int fadeOutTime;

	public FadeOutImageAction() {
		super(null,0);
	}
	public FadeOutImageAction(String name, int clientId) {
		super(name, clientId);
	}

	@Override
	public String getCommand() {
		return PCP.FADE_OUT_IMAGE;
	}

	@Override
	public String[] getParameters() {
		return new String[]{String.valueOf(fadeOutTime)};
	}
	
	public void setFadeOutTime(int fadeOutTime) {
		this.fadeOutTime = fadeOutTime;
	}
	public int getFadeOutTime() {
		return fadeOutTime;
	}
	
	@Override
	public String toString() {
		return "FadeOut Image" + super.toString();
	}
}
