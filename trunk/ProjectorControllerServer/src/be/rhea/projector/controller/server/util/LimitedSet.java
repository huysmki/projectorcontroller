package be.rhea.projector.controller.server.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class LimitedSet<T> extends LinkedHashSet<T> {
	private static final long serialVersionUID = 1L;
	private static final int MAX_ELEMENTS = 5;
	
	public LimitedSet() {
	}
	
	public LimitedSet(Collection<T> otherCollection) {
		this.addAll(otherCollection);
	}

	@Override
	public boolean add(T object) {
		boolean added = super.add(object);
		if (this.size() >= MAX_ELEMENTS) {
			int index = 0;
			for (Iterator<T> iterator = this.iterator(); iterator.hasNext();) {
				T nextObject = iterator.next();
				if (index++ > MAX_ELEMENTS) {
					this.remove(nextObject);
				}
				
			}
			
		}
		return added;
	}

	
}