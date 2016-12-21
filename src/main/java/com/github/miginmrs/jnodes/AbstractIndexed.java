package com.github.miginmrs.jnodes;

import java.util.List;

public abstract class AbstractIndexed<T extends Indexed<T>> implements Indexed<T> {
	Integer index = null;
	T indexed;

	public AbstractIndexed(T indexed) {
		this.indexed = indexed;
	}

	@SuppressWarnings("unchecked")
	public AbstractIndexed() {
		this.indexed = (T) this;
	}

	@Override
	public boolean isIndexed() {
		return index != null;
	}

	@Override
	public boolean isBitween(int min, int max) {
		if (index == null)
			return false;
		return index <= max && index >= min;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public void setIndex(int i) {
		index = i;
	}

	@Override
	public void removeIndex() {
		index = null;
	}

	@Override
	public void addTo(List<T> list) {
		int index = list.size();
		list.add(null);
		for (; index > 0; index--) {
			if (index - 1 == list.get(index - 1).getIndex())
				break;
			list.set(index, list.get(index - 1));
			list.get(index).setIndex(index);
		}
		list.set(index, indexed);
		setIndex(index);
	}

	@Override
	public void removeFrom(List<T> list) {
		int index = getIndex();
		int size = list.size();
		for (; index < size - 1; index++) {
			list.set(index, list.get(index + 1));
			list.get(index).setIndex(index);
		}
		list.remove(list.size() - 1);
		removeIndex();
	}

	@Override
	public boolean isIn(List<T> listIndexedNode) {
		if (index == null)
			return false;
		return indexed.equals(listIndexedNode.get(index));
	}
}
