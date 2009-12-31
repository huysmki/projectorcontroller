package be.rhea.projector.controller.server.scenario.actions;

import java.awt.Color;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.projector.controller.server.annotation.EditableProperty.Type;
import be.rhea.remote.PCP;

public class ColorAction extends AbstractProjectorClientAction {
	private static final long serialVersionUID = 6460667393505900137L;
	@EditableProperty(name = "Color", type = Type.COLOR)
	private Color color;

	public ColorAction() {
		super(null, 0);
	}

	public ColorAction(String name, int clientId, Color color) {
		super(name, clientId);
		this.color = color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	@Override
	public String[] getParameters() {
		return new String[] { String.valueOf(color.getRed()),
				String.valueOf(color.getGreen()),
				String.valueOf(color.getBlue()) };
	}

	@Override
	public String getCommand() {
		return PCP.START_COLOR;
	}
	
	@Override
	public String toString() {
		return "Color " + (color != null?(color.getRed() + "," + color.getGreen() + "," + color.getBlue()):"") + super.toString();
	}
}
