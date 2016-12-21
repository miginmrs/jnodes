package com.github.miginmrs.jnodes;

import java.util.Collection;

public interface Bridge<Node, T extends Bridge<Node, T>> extends Cloneable {
	Collection<Node> getEnds();

	boolean is(Node a, Node b);

	boolean starts(Node a);

	boolean isReflexive();

	boolean isOriented();

	Node end(Node n);

	Node getA();

	Node getB();
}
