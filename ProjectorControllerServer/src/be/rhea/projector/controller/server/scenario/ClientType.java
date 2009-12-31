package be.rhea.projector.controller.server.scenario;

public enum ClientType {
	PROJECTOR ("Projector"), ARTNET ("ArtNet");
	
	private String name;

	ClientType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static  ClientType getByName(String name) {
		ClientType[] values = ClientType.values();
		for (int i = 0; i < values.length; i++) {
			ClientType clientType = values[i];
			if (clientType.getName().equals(name)) {
				return clientType;
			}
		}
		return null;
	}
}
