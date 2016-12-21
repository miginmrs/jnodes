package com.github.miginmrs.jnodes;

import com.github.miginmrs.jnodes.exceptions.ArcNotFoundException;
import com.github.miginmrs.jnodes.exceptions.DuplicatedArcException;
import com.github.miginmrs.jnodes.exceptions.DuplicatedNodeException;
import com.github.miginmrs.jnodes.exceptions.NodeNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class GraphAdjacenceList<Node extends GNode<Node> & Indexed<Node>, Link extends Bridge<Node, Link> & Indexed<Link>>
		implements OrientedGraph<Node, Link> {
	ArrayList<LinkedList<Link>> map;
	ArrayList<Node> nodes;

	public GraphAdjacenceList(int n) {
		map = new ArrayList<>(n);
		nodes = new ArrayList<>(n);
	}

	@Override
	public void add(Node n) throws DuplicatedNodeException {
		if (n.isIndexed())
			throw new DuplicatedNodeException();
		n.addTo(nodes);
		map.add(n.getIndex(), new LinkedList<Link>());
	}

	@Override
	public void add(Link l) throws NodeNotFoundException,
            DuplicatedArcException {
		if (l.isIndexed())
			throw new DuplicatedArcException();
		for (Node node : l.getEnds())
			if (!node.isIn(nodes))
				throw new NodeNotFoundException();
		l.addTo(map.get(l.getA().getIndex()));
		if (l.starts(l.getB()))
			l.addTo(map.get(l.getB().getIndex()));
	}

	@Override
	public boolean isLinked(Node start, Node end) throws NodeNotFoundException {
		if (!start.isIn(nodes) || !end.isIn(nodes))
			throw new NodeNotFoundException();
		for (Link l : map.get(start.getIndex()))
			if (l.end(start).equals(end))
				return true;
		return false;
	}

	@Override
	public Collection<Node> getNext(Node start) throws NodeNotFoundException {
		if (!start.isIn(nodes))
			throw new NodeNotFoundException();
		Collection<Node> nodes = new HashSet<Node>();
		for (Link l : map.get(start.getIndex()))
			nodes.add(l.end(start));
		return nodes;
	}

	@Override
	public Collection<Node> getNodes() {
		return nodes;
	}

	@Override
	public Collection<Link> getLinks(Node start, Node end)
			throws NodeNotFoundException {
		Collection<Link> links = new HashSet<>();
		if (!start.isIn(nodes) || !end.isIn(nodes))
			throw new NodeNotFoundException();
		for (Link l : map.get(start.getIndex()))
			if (l.end(start).equals(end))
				links.add(l);
		return links;
	}

	@Override
	public void remove(Node n) throws NodeNotFoundException {
		if (!n.isIn(nodes))
			throw new NodeNotFoundException();
		int index = n.getIndex();
		n.removeFrom(nodes);
		map.remove(index);
		for (Node node : nodes)
			for (Link l : map.get(node.getIndex()))
				if (l.end(node).equals(n))
					l.removeFrom(map.get(node.getIndex()));
	}

	@Override
	public void remove(Link l) throws ArcNotFoundException {
		for (LinkedList<Link> links : map)
			links.remove(l);
	}
}
