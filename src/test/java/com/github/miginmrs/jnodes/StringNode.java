package com.github.miginmrs.jnodes;

public class StringNode extends AbstractIndexed<StringNode> implements
		GNode<StringNode> {
	public String s;

	public StringNode(String name) {
		s = name;
	}

	@Override
	public String toString() {
		return s;
	}
}
