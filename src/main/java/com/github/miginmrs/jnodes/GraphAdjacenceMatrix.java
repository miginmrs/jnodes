package com.github.miginmrs.jnodes;

import com.github.miginmrs.jnodes.exceptions.ArcNotFoundException;
import com.github.miginmrs.jnodes.exceptions.DuplicatedArcException;
import com.github.miginmrs.jnodes.exceptions.DuplicatedNodeException;
import com.github.miginmrs.jnodes.exceptions.NodeNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GraphAdjacenceMatrix<Node extends GNode<Node> & Indexed<Node>, Link extends Bridge<Node, Link> & Indexed<Link>>
		implements Graph<Node, Link>, Cloneable {
	List<Node> nodes;
	Matrix<List<Link>> matrix;

	/** @Complexity = O(n²) W(n²) */
	public GraphAdjacenceMatrix(int n) {
		matrix = new Matrix<>(n);
		nodes = new ArrayList<>(n);
	}

	@Override
	public void add(Node n) throws DuplicatedNodeException {
		if (n.isIndexed())
			throw new DuplicatedNodeException();
		if (nodes.size() == matrix.maxLine())
			throw new ArrayIndexOutOfBoundsException();
		n.addTo(nodes);
		for (Node node : nodes) {
			matrix.set(n.getIndex(), node.getIndex(), new LinkedList<>());
			matrix.set(node.getIndex(), n.getIndex(), new LinkedList<>());
		}
	}

	@Override
	public void add(Link l) throws NodeNotFoundException,
            DuplicatedArcException {
		if (l.isIndexed())
			throw new DuplicatedArcException();
		for (Node node : l.getEnds())
			if (!node.isIn(nodes))
				throw new NodeNotFoundException();
		matrix.get(l.getA().getIndex(), l.getB().getIndex()).add(l);
	}

	@Override
	public boolean isLinked(Node start, Node end) throws NodeNotFoundException {
		if (!start.isIn(nodes) || !end.isIn(nodes))
			throw new NodeNotFoundException();
		return !matrix.get(start.getIndex(), end.getIndex()).isEmpty();
	}

	@Override
	public Collection<Node> getNext(Node start) throws NodeNotFoundException {
		if (!start.isIn(nodes))
			throw new NodeNotFoundException();
		Collection<Node> nextNodes = new LinkedList<>();
		for (Node end : nodes)
			if (isLinked(start, end))
				nextNodes.add(end);
		return nextNodes;
	}

	@Override
	public Collection<Node> getNodes() {
		return nodes;
	}

	@Override
	public Collection<Link> getLinks(Node start, Node end)
			throws NodeNotFoundException {
		if (!start.isIn(nodes) || !end.isIn(nodes))
			throw new NodeNotFoundException();
		return matrix.get(start.getIndex(), end.getIndex());
	}

	@Override
	public void remove(Node n) throws NodeNotFoundException {
		if (!n.isIn(nodes))
			throw new NodeNotFoundException();
		for (Node node : nodes) {
			matrix.set(n.getIndex(), node.getIndex(), null);
			matrix.set(node.getIndex(), n.getIndex(), null);
		}
		n.removeFrom(nodes);
	}

	@Override
	public void remove(Link l) throws ArcNotFoundException {
		for (Node node : l.getEnds())
			if (!node.isIn(nodes))
				throw new ArcNotFoundException();
		if (!l.isIn(matrix.get(l.getA().getIndex(), l.getB().getIndex())))
			throw new ArcNotFoundException();
		l.removeFrom(matrix.get(l.getA().getIndex(), l.getB().getIndex()));
	}

}
