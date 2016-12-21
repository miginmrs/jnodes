package com.github.miginmrs.jnodes;

public interface Arc<Node, T extends Arc<Node, T>> extends Bridge<Node, T> {
	Node getStart();

	Node getEnd();

	void swap();

	Arc<Node, T> clone();
}
