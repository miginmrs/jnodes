package com.github.miginmrs.jnodes;

import com.github.miginmrs.jnodes.exceptions.CircuitException;
import com.github.miginmrs.jnodes.exceptions.CycleException;
import com.github.miginmrs.jnodes.exceptions.NodeNotFoundException;

import java.io.PrintStream;
import java.util.*;

public class GraphUse<Node extends GNode<Node>, Unit extends Quantity<Unit>, Link extends WeightedArc<Unit, Node, Link>> {
	Graph<Node, Link> graph;

	public GraphUse(Graph<Node, Link> graph) {
		this.graph = graph;
	}

	public HashMap<Node, Node> djikstra(Node start, Node end, boolean reversed)
			throws NodeNotFoundException {
		final HashMap<Node, Unit> map = new HashMap<>();
		final HashMap<Node, Node> prec = new HashMap<>();
		Collection<Node> remainings = new Vector<>(graph.getNodes());
		for (Node node : graph.getNodes()) {
			prec.put(node, null);
			map.put(node, null);
			//noinspection LoopStatementThatDoesntLoop
			for (Link link : graph.getLinks(reversed ? node : start,
					reversed ? start : node)) {
				map.put(node, link.getWeight());
				break;
			}
		}
		remainings.remove(start);

		for (Node node : remainings) {
			if (graph
					.isLinked(reversed ? node : start, reversed ? start : node))
				prec.put(node, start);
		}
		if (start.equals(end))
			return prec;
		while (!remainings.isEmpty()) {
			Node i = Collections.min(remainings, (n1, n2) -> {
                if (map.get(n1) == null)
                    return 1;
                if (map.get(n2) == null)
                    return -1;
                return map.get(n1).compareTo(map.get(n2));
            });
			if (end != null && i.equals(end) || map.get(i) == null
					|| map.get(i).equals(map.get(end)))
				break;
			remainings.remove(i);
			for (Node j : remainings) {
				if (!graph.isLinked(reversed ? j : i, reversed ? i : j))
					continue;
				Unit weight = null;
				//noinspection LoopStatementThatDoesntLoop
				for (Link link : graph.getLinks(reversed ? j : i, reversed ? i
						: j)) {
					weight = link.getWeight();
					break;
				}
				assert weight != null;
				weight = weight.plus(map.get(i));
				if (map.get(j) == null || map.get(j).compareTo(weight) > 0) {
					map.put(j, weight);
					prec.put(j, i);
				}
			}
		}
		return prec;
	}

	public List<Node> getPath(Node start, Node end)
			throws NodeNotFoundException {
		HashMap<Node, Node> prec = djikstra(start, end, false);
		List<Node> path = new Vector<>();
		if (prec.get(end) == null)
			throw new NodeNotFoundException();
		do {
			path.add(0, end);
			end = prec.get(end);
		} while (end != null);
		return path;
	}

	public Collection<Node> getNexts(Node start, boolean reversed)
			throws NodeNotFoundException {
		Collection<Node> nodes = new HashSet<>();
		for (Map.Entry<Node, Node> entry : djikstra(start, null, reversed)
				.entrySet())
			if (entry.getValue() != null)
				nodes.add(entry.getKey());
		return nodes;
	}

	public Collection<Node> getNexts(HashMap<Node, Node> djk)
			throws NodeNotFoundException {
		Collection<Node> nodes = new HashSet<>();
		for (Map.Entry<Node, Node> entry : djk.entrySet())
			if (entry.getValue() != null)
				nodes.add(entry.getKey());
		return nodes;
	}

	public Collection<Collection<Node>> getCFC(boolean doThrow)
			throws CircuitException {
		Collection<Collection<Node>> cc = new HashSet<>();
		Collection<Node> remaining = new HashSet<>(graph.getNodes());
		Node start;
		while (remaining.iterator().hasNext()) {
			start = remaining.iterator().next();
			// System.out.println("****** choix de "+start);
			Collection<Node> nodes = new HashSet<>();
			nodes.add(start);
			remaining.remove(start);
			Collection<Node> nexts;
			Collection<Node> precedents;
			List<Node> circuit = new Vector<>();
			try {
				if (doThrow && graph.isLinked(start, start)) {
					circuit.add(start);
					throw new CircuitException(circuit);
				}
				HashMap<Node, Node> djkNext = djikstra(start, null, false);
				nexts = getNexts(djkNext);
				HashMap<Node, Node> djkPrecedent = djikstra(start, null, true);
				precedents = getNexts(djkPrecedent);

				for (Node next : nexts)
					if (remaining.contains(next) && precedents.contains(next)) {
						if (doThrow) {
							Node end = next;
							do {
								circuit.add(0, end);
								end = djkNext.get(end);
							} while (end != null);

							end = djkPrecedent.get(next);
							while (end != null) {
								circuit.add(end);
								end = djkPrecedent.get(end);
							}
							throw new CircuitException(circuit);
						}
						nodes.add(next);
						remaining.remove(next);
					}
				cc.add(nodes);
			} catch (NodeNotFoundException ignored) {
			}
		}
		return cc;
	}

	public Collection<Collection<Node>> getCFC() {
		try {
			return getCFC(false);
		} catch (CircuitException ignored) {
		}
		return null;
	}

	public List<Node> getCircuit() {
		try {
			getCFC(true);
		} catch (CircuitException e) {
			//noinspection unchecked
			return (List<Node>) e.getList();
		}
		return null;
	}

	private Couple<List<Node>, Boolean> getCC(Node node,
			Collection<Couple<List<Node>, Boolean>> cfcs) {
		for (Couple<List<Node>, Boolean> cfc : cfcs) {
			if (cfc.start.contains(node))
				return cfc;
		}
		return null;
	}

	public Collection<Couple<List<Node>, Boolean>> getCC(boolean doThrow)
			throws CycleException {
		Collection<Couple<List<Node>, Boolean>> cfcs = new HashSet<>();
		Collection<Link> links = new HashSet<>();
		for (Node start : graph.getNodes()) {
			List<Node> nodes = new Vector<>();
			nodes.add(start);
			cfcs.add(new Couple<>(nodes, false));
			for (Node end : graph.getNodes())
				try {
					links.addAll(graph.getLinks(start, end));
				} catch (NodeNotFoundException ignored) {
				}
		}
		for (Link link : links) {
			Couple<List<Node>, Boolean> cfcStart = getCC(link.getStart(), cfcs), cfcEnd = getCC(
					link.getEnd(), cfcs);
			if (cfcStart == cfcEnd) {
				assert cfcStart != null;
				cfcStart.end = true;
				if (doThrow) {
					List<Node> cycle = new Vector<>();
					boolean start = false;
					for (Node node : cfcStart.start) {
						if (start)
							cycle.add(node);
						if (node.equals(link.getStart())
								|| node.equals(link.getEnd())) {
							if (start)
								break;
							cycle.add(node);
							start = true;
						}
					}
					cycle.add(cycle.get(0));
					throw new CycleException(cycle);
				}
			} else {
				assert cfcStart != null;
				assert cfcEnd != null;
				cfcStart.start.addAll(cfcEnd.start);
				if (cfcEnd.end)
					cfcStart.end = true;
				cfcs.remove(cfcEnd);
			}
		}
		return cfcs;
	}

	public Collection<Couple<List<Node>, Boolean>> getCC() {
		try {
			return getCC(false);
		} catch (CycleException ignored) {
		}
		return null;
	}

	public List<Node> getCycle() {
		try {
			getCC(true);
		} catch (CycleException e) {
			//noinspection unchecked
			return (List<Node>) e.getList();
		}
		return null;
	}

	public void print(PrintStream o) {
		try {
			for (Node i : graph.getNodes()) {
				o.println(i.toString() + ":");
				for (Node j : graph.getNext(i))
					for (Link l : graph.getLinks(i, j))
						o.println(l);
			}
		} catch (NodeNotFoundException ignored) {
		}
	}

	public void printVector(Collection<Node> v, PrintStream o) {
		for (Node n : v)
			o.print(n + " ");
		o.println();
	}
}
