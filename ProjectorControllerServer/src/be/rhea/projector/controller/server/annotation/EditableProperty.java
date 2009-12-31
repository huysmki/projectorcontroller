package be.rhea.projector.controller.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import be.rhea.projector.controller.server.scenario.ClientType;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EditableProperty {
	public enum Type { TEXT, COLOR, FILE, CLIENTS, IP, ARTNET, BOOLEAN, CLIENT_TYPE }
	String name();
	Type type() default Type.TEXT;
	ClientType allowedClientType() default ClientType.PROJECTOR;
}
