package KTHBleMesh;

// Copyright (c) 2015 ERICSSON AB

/**
 * TODO Update this auto-generated comment!
 *
 * @author Mohit Agnihotri
 */
public class BleMeshDeviceLink {

	private BleMeshDevice linkDevice;
	private int linkColour;
	private int isContacted;

	/**
	 * Constructs a {@code BleMeshDeviceLink}. TODO Update this auto-generated
	 * comment!
	 *
	 * @param aLinkDevice
	 * @param aLinkColour
	 * @param aIsContacted
	 */
	public BleMeshDeviceLink(BleMeshDevice aLinkDevice, int aLinkColour,
			int aIsContacted) {
		linkDevice = aLinkDevice;
		linkColour = aLinkColour;
		isContacted = aIsContacted;
	}

	/**
	 * Returns the linkDevice.
	 *
	 * @return the linkDevice
	 */
	public BleMeshDevice getLinkDevice() {
		return linkDevice;
	}

	/**
	 * Sets the linkDevice.
	 *
	 * @param aLinkDevice
	 *            the linkDevice
	 */
	public void setLinkDevice(BleMeshDevice aLinkDevice) {
		linkDevice = aLinkDevice;
	}

	/**
	 * Returns the linkColour.
	 *
	 * @return the linkColour
	 */
	public int getLinkColour() {
		return linkColour;
	}

	/**
	 * Sets the linkColour.
	 *
	 * @param aLinkColour
	 *            the linkColour
	 */
	public void setLinkColour(int aLinkColour) {
		linkColour = aLinkColour;
	}

	/**
	 * Returns the isContacted.
	 *
	 * @return the isContacted
	 */
	public int getIsContacted() {
		return isContacted;
	}

	/**
	 * Sets the isContacted.
	 *
	 * @param aIsContacted
	 *            the isContacted
	 */
	public void setIsContacted(int aIsContacted) {
		isContacted = aIsContacted;
	}

}
