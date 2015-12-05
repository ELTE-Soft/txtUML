package hu.elte.txtuml.utils;

public class Pair<F, S> {

	private final F first;
	private final S second;

	public static <F, S> Pair<F, S> of(F first, S second) {
		return new Pair<F, S>(first, second);
	}
	
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		final int prime = 10007;
		int result = prime + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Pair)) {
			return false;
		}
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (first == null ? other.first != null : !first
				.equals(other.first)) {
			return false;
		}
		if (second == null ? other.second != null : !second
				.equals(other.second)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "<" + first + ", " + second + ">";
	}

}
