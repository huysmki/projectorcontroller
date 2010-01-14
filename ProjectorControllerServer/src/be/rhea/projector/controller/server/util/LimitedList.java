package be.rhea.projector.controller.server.util;

import java.util.LinkedList;

@SuppressWarnings("unchecked")
public class LimitedList extends LinkedList {
	private static final long serialVersionUID = 1L;
	private static final int MAX_ELEMENTS = 5;

	@Override
	public boolean add(Object object) {
		if (this.size() >= MAX_ELEMENTS) {
			this.removeLast();
		}
		return super.add(object);
	}

	
}