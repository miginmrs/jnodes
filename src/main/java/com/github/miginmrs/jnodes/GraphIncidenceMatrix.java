package com.github.miginmrs.jnodes;

import com.github.miginmrs.jnodes.exceptions.ArcNotFoundException;
import com.github.miginmrs.jnodes.exceptions.DuplicatedArcException;
import com.github.miginmrs.jnodes.exceptions.DuplicatedNodeException;
import com.github.miginmrs.jnodes.exceptions.NodeNotFoundException;

import java.util.*;

public class GraphIncidenceMatrix<Node extends GNode<Node> & Indexed<Node>, Link extends Bridge<Node, Link> & Indexed<Link>>
		implements Graph<Node, Link> {

	List<Node> nodes;
	List<Link> links;
	Matrix<Integer> matrix;

	/** @Complexity = O(n*m) W(n*m) */
	public GraphIncidenceMatrix(int n, int m) {
		matrix = new Matrix<>(n, m);
		nodes = new ArrayList<>(n);
		links = new ArrayList<>(m);
	}

	/** @Complexity = O(nodes.size()) W(1) */
	@Override
	public void add(Node n) throws DuplicatedNodeException {
		if (n.isIndexed())
			throw new DuplicatedNodeException();
		if (nodes.size() == matrix.maxLine())
			throw new ArrayIndexOutOfBoundsException();
		n.addTo(nodes);
	}

	/** @Complexity = O(links.size()) W(1) */
	@Override
	public void add(Link l) throws NodeNotFoundException,
            DuplicatedArcException {
		if (l.isIndexed())
			throw new DuplicatedArcException();
		for (Node node : l.getEnds())
			if (!node.isIn(nodes))
				throw new NodeNotFoundException();
		if (links.size() == matrix.maxColumn())
			throw new ArrayIndexOutOfBoundsException();
		l.addTo(links);
		if (l.isReflexive()) {
			Node node = l.getA();
			matrix.set(node.getIndex(), l.getIndex(), 2);
		} else
			for (Node node : l.getEnds())
				matrix.set(node.getIndex(), l.getIndex(), l.starts(node) ? 1
						: -1);
	}

	/** @Complexity = O(links.size()) W(1) */
	@Override
	public boolean isLinked(Node start, Node end) throws NodeNotFoundException {
		if (!start.isIn(nodes) || !end.isIn(nodes))
			throw new NodeNotFoundException();
		for (Link l : links)
			if (matrix.get(start.getIndex(), l.getIndex()) > 0
					&& matrix.get(end.getIndex(), l.getIndex()) != 0)
				return true;
		return false;
	}

	/** @Complexity = O(nexts*(start_links+other_nodes)+links) W(nodes+links) */
	@Override
	public Collection<Node> getNext(Node start) throws NodeNotFoundException {
		if (!start.isIn(nodes))
			throw new NodeNotFoundException();
		Collection<Node> linkedNodes = new HashSet<>();
		LinkedList<Integer> linksIndexs = new LinkedList<>();
		LinkedList<Integer> nodesIndexs = new LinkedList<>();
		for (Link l : links)
			if (matrix.get(start.getIndex(), l.getIndex()) > 0)
				linksIndexs.add(l.getIndex());
		for (Node node : nodes)
			if (node.getIndex() != start.getIndex())
				nodesIndexs.add(node.getIndex());
		boolean exist = false;
		ListIterator<Integer> it = linksIndexs.listIterator();
		while (it.hasNext()) {
			if (matrix.get(start.getIndex(), it.next()) == 2) {
				exist = true;
				it.remove();
			}
		}
		if (exist)
			linkedNodes.add(start);
		while (!linksIndexs.isEmpty()) {
			int i, j = linksIndexs.pop();
			for (int iIndex = 0; matrix.get(i = nodesIndexs.get(iIndex), j) == 0; )
				iIndex++;
			nodesIndexs.removeFirstOccurrence(i);
			it = linksIndexs.listIterator();
			while (it.hasNext())
				if (matrix.get(i, it.next()) != 0)
					it.remove();
			linkedNodes.add(nodes.get(i));
		}
		return linkedNodes;
	}

	/** @Complexity = O(1) W(1) */
	@Override
	public Collection<Node> getNodes() {
		return nodes;
	}

	/** @Complexity = O(links.size()) W(links.size()) */
	@Override
	public Collection<Link> getLinks(Node start, Node end)
			throws NodeNotFoundException {
		if (!start.isIn(nodes) || !start.isIn(nodes))
			throw new NodeNotFoundException();
		Collection<Link> rLinks = new LinkedList<>();
		for (Link l : links)
			if (matrix.get(start.getIndex(), l.getIndex()) > 0
					&& matrix.get(end.getIndex(), l.getIndex()) != 0)
				rLinks.add(l);
		return rLinks;
	}

	@Override
	public void remove(Node n) throws NodeNotFoundException {
		if (!n.isIn(nodes))
			throw new NodeNotFoundException();
		for (Link link : links)
			if (matrix.get(n.getIndex(), link.getIndex()) > 0)
				try {
					remove(link);
				} catch (ArcNotFoundException ignored) {
				}
		n.removeFrom(nodes);
	}

	@Override
	public void remove(Link l) throws ArcNotFoundException {
		if (!l.isIn(links))
			throw new ArcNotFoundException();
		for (Node n : nodes)
			matrix.set(n.getIndex(), l.getIndex(), 0);
		l.removeFrom(links);
	}
}
