package com.github.miginmrs.jnodes.exceptions;

import java.util.List;

public class CycleException extends Exception {
	private List<?> l;

	public CycleException(List<?> cycle) {
		l = cycle;
	}

	public Object getList() {
		return l;
	}
}
