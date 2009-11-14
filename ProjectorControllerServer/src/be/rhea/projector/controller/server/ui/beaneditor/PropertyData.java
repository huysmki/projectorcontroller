package be.rhea.projector.controller.server.ui.beaneditor;

import java.lang.reflect.Field;

import be.rhea.projector.controller.server.annotation.EditableProperty.Type;

public class PropertyData {
	private String name;
	private Type type;
	private Field field;

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public Field getField() {
		return field;
	}
}
