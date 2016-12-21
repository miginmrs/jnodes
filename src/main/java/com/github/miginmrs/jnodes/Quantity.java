package com.github.miginmrs.jnodes;

public interface Quantity<Unit extends Quantity<Unit>> extends Comparable<Unit> {
	Unit plus(Unit aUnit);

	Unit menos(Unit aUnit);
}
