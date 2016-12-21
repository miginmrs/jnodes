package com.github.miginmrs.jnodes;

public class Couple<A, B> {
	public A start;
	public B end;

	public Couple(A start, B end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean equals(Object o) {
		try {
			@SuppressWarnings("unchecked")
			Couple<A, B> couple = (Couple<A, B>) o;
			return start.equals(couple.start) && end.equals(couple.end);
		} catch (RuntimeException e) {
			return false;
		}
	}
}
