package com.github.miginmrs.jnodes;

public class Matrix<T> {
	Object[] matrix;
	private int m;

	public Matrix(int n) {
		if (n < 1)
			throw new ArrayIndexOutOfBoundsException();
		matrix = new Object[n * n];
		this.m = n;
	}

	public Matrix(int n, int m) {
		if (n < 1)
			throw new ArrayIndexOutOfBoundsException();
		if (m < 1)
			throw new ArrayIndexOutOfBoundsException();
		matrix = new Object[n * m];
		this.m = m;
	}

	public void set(int l, int c, T val) {
		if (c >= m)
			throw new ArrayIndexOutOfBoundsException();
		matrix[c + m * l] = val;
	}

	@SuppressWarnings("unchecked")
	public T get(int l, int c) {
		if (c >= m)
			throw new ArrayIndexOutOfBoundsException();
		return (T) matrix[c + m * l];
	}

	public int size() {
		return matrix.length;
	}

	public int maxColumn() {
		return m;
	}

	public int maxLine() {
		return size() / maxColumn();
	}
}
