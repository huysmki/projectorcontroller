package be.rhea.remote.client;

import java.io.IOException;

public abstract class SimpleProtocolClient {
	public SimpleProtocolClient() {
		super();
	}

	public abstract void disconnect() throws IOException;

	public abstract String executeStreamingCommand(String command,
			SimpleProtocolClientStreamingCommand clientCommand)
			throws IOException;

	public abstract String sendCommand(String command, String[] parameter)
			throws IOException;

	public abstract String sendCommand(String command) throws IOException;

	public abstract void connect() throws IOException;
}