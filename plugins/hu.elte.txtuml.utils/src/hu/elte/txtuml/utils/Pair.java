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
		return first.hashCode() ^ second.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Pair))
			return false;
		Pair<?, ?> other = (Pair<?, ?>) obj;
		return this.first.equals(other.getFirst())
				&& this.second.equals(other.getSecond());
	}

}
