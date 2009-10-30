package be.rhea.projector.controller.server.scenario;

import java.io.Serializable;

import be.rhea.projector.controller.server.annotation.EditableProperty;

public class Client implements Serializable {
	private static final long serialVersionUID = 2816548312557512167L;
	@EditableProperty(name = "Name")
	private String name;
	@EditableProperty(name = "Host")
	private String host;
	@EditableProperty(name = "Port")
	private int port;
	
	public Client() {
	}

	public Client(String name, String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public String toString() {
		return "Client " + name + " on " + host + ":" + port;
	}
}
