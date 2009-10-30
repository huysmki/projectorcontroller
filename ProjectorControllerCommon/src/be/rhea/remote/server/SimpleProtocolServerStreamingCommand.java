package be.rhea.remote.server;

import java.io.InputStream;


public interface SimpleProtocolServerStreamingCommand extends
		SimpleProtocolServerCommand {
	String execute(InputStream inputStream);

}
