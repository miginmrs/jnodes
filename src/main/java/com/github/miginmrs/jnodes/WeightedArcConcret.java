package com.github.miginmrs.jnodes;

public class WeightedArcConcret<Unit extends Quantity<Unit>, Node, T extends WeightedArcConcret<Unit, Node, T>>
		extends ArcImpl<Node, T> implements WeightedArc<Unit, Node, T>, Cloneable {
	Unit weight;
	boolean isLink = true;

	public WeightedArcConcret(Node start, Node end, Unit weight) {
		super(start, end);
		this.weight = weight;
	}

	public WeightedArcConcret(Node start, Node end, Unit weight, boolean isLink) {
		super(start, end);
		this.weight = weight;
		this.isLink = isLink;
	}

	@Override
	public Unit getWeight() {
		return weight;
	}

	@Override
	public void setWeight(Unit weight) {
		this.weight = weight;
	}

	@Override
	public WeightedArc<Unit, Node, T> create(Node start, Node end, Unit val) {
		return new WeightedArcConcret<>(start, end, val, false);
	}

	@Override
	public boolean isLink() {
		return isLink;
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public WeightedArcConcret<Unit, Node, T> clone() {
		return (WeightedArcConcret<Unit, Node, T>) super.clone();
	}

	@Override
	public String toString() {
		return super.toString() + " (" + weight + ")";
	}
}
