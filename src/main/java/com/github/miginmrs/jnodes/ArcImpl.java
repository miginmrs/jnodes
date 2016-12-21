package com.github.miginmrs.jnodes;

import java.util.Collection;
import java.util.HashSet;

public class ArcImpl<Node, T extends ArcImpl<Node, T>> implements
		Clone<Arc<Node, T>>, Arc<Node, T> {
	protected Node start;
	protected Node end;

	public ArcImpl(Node start, Node end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public Collection<Node> getEnds() {
		Collection<Node> nodes = new HashSet<>();
		nodes.add(start);
		nodes.add(end);
		return nodes;
	}

	@Override
	public Node getStart() {
		return start;
	}

	@Override
	public Node getEnd() {
		return end;
	}

	@Override
	public void swap() {
		Node aux = start;
		start = end;
		end = aux;
	}

	@Override
	public boolean is(Node start, Node end) {
		return this.start.equals(start) && this.start.equals(end);
	}

	@Override
	public ArcImpl<Node, T> clone() {
		try {
			//noinspection unchecked
			return (ArcImpl<Node, T>) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return start + " link to " + end;
	}

	@Override
	public boolean isOriented() {
		return true;
	}

	@Override
	public boolean starts(Node n) {
		return start.equals(n);
	}

	@Override
	public Node end(Node n) {
		if (n.equals(start))
			return end;
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
