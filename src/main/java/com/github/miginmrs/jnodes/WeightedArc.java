package com.github.miginmrs.jnodes;

public interface WeightedArc<Unit extends Quantity<Unit>, Node, T extends WeightedArc<Unit, Node, T>>
		extends Arc<Node, T> {
	Unit getWeight();

	void setWeight(Unit value);

	WeightedArc<Unit, Node, T> create(Node start, Node end, Unit val);

	boolean isLink();
}
