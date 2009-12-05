package be.rhea.projector.controller.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EditableProperty {
	public enum Type { TEXT, COLOR, FILE, CLIENTS, IP, ARTNET, BOOLEAN }
	String name();
	Type type() default Type.TEXT;
}
