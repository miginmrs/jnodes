package com.github.miginmrs.jnodes;

import java.util.List;

public interface Indexed<T extends Indexed<T>> {
	boolean isIndexed();

	boolean isIn(List<T> listIndexedNode);

	boolean isBitween(int min, int max);

	int getIndex();

	void setIndex(int i);

	void addTo(List<T> nodes);

	void removeFrom(List<T> nodes);

	void removeIndex();
}
