package be.rhea.remote.server;


public interface SimpleProtocolServerExecuteCommand extends
		SimpleProtocolServerCommand {
	String execute(String[] parameters);

}
