package KTHBleMesh;

// Copyright (c) 2015 ERICSSON AB

import java.util.ArrayList;
import java.util.List;

import KTHBleMesh.BleMeshDeviceEnergyProp.EnergySource;

/**
 * TODO Update this auto-generated comment!
 *
 * @author Mohit Agnihotri
 */
public class BleMeshTopologyCtrlBlock {

	/**
	 * Currently a device can be part of just two piconets.
	 */

	private List<BleMeshDevicePiconet> devPiconet;
	private List<BleMeshDeviceLink> devPiconetNeighbours;

	private List<BleMeshDeviceLink> devNeighbours;

	private int isAllMasterContacted;
	private int capacity;
	private boolean isRelay;
	private double energyProfile;

	/**
	 * Returns the isAllMasterContacted.
	 *
	 * @return the isAllMasterContacted
	 */
	public int getIsAllMasterContacted() {
		return isAllMasterContacted;
	}

	/**
	 * Sets the isAllMasterContacted.
	 *
	 * @param aIsAllMasterContacted
	 *            the isAllMasterContacted
	 */
	public void setIsAllMasterContacted(int aIsAllMasterContacted) {
		isAllMasterContacted = aIsAllMasterContacted;
	}



	/**
	 * Constructs a {@code BleMeshTopologyCtrlBlock}. TODO Update this
	 * auto-generated comment!
	 *
	 * @param aParent
	 * @param aName
	 */
	public BleMeshTopologyCtrlBlock() {
		devPiconet = new ArrayList<BleMeshDevicePiconet>(2);
		devNeighbours = new ArrayList<BleMeshDeviceLink>();
		devPiconetNeighbours = new ArrayList<BleMeshDeviceLink>();
		isAllMasterContacted = 0;
		capacity = 7;
		isRelay = false;
		energyProfile = 0.0;
	}

	/**
	 * Returns the devPiconet.
	 *
	 * @return the devPiconet
	 */
	public List<BleMeshDevicePiconet> getDevPiconet() {
		return devPiconet;
	}

	/**
	 * Sets the devPiconet.
	 *
	 * @param aDevPiconet
	 *            the devPiconet
	 */
	public void setDevPiconet(List<BleMeshDevicePiconet> aDevPiconet) {
		devPiconet = aDevPiconet;
	}

	/**
	 * Returns the devNeighbours.
	 *
	 * @return the devNeighbours
	 */
	public List<BleMeshDeviceLink> getDevNeighbours() {
		return devNeighbours;
	}

	/**
	 * Sets the devNeighbours.
	 *
	 * @param aDevNeighbours
	 *            the devNeighbours
	 */
	public void setDevNeighbours(List<BleMeshDeviceLink> aDevNeighbours) {
		devNeighbours = aDevNeighbours;
	}

	/**
	 * Returns the capacity.
	 *
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Sets the capacity.
	 *
	 * @param aCapacity
	 *            the capacity
	 */
	public void setCapacity(int aCapacity) {
		if (aCapacity > 0) {
			capacity = aCapacity;
		}
	}

	/**
	 * TODO This function receives the list of possible neighbors from the
	 * global scan function and populates the local links.
	 *
	 * @param neigbourList
	 */
	public void updateNeighbourList(List<BleMeshDevice> neigbourList) {
		for (BleMeshDevice aNeighbour : neigbourList) {
			devNeighbours.add(new BleMeshDeviceLink(aNeighbour, 0, 0));
		}
	}

	/**
	 * Returns the devPiconetNeighbours.
	 *
	 * @return the devPiconetNeighbours
	 */
	public List<BleMeshDeviceLink> getDevPiconetNeighbours() {
		return devPiconetNeighbours;
	}

	/**
	 * Sets the devPiconetNeighbours.
	 *
	 * @param aDevPiconetNeighbours
	 *            the devPiconetNeighbours
	 */
	public void setDevPiconetNeighbours(
			List<BleMeshDeviceLink> aDevPiconetNeighbours) {
		devPiconetNeighbours = aDevPiconetNeighbours;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param alinkMasterDevice
	 * @param alinkSlaveDevice
	 * @param contactStatus
	 * @param linkColor
	 * @param devRole
	 * @param piconetId
	 */
	public void setLocalLinkStatus(BleMeshDevice alinkMasterDevice,
			BleMeshDevice alinkSlaveDevice, int contactStatus, int linkColor,
			int devRole) {
		for (BleMeshDeviceLink alink : devNeighbours) {
			if (alink.getLinkDevice().equals(alinkSlaveDevice)) {
				alink.setIsContacted(contactStatus);
				alink.setLinkColour(linkColor);
				if (linkColor == 1) {
					if (devRole == 1) {
						AddSlavetoMaster(alinkMasterDevice, alinkSlaveDevice,
								devRole);
					} else if (devRole == 2) {
						AddMastertoSlave(alinkMasterDevice, alinkSlaveDevice,
								devRole);
					}
				}
			}
		}
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aDevice
	 * @return
	 */
	public int getHigherIdNeighbourDeviceLinkStatus(BleMeshDevice aDevice) {
		for (BleMeshDeviceLink alink : devNeighbours) {
			if (!isDeviceLower(alink.getLinkDevice(), aDevice)
					&& alink.getLinkColour() == 0)
				return 0;
		}
		return 1;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aDevice
	 * @return
	 */
	public List<BleMeshDevice> getLowerIdNeighbourDevice(BleMeshDevice aDevice) {
		List<BleMeshDevice> neighbourList = new ArrayList<BleMeshDevice>();
		for (BleMeshDeviceLink alink : devNeighbours) {
			if (isDeviceLower(alink.getLinkDevice(), aDevice)) {
				neighbourList.add(alink.getLinkDevice());
			}

		}

		return neighbourList;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @return
	 */
	public int isLowerIdNeighbourDevice(BleMeshDevice aDevice) {
		for (BleMeshDeviceLink alink : devPiconetNeighbours) {
			if (alink.getIsContacted() == 0
					&& isDeviceLower(alink.getLinkDevice(), aDevice)) {
				return 0;
			}

		}
		return 1;
	}

	private void AddSlavetoMaster(BleMeshDevice linkMaster,
			BleMeshDevice linkSlave, int devRole) {
		/*
		 * This function is aimed to add slave to an master. There can be two
		 * possibilities: Piconet with the master exist : Just add the slave the
		 * list Piconet with master doesn't exist: Create the piconet and add
		 * slave to the list
		 */
		// if (piconetId >= devPiconet.size())
		// {
		// devPiconet.add(piconetId,new BleMeshDevicePiconet(devRole,
		// linkMaster));
		// devPiconet.get(piconetId).getDevSlaves().add(linkSlave);
		// GraphLogging.log(this,linkMaster.getDevTrafficChar().getAddress(),linkSlave.getDevTrafficChar().getAddress());
		// }
		// else
		// {
		// if(devPiconet.get(piconetId).getDevMaster().equals(linkMaster))
		// {
		// devPiconet.get(piconetId).getDevSlaves().add(linkSlave);
		// GraphLogging.log(this,linkMaster.getDevTrafficChar().getAddress(),linkSlave.getDevTrafficChar().getAddress());
		// }
		// }

		Boolean isPiconetExist = false;
		for (BleMeshDevicePiconet aPiconet : devPiconet) {
			if (aPiconet.getDevMaster().equals(linkMaster)) {
				aPiconet.getDevSlaves().add(linkSlave);
				// GraphLogging.log(this,linkMaster.getDevTrafficChar().getAddress(),linkMaster.getDevUniqueId(),
				// linkSlave.getDevTrafficChar().getAddress(),linkSlave.getDevUniqueId());
				isPiconetExist = true;
				break;
			}
		}

		if (!isPiconetExist) {
			BleMeshDevicePiconet piconet = new BleMeshDevicePiconet(devRole,
					linkMaster);
			piconet.getDevSlaves().add(linkSlave);
			devPiconet.add(piconet);
			// GraphLogging.log(this,linkMaster.getDevTrafficChar().getAddress(),linkMaster.getDevUniqueId(),
			// linkSlave.getDevTrafficChar().getAddress(),linkSlave.getDevUniqueId());
		}

	}

	private void AddMastertoSlave(BleMeshDevice linkMaster,
			BleMeshDevice linkSlave, int devRole) {

		devPiconet.add(new BleMeshDevicePiconet(devRole, linkSlave));
		devPiconet.get(devPiconet.size() - 1).getDevSlaves().add(linkMaster);
	}

	public List<BleMeshDevice> getHigerIdPiconetNeighbours(BleMeshDevice aDevice) {
		int maxId = 0xFFFF;
		List<BleMeshDevice> higherIdPiconetNeighbours = new ArrayList<BleMeshDevice>();
		if (devPiconetNeighbours.size() > 0) {
			for (BleMeshDeviceLink alink : devPiconetNeighbours) {
				if (alink.getLinkDevice().getDevUniqueId() < maxId
						&& (!isDeviceLower(alink.getLinkDevice(), aDevice))) {
					higherIdPiconetNeighbours.add(alink.getLinkDevice());
				}
			}
		}

		return higherIdPiconetNeighbours;
	}
	
	public List<BleMeshDevice> getAllPiconetNeighbours(BleMeshDevice aDevice) {
		List<BleMeshDevice> piconetNeighbours = new ArrayList<BleMeshDevice>();
		if (devPiconetNeighbours.size() > 0) {
			for (BleMeshDeviceLink alink : devPiconetNeighbours) {
				if (!alink.getLinkDevice().equals(aDevice))
					piconetNeighbours.add(alink.getLinkDevice());
			}
		}
		return piconetNeighbours;
	}

	/*
	 * This function implements the default gateway selection as described in
	 * the algorithm
	 */
	public BleMeshDevGatewayTable getOptimalGateway(
			BleMeshDevice aNeighPiconetMaster, int gatewaySelRule) {
		BleMeshDevGatewayTable optimalEntry = null;
		List<BleMeshDevGatewayTable> possibleGateways = new ArrayList<BleMeshDevGatewayTable>();

		for (BleMeshDevGatewayTable aRow : getDevPiconet().get(0)
				.getGatewaytable()) {
			if (aRow.getPicRemoteMaster().equals(aNeighPiconetMaster)
					&& aRow.getPicLocalGatewayDev().getDevMiddleware()
							.getTopologyCtrlBlock().getCapacity() > 0
					&& aRow.getPicRemoteGatewayDev().getDevMiddleware()
							.getTopologyCtrlBlock().getCapacity() > 0
			// &&
			// aRow.getPicLocalGatewayDev().getDevMiddleware().getTopologyCtrlBlock().isRelay()
			// == false
			// &&
			// aRow.getPicRemoteGatewayDev().getDevMiddleware().getTopologyCtrlBlock().isRelay()
			// == false
			) {
				aRow.setRelayEnergyProf((aRow.getPicLocalGatewayDev()
						.getDevMiddleware().getTopologyCtrlBlock()
						.getEnergyProfile() + aRow.getPicRemoteGatewayDev()
						.getDevMiddleware().getTopologyCtrlBlock()
						.getEnergyProfile()) / 2);

				aRow.setPicLocalGatewayCapacity(aRow.getPicLocalGatewayDev()
						.getDevMiddleware().getTopologyCtrlBlock()
						.getCapacity());
				aRow.setPicRemoteGatewayCapacity(aRow.getPicRemoteGatewayDev()
						.getDevMiddleware().getTopologyCtrlBlock()
						.getCapacity());
				possibleGateways.add(aRow);
			}
		}

		if (gatewaySelRule == 1)
			possibleGateways.sort(BleMeshDevGatewayTable.BleMeshGatewayRuleDef);
		else if (gatewaySelRule == 2)
			possibleGateways
					.sort(BleMeshDevGatewayTable.BleMeshGatewayRuleDefEnergy);
		else if (gatewaySelRule == 3)
			possibleGateways
					.sort(BleMeshDevGatewayTable.BleMeshGatewayRuleOnlyEnergy);
		else if (gatewaySelRule == 4)
			possibleGateways.sort(BleMeshDevGatewayTable.BleMeshGatewayRuleHop);

		if (possibleGateways.size() > 0) {
			optimalEntry = possibleGateways.get(0);
		}

		return optimalEntry;
	}

	public void updateGatewayEnergProfile(BleMeshDevGatewayTable optimalEntry) {
		if (optimalEntry.getPicLocalGatewayDev().getDevEnergyChar()
				.getEnergySourceType() != EnergySource.BLE_DEV_CONNECTED_SUPPLY) {
			optimalEntry.getPicLocalGatewayDev().getDevMiddleware().getTopologyCtrlBlock().setEnergyProfile(
					optimalEntry.getPicLocalGatewayDev().getDevMiddleware().getTopologyCtrlBlock().getEnergyProfile()
							* 0.66);
		}

			if (optimalEntry.getPicRemoteGatewayDev().getDevEnergyChar()
					.getEnergySourceType() != EnergySource.BLE_DEV_CONNECTED_SUPPLY) {
				optimalEntry
						.getPicRemoteGatewayDev()
						.getDevMiddleware()
						.getTopologyCtrlBlock()
						.setEnergyProfile(
								optimalEntry.getPicRemoteGatewayDev()
										.getDevMiddleware()
										.getTopologyCtrlBlock()
										.getEnergyProfile() * 0.66);

		}
	}

	public void updateGatewayMasterCapacity(BleMeshDevGatewayTable optimalEntry) {
		if (optimalEntry.getPicRole() == 1) {
			optimalEntry.getPicLocalGatewayDev().getDevMiddleware().getTopologyCtrlBlock().setCapacity(
					optimalEntry.getPicLocalGatewayDev().getDevMiddleware().getTopologyCtrlBlock().getCapacity() - 1);
		} else {
			optimalEntry.getPicLocalMaster().getTxManager()
					.decrementRemoteDevCapacity(optimalEntry.getPicRemoteGatewayDev());
		}
		return;
	}
	
	void UpdateLocalGatewayCapacity(BleMeshDevice aMaster,
			BleMeshDevice aGatewayDev) {
		if ((aMaster == null) || (aGatewayDev == null))
			return;

		BleMeshTopologyCtrlBlock currTopologyCtr = aMaster.getDevMiddleware()
				.getTopologyCtrlBlock();
		for (BleMeshDevGatewayTable aRow : currTopologyCtr.getDevPiconet()
				.get(0).getGatewaytable()) {
			if (aRow.getPicLocalMaster().equals(aMaster)
					&& aRow.getPicLocalGatewayDev().equals(aGatewayDev)
					&& aRow.getPicLocalGatewayCapacity() > 0) {
				aRow.setPicLocalGatewayCapacity(aRow
						.getPicLocalGatewayCapacity() - 1);
			}

		}

	}

	void UpdateLocalGateway(BleMeshDevice aMaster, BleMeshDevice aGatewayDev) {
		if ((aMaster == null) || (aGatewayDev == null))
			return;
		ArrayList<BleMeshDevGatewayTable> RemoveList = new ArrayList<BleMeshDevGatewayTable>();

		BleMeshTopologyCtrlBlock currTopologyCtr = aMaster.getDevMiddleware()
				.getTopologyCtrlBlock();
		for (BleMeshDevGatewayTable aRow : currTopologyCtr.getDevPiconet()
				.get(0).getGatewaytable()) {
			if (aRow.getPicLocalMaster().equals(aMaster)
					&& aRow.getPicLocalGatewayDev().equals(aGatewayDev)) {
				RemoveList.add(aRow);
			}

		}
		currTopologyCtr.getDevPiconet().get(0).getGatewaytable()
				.removeAll(RemoveList);
	}

	void updateLocalGatewayEnergy(BleMeshDevice aMaster,
			BleMeshDevice aGatewayDev) {
		if ((aMaster == null) || (aGatewayDev == null))
			return;

		BleMeshTopologyCtrlBlock currTopologyCtr = aMaster.getDevMiddleware()
				.getTopologyCtrlBlock();
		for (BleMeshDevGatewayTable aRow : currTopologyCtr.getDevPiconet()
				.get(0).getGatewaytable()) {
			if (aRow.getPicLocalMaster().equals(aMaster)
					&& aRow.getPicLocalGatewayDev().equals(aGatewayDev)) {
				aRow.setRelayEnergyProf(aRow.getRelayEnergyProf() * 0.66);
			}

		}
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param alinkSlaveDevice
	 * @param contactStatus
	 * @param linkColor
	 */
	public void setInterconnectLinkStatus(BleMeshDevice alinkSlaveDevice,
			int contactStatus, int linkColor) {
		for (BleMeshDeviceLink alink : devPiconetNeighbours) {
			if (alink.getLinkDevice().equals(alinkSlaveDevice)) {
				alink.setIsContacted(contactStatus);
				alink.setLinkColour(linkColor);
			}
		}
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param alinkMasterDevice
	 * @param alinkSlaveDevice
	 */
	public void createLocalInterconnect(BleMeshDevice alinkMasterDevice,
			BleMeshDevice alinkSlaveDevice, int devRole) {
		if (devRole == 1)
			AddSlavetoMaster(alinkMasterDevice, alinkSlaveDevice, devRole);
		else if (devRole == 2)
			AddMastertoSlave(alinkMasterDevice, alinkSlaveDevice, devRole);
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aPiconetMaster
	 */
	public void removePiconetNeighbour(BleMeshDevice aPiconetMaster) {
		if (devPiconetNeighbours.size() > 0) {
			for (BleMeshDeviceLink aPiconetNeigbour : devPiconetNeighbours) {
				if (aPiconetNeigbour.getLinkDevice().equals(aPiconetMaster)) {
					devPiconetNeighbours.remove(aPiconetNeigbour);
					break;
				}
			}
		}
	}

	public void setRelayNode(BleMeshDevice aPiconetMaster, BleMeshDevice aRelay) {
		/* Find a piconet with the passed master device */
		for (BleMeshDevicePiconet aPiconet : getDevPiconet()) {
			if (aPiconet.getDevMaster().equals(aPiconetMaster)
					&& ((aPiconet.getDevMaster().equals(aRelay)) || (aPiconet
							.getDevSlaves().contains(aRelay)))) {
				if (!aPiconet.getRelays().contains(aRelay))
					aPiconet.getRelays().add(aRelay);
			}
		}
	}

	private boolean isDeviceLower(BleMeshDevice a, BleMeshDevice b) {
		if (a.getDevUniqueId() == b.getDevUniqueId())
			return a.getDevTrafficChar().getAddress() < b.getDevTrafficChar()
					.getAddress() ? true : false;
		else
			return a.getDevUniqueId() < b.getDevUniqueId() ? true : false;
	}

	/**
	 * Returns the isRelay.
	 *
	 * @return the isRelay
	 */
	public boolean isRelay() {
		return isRelay;
	}

	/**
	 * Sets the isRelay.
	 *
	 * @param aIsRelay
	 *            the isRelay
	 */
	public void setRelay(boolean aIsRelay) {
		isRelay = aIsRelay;
	}

	/**
	 * Returns the energyProfile.
	 *
	 * @return the energyProfile
	 */
	public double getEnergyProfile() {
		return energyProfile;
	}

	/**
	 * Sets the energyProfile.
	 *
	 * @param aEnergyProfile
	 *            the energyProfile
	 */
	public void setEnergyProfile(double aEnergyProfile) {
		energyProfile = aEnergyProfile;
	}

}
