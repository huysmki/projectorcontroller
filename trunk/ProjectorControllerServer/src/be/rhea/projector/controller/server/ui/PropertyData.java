package be.rhea.projector.controller.server.ui;

import java.lang.reflect.Field;

public class PropertyData {
	private String name;
	private String type;
	private Field field;

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Field getField() {
		return field;
	}
}
