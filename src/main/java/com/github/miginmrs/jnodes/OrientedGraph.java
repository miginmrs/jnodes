package com.github.miginmrs.jnodes;

public interface OrientedGraph<Node extends GNode<Node>, Link extends Bridge<Node, Link>>
		extends Graph<Node, Link> {
}