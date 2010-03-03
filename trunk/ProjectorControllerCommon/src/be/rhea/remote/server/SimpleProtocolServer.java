package be.rhea.remote.server;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public abstract class SimpleProtocolServer {
	
	protected Map<String, SimpleProtocolServerCommand> commandMap;
	protected Logger logger;

	public abstract void start() throws IOException;

	public abstract void stop() throws IOException;

	public void setCommandMap(Map<String, SimpleProtocolServerCommand> commandMap) {
		this.commandMap = commandMap;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
