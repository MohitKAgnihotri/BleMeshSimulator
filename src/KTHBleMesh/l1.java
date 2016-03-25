package KTHBleMesh;

interface Comparable {
	boolean lessThan(Comparable y);
}

abstract class OrderedSet {
	abstract void insert(Comparable x);

	abstract Comparable removeFirst();

	abstract int size();

	abstract Comparable remove(Comparable x);

	abstract void flushQ();
}

class AbstractSimulator {
	static OrderedSet events;

	static void insert(Event e) {
		events.insert(e);
	}

	static void flushQ() { events.flushQ();}

	Event cancel(Event e) {
		throw new java.lang.RuntimeException("Method not implemented");
	}
}