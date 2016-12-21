package com.github.miginmrs.jnodes;

import java.util.List;

public class MyArc extends WeightedArcConcret<IntegerQuantity, StringNode, MyArc>
		implements Indexed<MyArc> {
	AbstractIndexed<MyArc> indexed = new AbstractIndexed<MyArc>(this){{}};

	public MyArc(StringNode start, StringNode end, IntegerQuantity weight) {
		super(start, end, weight);
	}

	@Override
	public boolean isIndexed() {
		return indexed.isIndexed();
	}

	@Override
	public boolean isIn(List<MyArc> listIndexed) {
		return indexed.isIn(listIndexed);
	}

	@Override
	public boolean isBitween(int min, int max) {
		return indexed.isBitween(min, max);
	}

	@Override
	public int getIndex() {
		return indexed.getIndex();
	}

	@Override
	public void setIndex(int i) {
		indexed.setIndex(i);
	}

	@Override
	public void addTo(List<MyArc> nodes) {
		indexed.addTo(nodes);
	}

	@Override
	public void removeFrom(List<MyArc> nodes) {
		indexed.removeFrom(nodes);
	}

	@Override
	public void removeIndex() {
		indexed.removeIndex();
	}

	@Override
	public int hashCode() {
		return indexed.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyArc other = (MyArc) obj;
		if (indexed == null) {
			if (other.indexed != null)
				return false;
		} else if (!indexed.equals(other.indexed))
			return false;
		return true;
	}
}
