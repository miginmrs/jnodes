package com.github.miginmrs.jnodes;

public class IntegerQuantity implements Quantity<IntegerQuantity> {
	private Integer n;

	public IntegerQuantity(Integer n) {
		this.n = n;
	}

	@Override
	public int compareTo(IntegerQuantity q) {
		return n.compareTo(q.n);
	}

	@Override
	public IntegerQuantity plus(IntegerQuantity aUnit) {
		return new IntegerQuantity(n + aUnit.n);
	}

	@Override
	public IntegerQuantity menos(IntegerQuantity aUnit) {
		return new IntegerQuantity(n - aUnit.n);
	}

	@Override
	public String toString() {
		return n.toString();
	}
}
