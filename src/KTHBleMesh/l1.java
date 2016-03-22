package KTHBleMesh;

interface Comparable {
	boolean lessThan(Comparable y);
}

abstract class OrderedSet {
	abstract void insert(Comparable x);

	abstract Comparable removeFirst();

	abstract int size();

	abstract Comparable remove(Comparable x);
}

class AbstractSimulator {
	static OrderedSet events;

	static void insert(Event e) {
		events.insert(e);
	}

	Event cancel(Event e) {
		throw new java.lang.RuntimeException("Method not implemented");
	}
}