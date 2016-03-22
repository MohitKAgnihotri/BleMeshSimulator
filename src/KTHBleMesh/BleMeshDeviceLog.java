package KTHBleMesh;

import KTHBleMesh.BleMeshDeviceEnergyProp.EnergySource;

public class BleMeshDeviceLog {

		double time;
		int deviceId;
		EnergySource  devEneSrc;
		double devInitEnergy;
		double currEnergy;
		boolean isDeviceCritical;
		boolean isDeviceAlive;
		boolean isSecondFailure;

		public BleMeshDeviceLog(double time, int deviceId, EnergySource devEneSrc, double devInitEnergy,
				double currEnergy, boolean isDeviceCritical, boolean isDeviceAlive, boolean isSecondFailure) {
			super();
			this.time = time;
			this.deviceId = deviceId;
			this.devEneSrc = devEneSrc;
			this.devInitEnergy = devInitEnergy;
			this.currEnergy = currEnergy;
			this.isDeviceCritical = isDeviceCritical;
			this.isDeviceAlive = isDeviceAlive;
			this.isSecondFailure = isSecondFailure;
		}
		public double getTime() {
			return time;
		}
		public void setTime(double time) {
			this.time = time;
		}
		public int getDeviceId() {
			return deviceId;
		}
		public void setDeviceId(int deviceId) {
			this.deviceId = deviceId;
		}
		public EnergySource getDevEneSrc() {
			return devEneSrc;
		}
		public void setDevEneSrc(EnergySource devEneSrc) {
			this.devEneSrc = devEneSrc;
		}
		public double getDevInitEnergy() {
			return devInitEnergy;
		}
		public void setDevInitEnergy(double devInitEnergy) {
			this.devInitEnergy = devInitEnergy;
		}
		public double getCurrEnergy() {
			return currEnergy;
		}
		public void setCurrEnergy(double currEnergy) {
			this.currEnergy = currEnergy;
		}
		public boolean isDeviceCritical() {
			return isDeviceCritical;
		}
		public void setDeviceCritical(boolean isDeviceCritical) {
			this.isDeviceCritical = isDeviceCritical;
		}
		public boolean isDeviceAlive() {
			return isDeviceAlive;
		}
		public void setDeviceAlive(boolean isDeviceAlive) {
			this.isDeviceAlive = isDeviceAlive;
		}
		public boolean isSecondFailure() {
			return isSecondFailure;
		}
		public void setSecondFailure(boolean isSecondFailure) {
			this.isSecondFailure = isSecondFailure;
		}
		
}
