package com.github.miginmrs.jnodes.exceptions;

import java.util.List;

public class CircuitException extends Exception {
	private List<?> l;

	public CircuitException(List<?> circuit) {
		l = circuit;
	}

	public Object getList() {
		return l;
	}

}
