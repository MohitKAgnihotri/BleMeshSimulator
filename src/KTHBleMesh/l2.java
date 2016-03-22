package KTHBleMesh;

import java.util.Random;

class Event implements Runnable, Comparable {
	double time;
	Runnable runnable;

	Event(double time, Runnable aRunnable) {
		this.time = time;
		runnable = aRunnable;
	}

	public boolean lessThan(Comparable y) {
		Event e = (Event) y; // Will throw an exception if y is not an Event
		return this.time <= e.time;
	}

	@Override
	public void run() {
		runnable.run();

	}
}

class Simulator extends AbstractSimulator {

	static Random rnd;
	static double time;
	double endTime;
	

	static double now() {
		return time;
	}

	Simulator(long seed, double simDuration) {
		time = 0.0;
		events = new ListQueue();
		rnd = new Random(seed);
		endTime = simDuration;
	}

	void doAllEvents() {
		Event e;
		while ((e = (Event) events.removeFirst()) != null && time < endTime) {
			if(time > e.time)
				System.out.printf("Something is worng! time=%f eventtime=%f",time,e.time);
			time = e.time;
			e.run();
			//System.out.printf("\n Time = %f", time);
			
		}
	}

	static double getRandom() {
		return rnd.nextDouble();
	}
}
