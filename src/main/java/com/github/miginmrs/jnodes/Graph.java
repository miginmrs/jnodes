package com.github.miginmrs.jnodes;

import com.github.miginmrs.jnodes.exceptions.ArcNotFoundException;
import com.github.miginmrs.jnodes.exceptions.DuplicatedArcException;
import com.github.miginmrs.jnodes.exceptions.DuplicatedNodeException;
import com.github.miginmrs.jnodes.exceptions.NodeNotFoundException;

import java.util.Collection;

public interface Graph<Node extends GNode<Node>, Link extends Bridge<Node, Link>> {
	void add(Node n) throws DuplicatedNodeException;

	void remove(Node n) throws NodeNotFoundException;

	void add(Link l) throws NodeNotFoundException, DuplicatedArcException;

	void remove(Link l) throws ArcNotFoundException;

	boolean isLinked(Node start, Node end) throws NodeNotFoundException;

	Collection<Node> getNext(Node start) throws NodeNotFoundException;

	Collection<Node> getNodes();

	Collection<Link> getLinks(Node start, Node end)
			throws NodeNotFoundException;
}
