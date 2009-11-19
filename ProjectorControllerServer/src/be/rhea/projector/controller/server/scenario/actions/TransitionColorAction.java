package be.rhea.projector.controller.server.scenario.actions;

import java.awt.Color;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.projector.controller.server.annotation.EditableProperty.Type;
import be.rhea.remote.PCP;

public class TransitionColorAction extends AbstractClientAction {
	private static final long serialVersionUID = -8233884029073198849L;
	@EditableProperty(name = "From Color", type = Type.COLOR)
	private Color fromColor;
	@EditableProperty(name = "To Color", type = Type.COLOR)
	private Color toColor;
	@EditableProperty(name = "Time (ms)")
	private int time;

	public TransitionColorAction() {
		super(null, 0);
	}

	public TransitionColorAction(String name, int clientId, Color fromColor, Color toColor, int time) {
		super(name, clientId);
		this.setFromColor(fromColor);
		this.setToColor(toColor);
		this.setTime(time);
	}

	@Override
	public String[] getParameters() {
		return new String[] { String.valueOf(getFromColor().getRed()),
							  String.valueOf(getFromColor().getGreen()),
							  String.valueOf(getFromColor().getBlue()),
							  String.valueOf(getToColor().getRed()),
							  String.valueOf(getToColor().getGreen()),
							  String.valueOf(getToColor().getBlue()),
							  String.valueOf(getTime())};
	}

	@Override
	public String getCommand() {
		return PCP.START_TRANSITION_COLOR;
	}

	public void setFromColor(Color fromColor) {
		this.fromColor = fromColor;
	}

	public Color getFromColor() {
		return fromColor;
	}

	public void setToColor(Color toColor) {
		this.toColor = toColor;
	}

	public Color getToColor() {
		return toColor;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		return "Transition " + (fromColor != null && toColor != null?"from " + fromColor.getRed() + "," + fromColor.getGreen() + "," + fromColor.getBlue() +
		       " to " + toColor.getRed() + "," + toColor.getGreen() + "," + toColor.getBlue() +
		       " in " + time + " ms":"")  + super.toString();
	}
}
