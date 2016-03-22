// Copyright (c) 2015 ERICSSON AB

package KTHBleMesh;

/**
 * Class representing a BLE mesh transmission
 *
 * @author Roman Chirikov
 */
public class BleMeshTransmission {

	private double transmissionId;
	private BleMeshPacket packet;
	private int channelNumber;
	private BleMeshDevice listener;
	private double rxPowerdBm;
	private double sinr; // unitless
	private boolean successFlag;
	private int length;
	private double duration;

	/**
	 * Constructs a {@code BleMeshTransmission}.
	 *
	 * @param aTransmissionId
	 * @param aPacket
	 * @param aLength
	 * @param aChannelNumber
	 * @param aListener
	 * @param aRxPowerdBm
	 * @param aSinr
	 */
	public BleMeshTransmission(double aTransmissionId, BleMeshPacket aPacket,
			int aLength, int aChannelNumber, BleMeshDevice aListener,
			double aRxPowerdBm, double aSinr) {
		transmissionId = aTransmissionId;
		packet = aPacket;
		channelNumber = aChannelNumber;
		listener = aListener;
		rxPowerdBm = aRxPowerdBm;
		sinr = aSinr;
		successFlag = false;
		length = aLength;
		duration = (double) length / 1000000;
	}

	/**
	 * Returns the packet.
	 *
	 * @return the packet
	 */
	public BleMeshPacket getPacket() {
		return packet;
	}

	/**
	 * Returns the channelNumber.
	 *
	 * @return the channelNumber
	 */
	public int getChannelNumber() {
		return channelNumber;
	}

	/**
	 * Returns the successFlag.
	 *
	 * @return the successFlag
	 */
	public boolean getSuccessFlag() {
		return successFlag;
	}

	/**
	 * Sets the successFlag.
	 *
	 * @param aSuccessFlag
	 *            the successFlag
	 */
	public void setSuccessFlag(boolean aSuccessFlag) {
		successFlag = aSuccessFlag;
	}

	/**
	 * Returns the duration.
	 *
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * Returns the listener.
	 *
	 * @return the listener
	 */
	public BleMeshDevice getListener() {
		return listener;
	}

	/**
	 * Returns the rxPowerdBm.
	 *
	 * @return the rxPowerdBm
	 */
	public double getRxPowerdBm() {
		return rxPowerdBm;
	}

	/**
	 * Returns the sinr.
	 *
	 * @return the sinr
	 */
	public double getSinr() {
		return sinr;
	}

	/**
	 * Sets the sinr.
	 *
	 * @param aSinr
	 *            the sinr
	 */
	public void setSinr(double aSinr) {
		sinr = aSinr;
	}

	/**
	 * Returns the transmissionId.
	 *
	 * @return the transmissionId
	 */
	public double getTransmissionId() {
		return transmissionId;
	}

}
