package com.github.miginmrs.jnodes;

import com.github.miginmrs.jnodes.exceptions.DuplicatedArcException;
import com.github.miginmrs.jnodes.exceptions.DuplicatedNodeException;
import com.github.miginmrs.jnodes.exceptions.NodeNotFoundException;
import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

public class Test {
	public static Graph<StringNode, MyArc> g = new GraphAdjacenceList<>(5);
	public static GraphUse<StringNode, IntegerQuantity, MyArc> gu = new GraphUse<>(g);
	public static StringNodeFlyWeight snfw = StringNodeFlyWeight.instance;

	public void testGraph() throws NodeNotFoundException,
			DuplicatedArcException, NumberFormatException, IOException,
			DuplicatedNodeException {
		PrintStream o = System.out;
		remplir("com/github/miginmrs/jnodes/data.txt");
		Assert.assertEquals(
				Arrays.asList(snfw.get("1"), snfw.get("5"), snfw.get("3"), snfw.get("4")),
				gu.getPath(snfw.get("1"), snfw.get("4"))
		);
	}

	public static void remplir(String path) throws IOException,
			DuplicatedNodeException, NumberFormatException,
			NodeNotFoundException, DuplicatedArcException {
		try (BufferedReader f = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream(path)))) {
			String line = f.readLine();
			if (line != null)
				for (String s : line.split(","))
					g.add(snfw.get(s.trim()));
			while ((line = f.readLine()) != null) {
				String[] parsedLine = line.split(",", 3);
				g.add(new MyArc(snfw.get(parsedLine[0].trim()), snfw
						.get(parsedLine[1].trim()), new IntegerQuantity(Integer
						.parseInt(parsedLine[2].trim()))));
			}
		}
	}
}
