package KTHBleMesh;



/**
 * TODO Update this auto-generated comment!
 *
 * @author TODO insert full name of main author(s)
 */
public class BleMeshDeviceEnergyProp {

	/**
	 * @return the batterySize
	 */
	public double getBatterySize() {
		return batterySize;
	}

	/**
	 * @param batterySize
	 *            the batterySize to set
	 */
	public void setBatterySize(double batterySize) {
		this.batterySize = batterySize;
	}

	/* These identify the type of energy supply for the Bluetooth device */
	public enum EnergySource {
		/**
		 * Device has a coin-cell battery
		 * {@code BLE_DEV_BATTRERY_NON_RECHARGEABLE}
		 */
		BLE_DEV_BATTRERY_NON_RECHARGEABLE,

		/**
		 * Device has a re-chargeable battery
		 * {@code BLE_DEV_BATTRERY_RECHARGEABLE}
		 */
		BLE_DEV_BATTRERY_RECHARGEABLE,

		/** Device is connected to the main {@code BLE_DEV_CONNECTED_SUPPLY} */
		BLE_DEV_CONNECTED_SUPPLY

	}

	private EnergySource energySourceType;

	private double initEnergyLevel;

	private double batterySize;

	private double batteryVolts;

	private double energyDissipitation;

	private double currEnergyLevel;

	public BleMeshDeviceEnergyProp() {
		energySourceType = EnergySource.BLE_DEV_BATTRERY_NON_RECHARGEABLE;
		initEnergyLevel = 1;
		batterySize = 250;
		batteryVolts = 3;
		energyDissipitation = 1;
		/*
		 * Assuming 1mv as the operating voltage, energy is given by Percentage
		 * of Capacity(%) x maxCapacity (mAH) x Voltage(V) * 3600 (for Joules)
		 */
		if (energySourceType != EnergySource.BLE_DEV_CONNECTED_SUPPLY)
			currEnergyLevel = (initEnergyLevel * batterySize * batteryVolts * 3600) / 100;
		else
			currEnergyLevel = (100 * 3000 * batteryVolts * 3600) / 100;
	}

	/**
	 * Returns the energySourceType.
	 *
	 * @return the energySourceType
	 */
	public EnergySource getEnergySourceType() {
		return energySourceType;
	}

	/**
	 * Sets the energySourceType.
	 *
	 * @param aEnergySourceType
	 *            the energySourceType
	 */
	public void setEnergySourceType(EnergySource aEnergySourceType) {
		energySourceType = aEnergySourceType;
	}

	/**
	 * Returns the currEnergyLevel.
	 *
	 * @return the currEnergyLevel
	 */
	public double getCurrEnergyLevel() {
		return currEnergyLevel;
	}

	/**
	 * Sets the currEnergyLevel.
	 *
	 * @param aD
	 *            the currEnergyLevel
	 */
	public void setCurrEnergyLevel(double aD) {
		currEnergyLevel = aD;
	}

	/**
	 * Decrements the current energy level of the device
	 *
	 */
	public void decrementEnergyLevel() {

		if ((EnergySource.BLE_DEV_BATTRERY_RECHARGEABLE == energySourceType)
				&& (getCurrEnergyLevel() > 0.0)) {
			setCurrEnergyLevel(getCurrEnergyLevel() - energyDissipitation);
		} else if ((EnergySource.BLE_DEV_BATTRERY_NON_RECHARGEABLE == energySourceType)
				&& (getCurrEnergyLevel() > 0.0)) {
			setCurrEnergyLevel(getCurrEnergyLevel() - energyDissipitation);
		}
		if (getCurrEnergyLevel() <= 0.0) {
			System.out.println("Device Dead");
		}
	}

	/**
	 * Decrements the current energy level of the device
	 *
	 */
	public void decrementEnergyLevelRx() {
		if ((EnergySource.BLE_DEV_BATTRERY_RECHARGEABLE == energySourceType)
				&& (getCurrEnergyLevel() > 0.0)) {
			setCurrEnergyLevel(getCurrEnergyLevel() - 1);
		} else if ((EnergySource.BLE_DEV_BATTRERY_NON_RECHARGEABLE == energySourceType)
				&& (getCurrEnergyLevel() > 0.0)) {
			setCurrEnergyLevel(getCurrEnergyLevel() - 1);
		}
		if (getCurrEnergyLevel() <= 0.0) {
			System.out.println("Device Dead");
		}
	}

	/**
	 * Returns the initEnergyLevel.
	 *
	 * @return the initEnergyLevel
	 */
	public double getInitEnergyLevel() {
		return initEnergyLevel;
	}

	/**
	 * Sets the initEnergyLevel.
	 *
	 * @param aInitEnergyLevel
	 *            the initEnergyLevel
	 */
	public void setInitEnergyLevel(int aInitEnergyLevel) {
		initEnergyLevel = aInitEnergyLevel;
	}

	public boolean isDeviceAlive() {
		return currEnergyLevel > 0 ? true : false;
	}
}
