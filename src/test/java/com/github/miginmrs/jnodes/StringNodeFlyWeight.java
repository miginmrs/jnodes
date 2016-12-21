package com.github.miginmrs.jnodes;

import java.util.HashMap;
import java.util.Map;

public class StringNodeFlyWeight {
	private Map<String, StringNode> map = new HashMap<>();
	public static final StringNodeFlyWeight instance = new StringNodeFlyWeight();

	private StringNodeFlyWeight() {
	}

	public StringNode get(String name) {
		if (!map.containsKey(name))
			map.put(name, new StringNode(name));
		return map.get(name);
	}
}
