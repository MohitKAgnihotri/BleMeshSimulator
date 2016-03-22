package KTHBleMesh;



/**
 * TODO Update this auto-generated comment!
 *
 * @author TODO insert full name of main author(s)
 */
public class BleMeshDeviceRadioProp {

	/* Possible Device power class */
	private final static int BLE_DEV_POWER_MODE_1 = 1;
	private final static int BLE_DEV_POWER_MODE_2 = 2;
	private final static int BLE_DEV_POWER_MODE_3 = 3;

	// @Parameter(defaultValue = "3", description =
	// "Power class of Bluetooth device")
	private int powerClass;

	// @Parameter(defaultValue = "1", description =
	// "Current transmission power for the device")
	private int currTxPower;

	// @Parameter(defaultValue = "0", description =
	// "Fixed energy cost for a transmission")
	private int fixedTxCost;

	// @Parameter(defaultValue = "0", description =
	// "Per byte energy cost for a transmission")
	private int byteTxCost;

	// @Parameter(defaultValue = "0", description =
	// "Fixed energy cost for a reception")
	private int fixedRxCost;

	// @Parameter(defaultValue = "0", description =
	// "Per byte energy cost for a reception")
	private int byteRxCost;

	// @Parameter(defaultValue = "2", description =
	// "Number of RF chains (separate Rx and Tx)")
	private int nrofRfChains;

	// @Parameter(defaultValue = "0.001", description =
	// "Minimal offset before advertizing")
	private double minOffset;

	// @Parameter(defaultValue = "0.002", description =
	// "Maximal offset before advertizing")
	private double maxOffset;

	// @Parameter(defaultValue = "10", description = "Effective TxRx Distance",
	// unit="m")
	private double maxDistance;

	/**
	 * Constructs a {@code BleMeshDeviceRadioProp}. TODO Update this
	 * auto-generated comment!
	 *
	 * @param aPowerClass
	 * @param aCurrTxPower
	 * @param aFixedTxCost
	 * @param aByteTxCost
	 * @param aFixedRxCost
	 * @param aByteRxCost
	 * @param aNrofRfChains
	 * @param aMinOffset
	 * @param aMaxOffset
	 */
	// public BleMeshDeviceRadioProp(Block aParent, String aName) {
	// super(aParent, aName);
	// }

	/**
	 * Returns the maxDistance.
	 *
	 * @return the maxDistance
	 */
	public double getMaxDistance() {
		return maxDistance;
	}

	/**
	 * Sets the maxDistance.
	 *
	 * @param aMaxDistance
	 *            the maxDistance
	 */
	public void setMaxDistance(double aMaxDistance) {
		maxDistance = aMaxDistance;
	}

	/**
	 * Returns the powerClass.
	 *
	 * @return the powerClass
	 */
	public int getPowerClass() {
		return powerClass;
	}

	/**
	 * Sets the powerClass.
	 *
	 * @param aPowerClass
	 *            the powerClass
	 */
	public void setPowerClass(int aPowerClass) {
		powerClass = aPowerClass;
	}

	/**
	 * Returns the currTxPower.
	 *
	 * @return the currTxPower
	 */
	public int getCurrTxPower() {
		return currTxPower;
	}

	/**
	 * Sets the currTxPower.
	 *
	 * @param aCurrTxPower
	 *            the currTxPower
	 */
	public void setCurrTxPower(int aCurrTxPower) {
		currTxPower = aCurrTxPower;
	}

	/**
	 * Returns the fixedTxCost.
	 *
	 * @return the fixedTxCost
	 */
	public int getFixedTxCost() {
		return fixedTxCost;
	}

	/**
	 * Sets the fixedTxCost.
	 *
	 * @param aFixedTxCost
	 *            the fixedTxCost
	 */
	public void setFixedTxCost(int aFixedTxCost) {
		fixedTxCost = aFixedTxCost;
	}

	/**
	 * Returns the byteTxCost.
	 *
	 * @return the byteTxCost
	 */
	public int getByteTxCost() {
		return byteTxCost;
	}

	/**
	 * Sets the byteTxCost.
	 *
	 * @param aByteTxCost
	 *            the byteTxCost
	 */
	public void setByteTxCost(int aByteTxCost) {
		byteTxCost = aByteTxCost;
	}

	/**
	 * Returns the fixedRxCost.
	 *
	 * @return the fixedRxCost
	 */
	public int getFixedRxCost() {
		return fixedRxCost;
	}

	/**
	 * Sets the fixedRxCost.
	 *
	 * @param aFixedRxCost
	 *            the fixedRxCost
	 */
	public void setFixedRxCost(int aFixedRxCost) {
		fixedRxCost = aFixedRxCost;
	}

	/**
	 * Returns the byteRxCost.
	 *
	 * @return the byteRxCost
	 */
	public int getByteRxCost() {
		return byteRxCost;
	}

	/**
	 * Sets the byteRxCost.
	 *
	 * @param aByteRxCost
	 *            the byteRxCost
	 */
	public void setByteRxCost(int aByteRxCost) {
		byteRxCost = aByteRxCost;
	}

	/**
	 * Returns the nrofRfChains.
	 *
	 * @return the nrofRfChains
	 */
	public int getNrofRfChains() {
		return nrofRfChains;
	}

	/**
	 * Sets the nrofRfChains.
	 *
	 * @param aNrofRfChains
	 *            the nrofRfChains
	 */
	public void setNrofRfChains(int aNrofRfChains) {
		nrofRfChains = aNrofRfChains;
	}

	/**
	 * Returns the minOffset.
	 *
	 * @return the minOffset
	 */
	public double getMinOffset() {
		return minOffset;
	}

	/**
	 * Sets the minOffset.
	 *
	 * @param aMinOffset
	 *            the minOffset
	 */
	public void setMinOffset(double aMinOffset) {
		minOffset = aMinOffset;
	}

	/**
	 * Returns the maxOffset.
	 *
	 * @return the maxOffset
	 */
	public double getMaxOffset() {
		return maxOffset;
	}

	/**
	 * Sets the maxOffset.
	 *
	 * @param aMaxOffset
	 *            the maxOffset
	 */
	public void setMaxOffset(double aMaxOffset) {
		maxOffset = aMaxOffset;
	}

}
