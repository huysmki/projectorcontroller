package be.rhea.projector.controller.server.player;

public class StateChangedEvent {
	private final State state;
	public enum State {PLAY, PAUSE, STOP};
	
	public StateChangedEvent(State state) {
		this.state = state;
	}
	public State getNewState() {
		return state;
	}
}
