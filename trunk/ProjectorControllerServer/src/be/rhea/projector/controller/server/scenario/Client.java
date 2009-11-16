package be.rhea.projector.controller.server.scenario;

import java.io.Serializable;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.projector.controller.server.annotation.EditableProperty.Type;

public class Client implements Serializable {
	private static final long serialVersionUID = 2816548312557512167L;
	@EditableProperty(name = "Id")
	private int id;
	@EditableProperty(name = "Name")
	private String name;
	@EditableProperty(name = "Host", type=Type.IP)
	private String host;
	@EditableProperty(name = "Port")
	private int port;
	
	public Client() {
	}

	public Client(int id, String name, String host, int port) {
		this.id = id;
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
		return "Client " + name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
