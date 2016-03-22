package KTHBleMesh;

// Copyright (c) 2015 ERICSSON AB

import java.util.ArrayList;
import java.util.List;

/**
 * BLE mesh packet
 *
 * @author Roman Chirikov
 */
public class BleMeshPacket {

	private int fromAddress;
	private int toAddress;
	private int bitLength;
	private double id;
	private int viaAddress;
	private List<Integer> hops;
	private List<Integer> route;
	private int ttl;
	private boolean isRouteAvail;
	private int type;
	private int previousDestination;
	
	

	/**
	 * Constructs a {@code BleMeshPacket}. TODO Update this auto-generated
	 * comment!
	 * 
	 * @param aId
	 * @param aFromAddress
	 * @param aToAddress
	 * @param aBitLength
	 *
	 */
	public BleMeshPacket(double aId, int aFromAddress, int aToAddress,
			int aBitLength) {
		id = aId;
		bitLength = aBitLength;
		fromAddress = aFromAddress;
		viaAddress = aFromAddress; // address of the last re-transmitter
		toAddress = aToAddress;
		hops = new ArrayList<>();
		route = new ArrayList<>();
		isRouteAvail = false;
		type = 1;
		previousDestination = 0;
	}

	/**
	 * Constructs a {@code BleMeshPacket}. TODO Update this auto-generated
	 * comment!
	 *
	 * @param aPacket
	 */
	public BleMeshPacket(BleMeshPacket aPacket) {
		this.id = aPacket.getId();
		this.fromAddress = aPacket.getFromAddress();
		this.toAddress = aPacket.getToAddress();
		this.bitLength = aPacket.getBitLength();
		this.viaAddress = aPacket.getViaAddress();
		this.hops = new ArrayList<>(aPacket.getHops());
		this.route = new ArrayList<>(aPacket.getRoute());
		this.isRouteAvail = aPacket.isRouteAvail();
		this.type = aPacket.getType();

	}

	/**
	 * Returns the viaAddress.
	 *
	 * @return the viaAddress
	 */
	public int getViaAddress() {
		return viaAddress;
	}

	/**
	 * Returns the ttl.
	 *
	 * @return the ttl
	 */
	public int getTtl() {
		return ttl;
	}

	/**
	 * Sets the ttl.
	 *
	 * @param aTtl
	 *            the ttl
	 */
	public void setTtl(int aTtl) {
		ttl = aTtl;
	}

	/**
	 * Sets the viaAddress.
	 *
	 * @param aViaAddress
	 *            the viaAddress
	 */
	public void setViaAddress(int aViaAddress) {
		viaAddress = aViaAddress;
	}

	/**
	 * Returns the id.
	 *
	 * @return the id
	 */
	public double getId() {
		return id;
	}

	/**
	 * Returns the bitLength.
	 *
	 * @return the bitLength
	 */
	public int getBitLength() {
		return bitLength;
	}

	/**
	 * Returns the fromAddress.
	 *
	 * @return the fromAddress
	 */
	public int getFromAddress() {
		return fromAddress;
	}

	/**
	 * Returns the toAddress.
	 *
	 * @return the toAddress
	 */
	public int getToAddress() {
		return toAddress;
	}

	/**
	 * Returns the hops.
	 *
	 * @return the hops
	 */
	public List<Integer> getHops() { // ArrayList<Integer>
		return hops;
	}

	/**
	 * Sets the hops.
	 *
	 * @param aHops
	 *            the hops
	 */
	public void setHops(List<Integer> aHops) { // ArrayList<Integer>
		hops = aHops;
	}

	/**
	 * Returns the isRouteAvail.
	 *
	 * @return the isRouteAvail
	 */
	public boolean isRouteAvail() {
		return isRouteAvail;
	}

	/**
	 * Sets the isRouteAvail.
	 *
	 * @param aIsRouteAvail
	 *            the isRouteAvail
	 */
	public void setRouteAvail(boolean aIsRouteAvail) {
		isRouteAvail = aIsRouteAvail;
	}

	/**
	 * Returns the route.
	 *
	 * @return the route
	 */
	public List<Integer> getRoute() {
		
		return route;
	}

	/**
	 * Sets the route.
	 *
	 * @param aRoute
	 *            the route
	 */
	public void setRoute(List<Integer> aRoute) {
		route = aRoute;
	}

	/**
	 * Returns the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param aType
	 *            the type
	 */
	public void setType(int aType) {
		type = aType;
	}
	
	/**
	 * This field is used in packets type 2 when a device on the route fails, 
	 *  for informing the source about the intended destination of the packet
	 * @param previousDestination
	 */
	public void setPreviousDestination(int previousDestination) {
		this.previousDestination = previousDestination;
	}
	
	public int getPreviousDestination() {
		return previousDestination;
	}

	// /**
	// * TODO Update this auto-generated comment!
	// *
	// * @return BleMeshPacket
	// */
	// public BleMeshPacket duplicate()
	// {
	// BleMeshPacket newPacket = new BleMeshPacket(id, fromAddress, toAddress,
	// bitLength);
	// newPacket.hops = new ArrayList<>();
	// // TODO: avoid copying?
	// for(int i=0; i<hops.size(); i++)
	// {
	// newPacket.hops.add(hops.get(i));
	// }
	// return newPacket;
	// }

}
