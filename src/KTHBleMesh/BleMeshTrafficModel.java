// Copyright (c) 2015 ERICSSON AB

package KTHBleMesh;

/**
 * Traffic model for mesh simulator
 *
 * @author Roman Chirikov
 */
public class BleMeshTrafficModel {

	
	private int packetSize;

	// frequency of packets generated from the device
	private double period; 

	
	private double delay;

	
	private int destination;

	private boolean isDestSet;

	
	private int nrofPackets;

	
	private int ttl;

	@SuppressWarnings("unused")
	private BleMeshDevice device;
	private int packetCounter = 0;

	

	void initTraffic() {
		if ((destination != -1)
				&& (packetCounter < nrofPackets && nrofPackets > 0 || nrofPackets == 0)) {

			double packetId = Simulator.getRandom();

			BleMeshPacket packet = new BleMeshPacket(packetId, device
					.getDevTrafficChar().getAddress(), destination, packetSize);
			if (packetCounter == 0) {
				packet.setType(0);
			} else {
				packet.setType(1);
			}

			packet.setTtl(ttl);

			device.SendPacket(packet);
			packetCounter++;

			/** Init new traffic - schedule another event */
			Event e = new Event(period + Simulator.now(), new Runnable() {
				@Override
				public void run() {
					initTraffic();
				}
			});

			Simulator.insert(e);

		}
	}

	

	/**
	 * Constructs a {@code BleMeshTrafficModel}.
	 *
	 * @param aParent
	 * @param aName
	 */
	public BleMeshTrafficModel(BleMeshDevice aDevice) {

		device = aDevice;
		/** Init new traffic - schedule an event */
		Event e = new Event(delay + 7 + Simulator.now(), new Runnable() {
			@Override
			public void run() {
				initTraffic();
			}
		});

		Simulator.insert(e);
	}

	/**
	 * Returns the destination.
	 *
	 * @return the destination
	 */
	public int getDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 *
	 * @param aDestination
	 *            the destination
	 */
	public void setDestination(int aDestination) {
		destination = aDestination;
	}

	/**
	 * Returns the isDestSet.
	 *
	 * @return the isDestSet
	 */
	public boolean isDestSet() {
		return isDestSet;
	}

	/**
	 * Sets the isDestSet.
	 *
	 * @param aIsDestSet
	 *            the isDestSet
	 */
	public void setDestSet(boolean aIsDestSet) {
		isDestSet = aIsDestSet;
	}

	/**
	 * @return the period
	 */
	public double getPeriod() {
		return period;
	}

	/**
	 * @param period
	 *            the period to set
	 */
	public void setPeriod(double period) {
		this.period = period;
	}

	/**
	 * @return the delay
	 */
	public double getDelay() {
		return delay;
	}

	/**
	 * @param delay
	 *            the delay to set
	 */
	public void setDelay(double delay) {
		this.delay = delay;
	}

	/**
	 * @return the nrofPackets
	 */
	public int getNrofPackets() {
		return nrofPackets;
	}

	/**
	 * @param nrofPackets
	 *            the nrofPackets to set
	 */
	public void setNrofPackets(int nrofPackets) {
		this.nrofPackets = nrofPackets;
	}

}
