package be.rhea.remote.client;

import java.io.OutputStream;

public interface SimpleProtocolClientStreamingCommand {
	
	void execute(OutputStream stream);
	
}
