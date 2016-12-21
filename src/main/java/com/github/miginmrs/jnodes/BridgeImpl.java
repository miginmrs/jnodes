package com.github.miginmrs.jnodes;

import java.util.Collection;
import java.util.HashSet;

public class BridgeImpl<Node extends Comparable<Node>> implements
		Bridge<Node, BridgeImpl<Node>>, Cloneable {
	private Node start, end;

	public BridgeImpl(Node a, Node b) {
		boolean reverse = a.compareTo(b) > 0;
		this.start = reverse ? a : b;
		this.end = reverse ? b : a;
	}

	@Override
	public Collection<Node> getEnds() {
		Collection<Node> nodes = new HashSet<Node>();
		nodes.add(start);
		nodes.add(end);
		return nodes;
	}

	@Override
	public boolean is(Node a, Node b) {
		boolean reverse = a.compareTo(b) > 0;
		return start.equals(reverse ? a : b) && end.equals(reverse ? b : a);
	}

	@Override
	public BridgeImpl<Node> clone() {
		try {
			//noinspection unchecked
			return (BridgeImpl<Node>) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return start + " and " + end + " are linked";
	}

	@Override
	public boolean isOriented() {
		return false;
	}

	@Override
	public boolean starts(Node n) {
		return start.equals(n) || end.equals(n);
	}

	@Override
	public Node end(Node n) {
		if (n.equals(start))
			return end;
		if (n.equals(end))
			return start;
		return null;
	}

	@Override
	public Node getA() {
		return start;
	}

	@Override
	public Node getB() {
		return end;
	}

	@Override
	public boolean isReflexive() {
		return start.equals(end);
	}
}
