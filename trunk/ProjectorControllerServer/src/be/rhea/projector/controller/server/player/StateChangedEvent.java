package be.rhea.projector.controller.server.player;

public class StateChangedEvent {
	private final State state;
	private final String message;
	public enum State {PLAY, PAUSE, STOP, MANUAL_ACKNOWLEDGE};
	
	public StateChangedEvent(State state) {
		this.state = state;
		message = null;
	}
	public StateChangedEvent(State state, String message) {
		this.state = state;
		this.message = message;
	}
	public State getNewState() {
		return state;
	}
	public String getMessage() {
		return message;
	}
}
