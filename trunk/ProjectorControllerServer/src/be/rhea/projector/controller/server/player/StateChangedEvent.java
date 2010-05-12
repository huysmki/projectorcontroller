package be.rhea.projector.controller.server.player;

import be.rhea.projector.controller.server.scenario.actions.AbstractAction;

public class StateChangedEvent {
	private final State state;
	private final String message;
	private AbstractAction action;
	public enum State {PLAY, PAUSE, STOP, MANUAL_ACKNOWLEDGE, ACTION_EXECUTED};
	
	public StateChangedEvent(State state) {
		this.state = state;
		message = null;
	}

	public StateChangedEvent(State state, String message) {
		this.state = state;
		this.message = message;
	}

	public StateChangedEvent(State state, AbstractAction action) {
		this.state = state;
		this.action = action;
		message = null;
	}

	public State getNewState() {
		return state;
	}

	public String getMessage() {
		return message;
	}

	public AbstractAction getAction() {
		return action;
	}
}
