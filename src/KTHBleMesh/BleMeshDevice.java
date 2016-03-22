// Copyright (c) 2015 ERICSSON AB

package KTHBleMesh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import KTHBleMesh.BleMeshDeviceEnergyProp.EnergySource;

/**
 * BLE mesh device
 *
 * @author Mohit Agnihotri
 */
public class BleMeshDevice {
	public String bleDeviceName;
	public static ArrayList<Integer> uniqueId = new ArrayList<Integer>();

	/**
	 * Rule used for Gateway Selection
	 *
	 * @author Mohit Agnihotri
	 */
	public enum OptimalGatewayRule {
		/** TODO Based upon the Native Algorithm {@code DEFAULT} */
		DEFAULT,
		/**
		 * Based upon the Native Algorithm, Preference is given to Node with
		 * higher energy in case of multiple options {@code DEFAULT_WITH_ENERGY}
		 */
		DEFAULT_WITH_ENERGY,
		/** Choose Relay node with Maximum energy {@code ENERGY} */
		ENERGY,
		/** Choose Relay node with minimum hops {@code HOPS} */
		HOPS
	}

	ArrayList<Double> rxPackets;
	ArrayList<Double> txPackets;

	private BleMeshTxManager txManager;
	private double processingTime = 0; // time needed by a device for
										// retransmission

	private BleMeshDeviceRadioProp devRadioChar;

	private BleMeshDeviceEnergyProp devEnergyChar;

	private BleMeshDeviceComputationProp devComputationChar;

	private BleMeshDeviceTrafficProp devTrafficChar;

	private BleMeshDeviceMobilityProp devMobilityChar;

	private BleMeshMiddleware devMiddleware;

	private BleMeshTrafficModel trafficModel;
	
	private List<BleMeshDevice> redundantPiconetList;

	/* Parameter for gateway selection algorithm */
	private int gatewaySelRule;

	int devUniqId;

	/* Total number of packets transmitted by the device */
	int packetTxCount;

	/* Total number of packets received at the node */
	int packetRxCount;

	int bridgeCnt;

	class EnergyDissi {
		List<Double> Energy;
		List<Double> Time;

		EnergyDissi() {
			Energy = new ArrayList<Double>();
			Time = new ArrayList<Double>();
		}

		/**
		 * Returns the energy.
		 *
		 * @return the energy
		 */
		public List<Double> getEnergy() {
			return Energy;
		}

		/**
		 * Sets the energy.
		 *
		 * @param aEnergy
		 *            the energy
		 */
		public void setEnergy(List<Double> aEnergy) {
			Energy = aEnergy;
		}

		/**
		 * Returns the time.
		 *
		 * @return the time
		 */
		public List<Double> getTime() {
			return Time;
		}

		/**
		 * Sets the time.
		 *
		 * @param aTime
		 *            the time
		 */
		public void setTime(List<Double> aTime) {
			Time = aTime;
		}

	}

	EnergyDissi EnergyDissipitationProfile;

	/**
	 * Returns the energyDissipitationProfile.
	 *
	 * @return the energyDissipitationProfile
	 */
	public EnergyDissi getEnergyDissipitationProfile() {
		return EnergyDissipitationProfile;
	}

	/**
	 * Sets the energyDissipitationProfile.
	 *
	 * @param aEnergyDissipitationProfile
	 *            the energyDissipitationProfile
	 */
	public void setEnergyDissipitationProfile(
			EnergyDissi aEnergyDissipitationProfile) {
		EnergyDissipitationProfile = aEnergyDissipitationProfile;
	}

	/**
	 * Constructs a {@code BleMeshDevice}.
	 *
	 * @param aParent
	 * @param aName
	 */
	public BleMeshDevice() {
		redundantPiconetList = new ArrayList<BleMeshDevice>();
		devRadioChar = new BleMeshDeviceRadioProp();
		devEnergyChar = new BleMeshDeviceEnergyProp();
		devComputationChar = new BleMeshDeviceComputationProp();
		devTrafficChar = new BleMeshDeviceTrafficProp();
		devMobilityChar = new BleMeshDeviceMobilityProp();
		devMiddleware = new BleMeshMiddleware();
		setTrafficModel(new BleMeshTrafficModel(this));
		devUniqId = GetRandomId();
		packetRxCount = 0;
		packetTxCount = 0;
		rxPackets = new ArrayList<Double>();
		txPackets = new ArrayList<Double>();
		EnergyDissipitationProfile = new EnergyDissi();
		bridgeCnt = 0;

	}

	/**
	 * Receive a transmission
	 *
	 * @param aTransmission
	 */
	public void receive(BleMeshTransmission aTransmission) {
		final BleMeshTransmission transmission = aTransmission;
		final BleMeshPacket packet = transmission.getPacket();

		//if (packet.getType() == 2) {
			//System.out.println("Failed packet received on " + this.getDevUniqueId() + " from " + packet.getFromAddress());				
		//}

		if (devEnergyChar.isDeviceAlive()) {
			
			/* Decrement the energy for the receive operations */
			devEnergyChar.decrementEnergyLevelRx();
			packetRxCount++;
			
			//	System.out.printf("\n [rx_event]:  Time = %f, EnergyChar %s, DeviceId %d, DeviceInitEnergy=%f, DeviceCurrEnergy=%f, isDevAlive=%b, isdevCritical=%b, DevPacketsRx =%d \n",
			// Simulator.now(),
			// this.getDevEnergyChar().getEnergySourceType(),this.getDevUniqueId(),
			//  this.getDevEnergyChar().getInitEnergyLevel(),this.getDevEnergyChar().getCurrEnergyLevel(),
			//  devEnergyChar.isDeviceAlive(), this.isDeviceCritical(), this.getDevMiddleware().getRoutingCtrlBlock().getPacketsRecived()); 
			BleMeshSimulator.simLog.add(
					new BleMeshDeviceLog(
							Simulator.now(), 
							this.getDevUniqueId(), 
							this.getDevEnergyChar().getEnergySourceType(), 
							this.getDevEnergyChar().getInitEnergyLevel(), 
							this.getDevEnergyChar().getCurrEnergyLevel(), 
							this.isDeviceCritical(), devEnergyChar.isDeviceAlive(), false));


			if (this.isDeviceCritical()) {
				// RouteLog.log(this, getTime(),packetTxCount,packetRxCount);
			}

			/*
			 * Check if the following are true: Device is alive Device has not
			 * Transmitted the packet Device has not already routed the packet
			 */
			if (devEnergyChar.isDeviceAlive()) {
				rxPackets.add(packet.getId());

				// do not listen to yourself
				if (packet.getFromAddress() == devTrafficChar.getAddress()
						|| packet.getViaAddress() == devTrafficChar
								.getAddress())
					return;

				// do not process corrupted packet
				if (!transmission.getSuccessFlag())
					return;

				/* Received a packet which needs further routing */
				/* Set the via-address to self-address */

				BleMeshPacket aPacket = aTransmission.getPacket();
				SendPacket(aPacket);
			} else if (devEnergyChar.isDeviceAlive()
					&& (packet.getToAddress() == devTrafficChar.getAddress())
					&& (packet.getType() == 0 || packet.getType() == 10)) {

				// do not listen to yourself
				if (packet.getFromAddress() == devTrafficChar.getAddress()
						|| packet.getViaAddress() == devTrafficChar
								.getAddress())
					return;

				// do not process corrupte+d packet
				if (!transmission.getSuccessFlag())
					return;
				/* Received a packet which needs further routing */
				/* Set the via-address to self-address */

				BleMeshPacket aPacket = aTransmission.getPacket();
				SendPacket(aPacket);
			}

		} else {
			System.out.printf(
					"Dropping packet intended from  %d -> %d  @ Node %d, Failed device %d\n",
					aTransmission.getPacket().getFromAddress(), aTransmission
							.getPacket().getToAddress(), aTransmission
							.getPacket().getViaAddress(), this.getDevUniqueId());
			
			
			// if the device is not alive, inform the source and neighboring piconets about it to find a new route 
			if (!devEnergyChar.isDeviceAlive()) {
				Integer source = aTransmission.getPacket().getFromAddress();
				BleMeshPacket failPacket = new BleMeshPacket(aTransmission.getPacket().getId()+1, this
						.getDevTrafficChar().getAddress(), source, 0);
				List<Integer> aRoute = new ArrayList<Integer>();
				failPacket.setRoute(aRoute);
				failPacket.setRouteAvail(false);
				failPacket.setPreviousDestination(aTransmission.getPacket().getToAddress());
				failPacket.setType(2);
				failPacket.setTtl(1);
				
				this.SendPacket(failPacket);
				
				devEnergyChar.decrementEnergyLevel();
				packetTxCount++;
			}
		}
	}

	/**
	 * Returns the devRadioChar.
	 *
	 * @return the devRadioChar
	 */
	public BleMeshDeviceRadioProp getDevRadioChar() {
		return devRadioChar;
	}

	/**
	 * Sets the devRadioChar.
	 *
	 * @param aDevRadioChar
	 *            the devRadioChar
	 */
	public void setDevRadioChar(BleMeshDeviceRadioProp aDevRadioChar) {
		devRadioChar = aDevRadioChar;
	}

	/**
	 * Returns the devEnergyChar.
	 *
	 * @return the devEnergyChar
	 */
	public BleMeshDeviceEnergyProp getDevEnergyChar() {
		return devEnergyChar;
	}

	/**
	 * Sets the devEnergyChar.
	 *
	 * @param aDevEnergyChar
	 *            the devEnergyChar
	 */
	public void setDevEnergyChar(BleMeshDeviceEnergyProp aDevEnergyChar) {
		devEnergyChar = aDevEnergyChar;
	}

	/**
	 * Returns the devComputationChar.
	 *
	 * @return the devComputationChar
	 */
	public BleMeshDeviceComputationProp getDevComputationChar() {
		return devComputationChar;
	}

	/**
	 * Sets the devComputationChar.
	 *
	 * @param aDevComputationChar
	 *            the devComputationChar
	 */
	public void setDevComputationChar(
			BleMeshDeviceComputationProp aDevComputationChar) {
		devComputationChar = aDevComputationChar;
	}

	/**
	 * Returns the devTrafficChar.
	 *
	 * @return the devTrafficChar
	 */
	public BleMeshDeviceTrafficProp getDevTrafficChar() {
		return devTrafficChar;
	}

	/**
	 * Sets the devTrafficChar.
	 *
	 * @param aDevTrafficChar
	 *            the devTrafficChar
	 */
	public void setDevTrafficChar(BleMeshDeviceTrafficProp aDevTrafficChar) {
		devTrafficChar = aDevTrafficChar;
	}

	/**
	 * Returns the devMobilityChar.
	 *
	 * @return the devMobilityChar
	 */
	public BleMeshDeviceMobilityProp getDevMobilityChar() {
		return devMobilityChar;
	}

	/**
	 * Sets the devMobilityChar.
	 *
	 * @param aDevMobilityChar
	 *            the devMobilityChar
	 */
	public void setDevMobilityChar(BleMeshDeviceMobilityProp aDevMobilityChar) {
		devMobilityChar = aDevMobilityChar;
	}

	/**
	 * Action done when the packet reaches its destination
	 *
	 */
	protected void packetDelivered() {
		// do something
		// System.out.println("Finished");
	}

	/**
	 * Returns the txManager.
	 *
	 * @return the txManager
	 */
	public BleMeshTxManager getTxManager() {
		return txManager;
	}

	/**
	 * Sets the txManager.
	 *
	 * @param aTxManager
	 *            the txManager
	 */
	public void setTxManager(BleMeshTxManager aTxManager) {
		txManager = aTxManager;
	}

	/**
	 * Returns the processingTime.
	 *
	 * @return the processingTime
	 */
	public double getProcessingTime() {
		return processingTime;
	}

	/**
	 * Returns the devMiddleware.
	 *
	 * @return the devMiddleware
	 */
	public BleMeshMiddleware getDevMiddleware() {
		return devMiddleware;
	}

	/**
	 * Sets the devMiddleware.
	 *
	 * @param aDevMiddleware
	 *            the devMiddleware
	 */
	public void setDevMiddleware(BleMeshMiddleware aDevMiddleware) {
		devMiddleware = aDevMiddleware;
	}

	public List<BleMeshDevice> getRedundantPiconetList() {
		return redundantPiconetList;
	}

	public void setRedundantPiconetList(List<BleMeshDevice> redundantPiconetList) {
		this.redundantPiconetList = redundantPiconetList;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 */
	public void setDevMasterContactStatus() {
		int contactFlag = 1;
		for (BleMeshDeviceLink aLink : this.getDevMiddleware()
				.getTopologyCtrlBlock().getDevNeighbours()) {
			if (this.getDevUniqueId() < aLink.getLinkDevice().getDevUniqueId()
					&& aLink.getIsContacted() == 0) {
				contactFlag = 0;
				break;
			}
		}
		this.getDevMiddleware().getTopologyCtrlBlock()
				.setIsAllMasterContacted(contactFlag);
	}

	/**
	 * This function initiates the phase 1 of the topology formation algorithm.
	 *
	 */
	public void construct() {
		int status = this.getDevMiddleware().getTopologyCtrlBlock()
				.getHigherIdNeighbourDeviceLinkStatus(this);
		if (status == 0) {
			Event e = new Event(1, new Runnable() {
				@Override
				public void run() {
					construct();
				}
			});
			Simulator.insert(e);
			return;
		}

		/*
		 * If you are already a slave device, Inform all the SetSlaveLinkStatus
		 * = Green
		 */
		if (this.getDevMiddleware().getTopologyCtrlBlock().getDevPiconet()
				.size() > 0
				&& this.getDevMiddleware().getTopologyCtrlBlock()
						.getDevPiconet().get(0).getDevRole() == 2) {
			List<BleMeshDevice> SlaveList = this.getDevMiddleware()
					.getTopologyCtrlBlock().getLowerIdNeighbourDevice(this);
			for (BleMeshDevice aSlaveDevice : SlaveList) {
				this.getDevMiddleware().getTopologyCtrlBlock()
						.setLocalLinkStatus(this, aSlaveDevice, 1, 3, 0);
				this.getTxManager().SetRemoteLinkStatus(this, aSlaveDevice, 1,
						3, 0);
			}
		} else {
			/*
			 * Instantiate a piconet with Self as Master. At this moment, the
			 * device doesn't have any slaves.
			 */
			BleMeshDevicePiconet aSelfPiconet = new BleMeshDevicePiconet(1,
					this);
			this.getDevMiddleware().getTopologyCtrlBlock().getDevPiconet()
					.add(aSelfPiconet);
			this.capture(this.getDevMiddleware().getTopologyCtrlBlock()
					.getLowerIdNeighbourDevice(this));
			
			if (aSelfPiconet.getDevSlaves().size()==0) {
				// get a list of neighboring piconets
				//List<BleMeshDevice> neighboringPiconets = this.genNeighbourPiconetList();
				List<BleMeshDevice> neighboringDevices =this.getTxManager().getNeighbouringDevices(this);
				for (BleMeshDevice neighDevice : neighboringDevices) {
					if (neighDevice.devMiddleware.getTopologyCtrlBlock().getDevPiconet().get(0).getDevRole()==1) {
						List<BleMeshDevice> neighPiconetSlaves = neighDevice.getDevMiddleware().getTopologyCtrlBlock().getDevPiconet().get(0).getDevSlaves();
						//if (neighPiconetSlaves.size()==4) {
							// attach a device to neighboring piconet
							neighPiconetSlaves.add(this);
							neighDevice.getDevMiddleware().getTopologyCtrlBlock().setLocalLinkStatus(this, this, 1, 3, 0);
							neighDevice.getTxManager().SetRemoteLinkStatus(this, this, 1, 3, 0);
							break;
						//}
					}
				}
			}
		}

		if (this.getDevMiddleware().getTopologyCtrlBlock().getDevPiconet()
				.size() > 0
				&& this.getDevMiddleware().getTopologyCtrlBlock()
						.getDevPiconet().get(0).getDevRole() == 1) {

			/** HOOK Point: Add event into the Queue */
			Event e = new Event(2, new Runnable() {
				@Override
				public void run() {
					UpdateGatewayTable();
				}
			});
			Simulator.insert(e);
		}

		return;

	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param slaveDevice
	 */
	public void capture(List<BleMeshDevice> slaveDevice) {
		int preys = slaveDevice.size();
		List<BleMeshDevice> slaveDeviceCpy;

		while (preys > this.getDevMiddleware().getTopologyCtrlBlock()
				.getCapacity()) {
			BleMeshDevice maxDevice = getMaxPrey(slaveDevice);

			slaveDeviceCpy = new ArrayList<BleMeshDevice>(slaveDevice);
			List<BleMeshDevice> CommonNeigh = FindCommonNeighbour(preys
					- this.getDevMiddleware().getTopologyCtrlBlock()
							.getCapacity(), slaveDeviceCpy, maxDevice);

			slaveDevice.remove(maxDevice);
			preys = slaveDevice.size();

			if (CommonNeigh.size() == 0) {
				contact(maxDevice);
			} else {
				/*
				 * C(u,v) = blue
				 */
				this.getDevMiddleware().getTopologyCtrlBlock()
						.setLocalLinkStatus(this, maxDevice, 1, 5, 0);
				this.getTxManager().SetRemoteLinkStatus(this, maxDevice, 1, 5,
						0);

				/*
				 * prey = prey - CommonNeigh; for all nodes(x) in CommonNeigh
				 * C(u,x) = red capacity(x) = capacity(x) - 1
				 */
				for (BleMeshDevice aCommonNode : CommonNeigh) {
					this.getDevMiddleware().getTopologyCtrlBlock()
							.setLocalLinkStatus(this, aCommonNode, 1, 4, 0);
					this.getTxManager().SetRemoteLinkStatus(this, aCommonNode,
							1, 4, 0);
					this.getTxManager().decrementRemoteDevCapacity(aCommonNode);
				}
				slaveDevice.removeAll(CommonNeigh);
				preys = slaveDevice.size();

			}

		}

		for (BleMeshDevice aPrey : slaveDevice) {
			contact(aPrey);
		}
		
	}

	public void contact(BleMeshDevice aDevice) {
		if (this.getTxManager().getRemoteDevRole(aDevice) != 2) {
			this.getDevMiddleware().getTopologyCtrlBlock()
					.setLocalLinkStatus(this, aDevice, 1, 1, 1);
			this.getTxManager().SetRemoteLinkStatus(this, aDevice, 1, 1, 2);
		} else {
			this.getDevMiddleware().getTopologyCtrlBlock()
					.setLocalLinkStatus(this, aDevice, 1, 2, 0);
			this.getTxManager().SetRemoteLinkStatus(this, aDevice, 1, 2, 0);
		}

		this.getDevMiddleware()
				.getTopologyCtrlBlock()
				.setCapacity(
						this.getDevMiddleware().getTopologyCtrlBlock()
								.getCapacity() - 1);
		return;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param device1
	 * @param device2
	 * @return
	 */

	public List<BleMeshDevice> FindCommonNeighbour(int minimumNeigh,
			List<BleMeshDevice> PreyList, BleMeshDevice device2) {

		int noDelegatedNodes = 0;
		List<BleMeshDevice> deviceList1 = PreyList;
		List<BleMeshDevice> deviceList2 = device2.getDevMiddleware()
				.getTopologyCtrlBlock().getLowerIdNeighbourDevice(device2);

		deviceList1.retainAll(deviceList2);

		noDelegatedNodes = minimumNeigh < 7 ? (minimumNeigh < deviceList1
				.size() ? minimumNeigh : deviceList1.size()) : ((7 < PreyList
				.size() ? 7 : deviceList1.size()));

		deviceList1.sort(BleMeshDevAddress);
		return deviceList1.subList(0, noDelegatedNodes);

	}

	BleMeshDevice getMaxPrey(List<BleMeshDevice> slaveDevice) {
		int index = -1;
		BleMeshDevice maxDevice = null;
		for (BleMeshDevice aDevice : slaveDevice) {
			if (aDevice.getDevUniqueId() > index) {
				maxDevice = aDevice;
				index = aDevice.getDevUniqueId();
			}
		}
		return maxDevice;
	}

	/** TODO Update this auto-generated comment for {@code BleMeshDevAddress} */
	public static Comparator<BleMeshDevice> BleMeshDevAddress = new Comparator<BleMeshDevice>() {

		@Override
		public int compare(BleMeshDevice s1, BleMeshDevice s2) {

			int address1 = s1.getDevUniqueId();
			int address2 = s2.getDevUniqueId();

			/* For ascending order */
			return address1 - address2;
		}
	};

	/**
	 * This function generates the list of the neighboring piconet list. The
	 * generated list is segmented in Low and High parts representing Piconet
	 * master with Id lower and higher than the current node
	 *
	 */
	private List<BleMeshDevice> genNeighbourPiconetList() {
		List<Integer> idList = new ArrayList<Integer>();
		List<BleMeshDevice> neighMasters = new ArrayList<BleMeshDevice>();

		for (BleMeshDevGatewayTable aRow : this.getDevMiddleware()
				.getTopologyCtrlBlock().getDevPiconet().get(0)
				.getGatewaytable()) {

			if (idList.contains(aRow.getPicRemoteMaster().getDevTrafficChar()
					.getAddress()) == false) {
				this.getDevMiddleware()
						.getTopologyCtrlBlock()
						.getDevPiconetNeighbours()
						.add(new BleMeshDeviceLink(aRow.getPicRemoteMaster(),
								0, 0));
				idList.add(aRow.getPicRemoteMaster().getDevTrafficChar()
						.getAddress());
				neighMasters.add(aRow.getPicRemoteMaster());
			}
		}
		
		return neighMasters;
	}

	/**
	 * TODO The function is used to initiate the gateway table update procedure
	 * for each slave acquired by the master.
	 *
	 */
	private void UpdateGatewayTable() {
		if (this.getDevMiddleware().getTopologyCtrlBlock().getDevPiconet()
				.size() > 0
				&& this.getDevMiddleware().getTopologyCtrlBlock()
						.getDevPiconet().get(0).getDevRole() == 1) {
			this.getDevMiddleware().getTopologyCtrlBlock().getDevPiconet()
					.get(0).getGatewaytable()
					.addAll(getSlavesNeighbourInformation(this));
			for (BleMeshDevice aSlave : this.getDevMiddleware()
					.getTopologyCtrlBlock().getDevPiconet().get(0)
					.getDevSlaves()) {
				this.getDevMiddleware().getTopologyCtrlBlock().getDevPiconet()
						.get(0).getGatewaytable()
						.addAll(getSlavesNeighbourInformation(aSlave));
			}
			genPiconetInterconnectRules(this);
			genNeighbourPiconetList();
			

			/** HOOK Point: Add event into the Queue */
			/* Launch interconnect phase of the algorithm. */
			Event e = new Event(3, new Runnable() {
				@Override
				public void run() {
					Interconnect();
				}
			});
			Simulator.insert(e);
		}
	}

	/**
	 * The construction of the gateway table is straightforward. It is
	 * sufficient that each master say u collects from its slaves s_u
	 * information about their neighbors v; namely, their master m(vi), capacity
	 * c(vi), and the color of the edge c(s_u, vi). Using such information, u
	 * can infer all the possible rules of interconnection.
	 *
	 * @param aSlaveDev
	 * @return List of gateway information
	 */
	private List<BleMeshDevGatewayTable> getSlavesNeighbourInformation(
			BleMeshDevice aSlaveDev) {
		List<BleMeshDevGatewayTable> gatewayInfo = new ArrayList<BleMeshDevGatewayTable>();
		for (BleMeshDeviceLink aNeighbour : aSlaveDev.getDevMiddleware()
				.getTopologyCtrlBlock().getDevNeighbours()) {
			if (!(aSlaveDev.getTxManager().getRemoteMaster(
					aNeighbour.getLinkDevice()).equals(aSlaveDev
					.getDevMiddleware().getTopologyCtrlBlock().getDevPiconet()
					.get(0).getDevMaster()))) {

				aSlaveDev.getDevMiddleware().getTopologyCtrlBlock()
						.setEnergyProfile(getEnergyProfile(aSlaveDev));
				aNeighbour
						.getLinkDevice()
						.getDevMiddleware()
						.getTopologyCtrlBlock()
						.setEnergyProfile(
								getEnergyProfile(aNeighbour.getLinkDevice()));

				/* Set Energy Profile for the Masters as well */
				BleMeshDevice remoteMaster = aSlaveDev.getTxManager()
						.getRemoteMaster(aNeighbour.getLinkDevice());
				remoteMaster.getDevMiddleware().getTopologyCtrlBlock()
						.setEnergyProfile(getEnergyProfile(remoteMaster));

				BleMeshDevice localMaster = aSlaveDev.getDevMiddleware()
						.getTopologyCtrlBlock().getDevPiconet().get(0)
						.getDevMaster();
				localMaster.getDevMiddleware().getTopologyCtrlBlock()
						.setEnergyProfile(getEnergyProfile(localMaster));

				gatewayInfo
						.add(new BleMeshDevGatewayTable(
						/* Local Master & Gateway */
						aSlaveDev.getDevMiddleware().getTopologyCtrlBlock()
								.getDevPiconet().get(0).getDevMaster(),
								aSlaveDev,

								/* Remote Master & Gateway */
								aSlaveDev.getTxManager().getRemoteMaster(
										aNeighbour.getLinkDevice()), aNeighbour
										.getLinkDevice(),

								/* Local Gateway capacity */
								aSlaveDev.getDevMiddleware()
										.getTopologyCtrlBlock().getCapacity(),

								/* Remote Gateway capacity */
								aSlaveDev.getTxManager().getRemoteCapacity(
										aNeighbour.getLinkDevice()),

								/* link-color between local and remote gateway */
								aNeighbour.getLinkColour(), (aSlaveDev
										.getDevMiddleware()
										.getTopologyCtrlBlock()
										.getEnergyProfile() + aNeighbour
										.getLinkDevice().getDevMiddleware()
										.getTopologyCtrlBlock()
										.getEnergyProfile()) / 2));
			}
		}

		return gatewayInfo;
	}

	/**
	 * This function is used to process the gateway information received from
	 * the slaves and determine the interconnection rules. All the rules assume
	 * Id (u) > Id(v)
	 * 
	 * @param aMaster
	 */

	private void genPiconetInterconnectRules(BleMeshDevice aMaster) {

		List<BleMeshDevGatewayTable> gatewayTable = aMaster.getDevMiddleware()
				.getTopologyCtrlBlock().getDevPiconet().get(0)
				.getGatewaytable();

		if (gatewayTable.size() > 0) {
			List<BleMeshDevGatewayTable> toRemove = new ArrayList<BleMeshDevGatewayTable>();
			for (BleMeshDevGatewayTable aEntry : gatewayTable) {
				if ((aEntry.getPicLocalGatewayCapacity() == 0)
						|| (aEntry.getPicRemoteGatewayCapacity() == 0)) {
					toRemove.add(aEntry);
					continue;
				}

				/*
				 * Check for Rule 1 : Three-hop interconnection Two piconets
				 * q(u) and q(v) may be interconnected through an edge e between
				 * two slaves localGateway and RemoteGateway, where c(e) =
				 * green. (Operation: s_u captures s_v)
				 * 
				 * u ---> s_u --g-- s_v <--- v => u ---> s_u ---> s_v <--- v u &
				 * v are the masters of respective piconets and s_u & s_v are
				 * slaves of master u & v
				 */

				if (aEntry.getPicLinkColor() == 3
						&& !(aEntry.getPicRemoteGatewayDev().equals(aEntry
								.getPicRemoteMaster()))
						&& !(aEntry.getPicLocalGatewayDev().equals(aEntry
								.getPicLocalMaster()))) {
					if (aMaster.getDevUniqueId() > aEntry.getPicRemoteMaster()
							.getDevUniqueId()) {
						aEntry.setPicRole(1);
						aEntry.setPicInterconnectionRule(1);
					} else {
						aEntry.setPicRole(2);
						aEntry.setPicInterconnectionRule(1);
					}
				}

				/*
				 * Check for Rule 2-a : Two-hop interconnection c(localGateway,
				 * RemoteGateway) = green, localGateway is a slave of u and
				 * RemoteGateway is a master of piconet v. (Operation:
				 * localGateway captures RemoteGateway.) u ---> s_u --g-- v =>
				 * u---> s_u ---> v u & v are the masters of respective piconets
				 * and s_u is slave of master u (Operation: s_u captures v.)
				 */

				else if (aEntry.getPicLinkColor() == 3
						&& (aEntry.getPicRemoteGatewayDev().equals(aEntry
								.getPicRemoteMaster()))
						&& !(aEntry.getPicLocalGatewayDev().equals(aEntry
								.getPicLocalMaster()))) {
					if (aMaster.getDevUniqueId() > aEntry.getPicRemoteMaster()
							.getDevUniqueId()) {
						aEntry.setPicRole(1);
						aEntry.setPicInterconnectionRule(2);
					} else {
						aEntry.setPicRole(2);
						aEntry.setPicInterconnectionRule(2);
					}
				}

				else if (aEntry.getPicLinkColor() == 3
						&& !(aEntry.getPicRemoteGatewayDev().equals(aEntry
								.getPicRemoteMaster()))
						&& (aEntry.getPicLocalGatewayDev().equals(aEntry
								.getPicLocalMaster()))) {
					if (aMaster.getDevUniqueId() > aEntry.getPicRemoteMaster()
							.getDevUniqueId()) {
						aEntry.setPicRole(1);
						aEntry.setPicInterconnectionRule(2);
					} else {
						aEntry.setPicRole(2);
						aEntry.setPicInterconnectionRule(2);
					}
				}

				/*
				 * I-Rule 2-b: Through the edge (v, s_x) or (u, s_x) where s_x
				 * is slave of u or v, and c((v, s_x)) = silver or c((u, s_x)) =
				 * silver (that is, s_x is smaller than both u and v, and it
				 * belong to either q(u) or q(v) but not both. Both piconets
				 * attempted to slave s_x but only one of them was successful).
				 * (Operation: v captures s_x or u captures s_x.)
				 * 
				 * u ---> s_u --g-- v => u--->s_u<--- v or u --g-- s_v <--- v =>
				 * u ---> s_v <--- v
				 */

				else if (aEntry.getPicLinkColor() == 2
						&& ((aEntry.getPicLocalGatewayDev().equals(
								aEntry.getPicLocalMaster()) && !(aEntry
								.getPicRemoteGatewayDev().equals(aEntry
								.getPicRemoteMaster()))) || (!(aEntry
								.getPicLocalGatewayDev().equals(aEntry
								.getPicLocalMaster())) && (aEntry
								.getPicRemoteGatewayDev().equals(aEntry
								.getPicRemoteMaster()))))) {
					if (aMaster.getDevUniqueId() > aEntry.getPicRemoteMaster()
							.getDevUniqueId()) {
						aEntry.setPicRole(1);
						aEntry.setPicInterconnectionRule(3);
					} else {
						aEntry.setPicRole(2);
						aEntry.setPicInterconnectionRule(3);
					}
				}

				/*
				 * I-Rule 2-c: Through the edge (u, s_v) where s_v is slave of v
				 * and c((u, s_v)) = red (that is, s_v is smaller than both u
				 * and v. s_v is slaved by v, and s_v was delegated by u to v).
				 * (Operation: s_v captures u.) u --r-- s_v <--- v => u <--- s_v
				 * <--- v
				 */
				else if (aEntry.getPicLinkColor() == 4
						&& aEntry.getPicLocalGatewayDev().equals(
								aEntry.getPicLocalMaster())
						&& !(aEntry.getPicRemoteGatewayDev().equals(aEntry
								.getPicRemoteMaster()))) {
					if (aMaster.getDevUniqueId() > aEntry.getPicRemoteMaster()
							.getDevUniqueId()) {
						aEntry.setPicRole(2);
						aEntry.setPicInterconnectionRule(4);
					} else {
						aEntry.setPicRole(1);
						aEntry.setPicInterconnectionRule(4);
					}
				}

				/*
				 * I-Rule 2-c: Through the edge (s_u, v) where s_u is slave of u
				 * and c((s_u, v)) = red (that is, s_u is smaller than both u
				 * and v. s_u is slaved by u, and s_u was delegated by v to u).
				 * (Operation: s_u captures v.)
				 */
				else if (aEntry.getPicLinkColor() == 4
						&& !(aEntry.getPicLocalGatewayDev().equals(aEntry
								.getPicLocalMaster()))
						&& aEntry.getPicRemoteGatewayDev().equals(
								aEntry.getPicRemoteMaster())) {
					if (aMaster.getDevUniqueId() > aEntry.getPicRemoteMaster()
							.getDevUniqueId()) {
						aEntry.setPicRole(2);
						aEntry.setPicInterconnectionRule(4);
					} else {
						aEntry.setPicRole(1);
						aEntry.setPicInterconnectionRule(4);
					}
				}

				/*
				 * I-Rule 3 (One-hop interconnection): through the edge e(u,v)
				 * where c((u, v)) = red. Both u and v are masters of different
				 * piconets. (Operation: v captures u )
				 */

				else if (aEntry.getPicLinkColor() == 4
						&& aEntry.getPicLocalGatewayDev().equals(
								aEntry.getPicLocalMaster())
						&& aEntry.getPicRemoteGatewayDev().equals(
								aEntry.getPicRemoteMaster())) {
					if (aMaster.getDevUniqueId() > aEntry.getPicRemoteMaster()
							.getDevUniqueId()) {
						aEntry.setPicRole(2);
						aEntry.setPicInterconnectionRule(5);
					} else {
						aEntry.setPicRole(1);
						aEntry.setPicInterconnectionRule(5);
					}
				}

				if (aEntry.getPicInterconnectionRule() == 0) {
					toRemove.add(aEntry);
				} else {
					/* log the error scenario */
				}

			}
			gatewayTable.removeAll(toRemove);
		}

	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 */
	private void Interconnect() {
		if (this.getDevMiddleware().getTopologyCtrlBlock()
				.isLowerIdNeighbourDevice(this) == 0) {
			/** HOOK Point: Add event into the Queue */
			/* Wait for the gateways of the smaller piconet to contact. */
			Event e = new Event(3, new Runnable() {
				@Override
				public void run() {
					Interconnect();
				}
			});
			Simulator.insert(e);

		} else {

			List<BleMeshDevice> aNeighPiconetMaster = this.getDevMiddleware()
					.getTopologyCtrlBlock().getHigerIdPiconetNeighbours(this);

			List<BleMeshDevice> neighbourPiconetHigherIdDevice = null;
			//List<BleMeshDevice> B = new ArrayList<BleMeshDevice>();

			aNeighPiconetMaster.sort(BleMeshDevAddress);;
			
			BleMeshDevGatewayTable optimalGateway = null;
			BleMeshDevice aBlePiconetNeighbour = null;

			while (aNeighPiconetMaster.size() > 0) {
				/*
				 * Get Piconet master with smallest Id say v, among all the high
				 * Id master
				 */
				aBlePiconetNeighbour = aNeighPiconetMaster.get(0);

				/* Determine the optimal Gateway for the interconnection */
				optimalGateway = this
						.getDevMiddleware()
						.getTopologyCtrlBlock()
						.getOptimalGateway(aBlePiconetNeighbour, gatewaySelRule);

				/*
				 * Trigger interconnection based upon the role identified for
				 * the gateway device
				 */
				if (optimalGateway != null) {
					/*
					 * for (BleMeshTrafficModel aTrafficModel : trafficModel) {
					 * if (aTrafficModel.isDestSet() == false) {
					 * aTrafficModel.setDestination
					 * (aBlePiconetNeighbour.getDevTrafficChar().getAddress());
					 * aTrafficModel.setDestSet(true); break; } }
					 */

					this.getDevMiddleware().getTopologyCtrlBlock().updateGatewayMasterCapacity(optimalGateway);
					this.getDevMiddleware().getTopologyCtrlBlock().updateGatewayEnergProfile(optimalGateway);

					this.getDevMiddleware().getTopologyCtrlBlock()
							.setInterconnectLinkStatus(optimalGateway.getPicRemoteMaster(), 1, 0);
					this.getTxManager().SetRemoteInterconnectLinkStatus(this, optimalGateway.getPicRemoteMaster(), 1,
							0);
					this.getTxManager().createInterconnect(optimalGateway);
					this.bridgeCnt++;

					neighbourPiconetHigherIdDevice = this.getTxManager()
							.getRemoteDeviceNeighboursHId(aBlePiconetNeighbour);

					/* Update HNeighbourlist = HNeighbourlist -v -HNeigh(v) */
					List<BleMeshDevice> tempList = new ArrayList<BleMeshDevice>(
							aNeighPiconetMaster);
					aNeighPiconetMaster.remove(aBlePiconetNeighbour);
					aNeighPiconetMaster
							.removeAll(neighbourPiconetHigherIdDevice);

					/* Update B = HNeigh(u) intersection HNeigh(v) */
					tempList.retainAll(neighbourPiconetHigherIdDevice);
					redundantPiconetList.addAll(tempList);
					
				} else {
					this.getDevMiddleware()
							.getTopologyCtrlBlock()
							.setInterconnectLinkStatus(aBlePiconetNeighbour, 1,
									0);
					this.getTxManager().SetRemoteInterconnectLinkStatus(this,
							aBlePiconetNeighbour, 1, 0);
					this.getTxManager().createInterconnect(optimalGateway);
					aNeighPiconetMaster.remove(aBlePiconetNeighbour);
				}
				
				for (BleMeshDevice piconetNeighbour : redundantPiconetList) {
					/* Contact the Master and set the contact status */
					this.getDevMiddleware().getTopologyCtrlBlock().setInterconnectLinkStatus(piconetNeighbour, 1, 0);
					this.getTxManager().SetRemoteInterconnectLinkStatus(this, piconetNeighbour, 1, 0);
				}
				
				/*
				redundantPiconetList = this.getDevMiddleware().getTopologyCtrlBlock().getAllPiconetNeighbours(this);
				System.out.println("Device: " + this.getDevUniqueId() + " ");
				
				boolean isFound=false;
				for (BleMeshDevice device:redundantPiconetList) {
					List<BleMeshDevGatewayTable> gatewayTables = this.getDevMiddleware().getTopologyCtrlBlock().getDevPiconet().get(0).getGatewaytable();
				    for (BleMeshDevGatewayTable gatewayTable:gatewayTables) {
					    if (gatewayTable.getPicRemoteMaster().getDevUniqueId()==this.getDevUniqueId() && gatewayTable.getPicLocalMaster().getDevUniqueId()==device.getDevUniqueId()) {
							redundantPiconetList.remove(device);
							System.out.println("Removing relay device " + device.getDevUniqueId() + " from redundant list");
							isFound=true;
							break;
					    } 
				    }
				    if (isFound)
				    	break;
				}
				
				for (BleMeshDevice device:redundantPiconetList) {
					System.out.println("Master device " + device.getDevUniqueId());
				}

				if (optimalGateway!=null && redundantPiconetList.contains(optimalGateway.getPicRemoteMaster())) {
					System.out.println("Optimal gateway = remote device" + optimalGateway.getPicRemoteMaster().getDevUniqueId() + ", local device" + optimalGateway.getPicLocalMaster().getDevUniqueId());
					redundantPiconetList.remove(aBlePiconetNeighbour);
					System.out.println("Removing master device " + optimalGateway.getPicRemoteMaster().getDevUniqueId() + " from redundant list");
				}
				*/
			}
			
			
			if (bridgeCnt<2) {
				/* Schedule the resillence phase at the */
				Event e = new Event(4, new Runnable() {
					@Override
					public void run() {
						RessillencePhase();
					}
				});
				Simulator.insert(e);
			}
		}
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 */
	private void RessillencePhase() {

		List<BleMeshDevice> RedundantPiconetList = this.getRedundantPiconetList();
		List<BleMeshDevGatewayTable> optimalGateways = new ArrayList<BleMeshDevGatewayTable>();

		if (RedundantPiconetList.size() == 0)
			return;
		
		System.out.println("Device: " + this.getDevUniqueId() + " ");

		for (BleMeshDevice aBleMeshDevice : RedundantPiconetList) {
			/* Determine the optimal Gateway for the interconnection */
			BleMeshDevGatewayTable optimalGateway = this.getDevMiddleware().getTopologyCtrlBlock()
					.getOptimalGateway(aBleMeshDevice, gatewaySelRule);

			/* Determine the optimal Gateway for the interconnection */
			optimalGateways.add(optimalGateway);
			System.out.println("Master device" + aBleMeshDevice.getDevUniqueId());
		}

		if (gatewaySelRule == 1)
			optimalGateways.sort(BleMeshDevGatewayTable.BleMeshGatewayRuleDef);
		else if (gatewaySelRule == 2)
			optimalGateways.sort(BleMeshDevGatewayTable.BleMeshGatewayRuleDefEnergy);
		else if (gatewaySelRule == 3)
			optimalGateways.sort(BleMeshDevGatewayTable.BleMeshGatewayRuleOnlyEnergy);
		else if (gatewaySelRule == 4)
			optimalGateways.sort(BleMeshDevGatewayTable.BleMeshGatewayRuleHop);

		BleMeshDevGatewayTable selectedGateway = optimalGateways.get(0);
		if (selectedGateway != null) {

			/*Update the Master Capacity and the Energy profile for the gateways devices*/
			this.getDevMiddleware().getTopologyCtrlBlock().updateGatewayMasterCapacity(selectedGateway);
			this.getDevMiddleware().getTopologyCtrlBlock().updateGatewayEnergProfile(selectedGateway);

			/*Set the interconnect link status and create the connection between the bridges*/
			this.getDevMiddleware().getTopologyCtrlBlock()
					.setInterconnectLinkStatus(selectedGateway.getPicRemoteMaster(), 1, 0);
			this.getTxManager().SetRemoteInterconnectLinkStatus(this, selectedGateway.getPicRemoteMaster(), 1, 0);
			this.getTxManager().createInterconnect(selectedGateway);
			
			this.bridgeCnt++;
			
			System.out.println("Removing Device " +selectedGateway.getPicRemoteMaster().getDevUniqueId() + " from redundant list");
			RedundantPiconetList.remove(selectedGateway.getPicRemoteMaster());
		}
		this.setRedundantPiconetList(RedundantPiconetList);
		
		/*
		if (bridgeCnt<3) {
			/* Schedule the resillence phase at the */
		/*
			Event e = new Event(5, new Runnable() {
				@Override
				public void run() {
					RessillencePhase();
				}
			});
			Simulator.insert(e);
		}
		*/
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 */
	public void UpdateRoutingTable() {
		List<BleMeshDevicePiconet> piconetList = this.getDevMiddleware()
				.getTopologyCtrlBlock().getDevPiconet();
		BleMeshRoutingCtrlBlock routingCtrlBLk = this.getDevMiddleware()
				.getRoutingCtrlBlock();

		/*
		 * For each piconet create a list of direct neighbors. This is only
		 * valid for a device acting as a master in at-least one piconet.
		 */
		for (BleMeshDevicePiconet aPiconet : piconetList) {

			List<Integer> directNodes = new ArrayList<Integer>();
			List<Integer> relayNodes = new ArrayList<Integer>();

			/* Device is the master, hence it has direct neighbors. */
			if (aPiconet.getDevRole() == 1) {
				List<BleMeshDevice> slaveList = aPiconet.getDevSlaves();
				List<BleMeshDevice> RelayList = aPiconet.getRelays();
				for (BleMeshDevice aSlave : slaveList) {
					int slaveId = aSlave.getDevTrafficChar().getAddress();
					directNodes.add(slaveId);
				}

				for (BleMeshDevice aRelay : RelayList) {
					int relayId = aRelay.getDevTrafficChar().getAddress();
					relayNodes.add(relayId);
				}
			} else {
				int masterId = aPiconet.getDevMaster().getDevTrafficChar()
						.getAddress();
				directNodes.add(masterId);
				relayNodes.add(masterId);
			}

			/* Adding self address as directly reachable Device */
			directNodes.add(this.getDevTrafficChar().getAddress());

			routingCtrlBLk.getdirectNodes().add(directNodes);
			routingCtrlBLk.getRelayNodes().addAll(relayNodes);

		}

	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aPacket
	 * @return
	 */
	public void SendPacket(BleMeshPacket aPacket) {

		aPacket.setViaAddress(devTrafficChar.getAddress());
		aPacket.getHops().add(devTrafficChar.getAddress());

		int srcAdd = aPacket.getFromAddress();
		int dstAdd = aPacket.getToAddress();
		int viaAdd = aPacket.getViaAddress();

		if (dstAdd == viaAdd) {
			this.getDevMiddleware()
					.getRoutingCtrlBlock()
					.setPacketsRecived(
							this.getDevMiddleware().getRoutingCtrlBlock()
									.getPacketsRecived() + 1);
			// System.out.printf("Packet Received at the destination = %d \n",dstAdd);
			// System.out.println(aPacket.getHops());
			BleMeshPacket ctrlPacket = this.getDevMiddleware()
					.getRoutingCtrlBlock().ProcessPacket(aPacket, this);
			if (ctrlPacket != null) {
				SendPacket(ctrlPacket);
			} 
			// if (aPacket.isRouteAvail())
			// System.out.printf("Cache Hit ....");
			return;
		}

		if (aPacket.isRouteAvail() && !aPacket.getRoute().isEmpty()) {
			/*
			 * Get the next hope from the packet Remove the hop from the Route
			 * send the packet to the determined hop
			 */
			if (this.devEnergyChar.isDeviceAlive() || aPacket.getType()==2) {
				
				int hop = aPacket.getRoute().get(0);
				aPacket.getRoute().remove(0);

				this.transmit(aPacket, hop);
				this.getDevEnergyChar().decrementEnergyLevel();
				//System.out.printf("\n [tx_event]: Time = %f,EnergyChar %s, DeviceId %d, DeviceInitEnergy=%f, DeviceCurrEnergy=%f, isDevAlive=%b, isdevCritical=%b, DevPacketsTx =%d \n",
				//	Simulator.now(),
				//	 this.getDevEnergyChar().getEnergySourceType(),this.getDevUniqueId(),
				//   this.getDevEnergyChar().getInitEnergyLevel(),this.getDevEnergyChar().getCurrEnergyLevel(),
				//    devEnergyChar.isDeviceAlive(), this.isDeviceCritical(), this.getDevMiddleware().getRoutingCtrlBlock().getPacketsRecived());
			    BleMeshSimulator.simLog.add(
						new BleMeshDeviceLog(
								Simulator.now(), 
								this.getDevUniqueId(), 
								this.getDevEnergyChar().getEnergySourceType(), 
								this.getDevEnergyChar().getInitEnergyLevel(), 
								this.getDevEnergyChar().getCurrEnergyLevel(), 
								this.isDeviceCritical(), devEnergyChar.isDeviceAlive(), false));
			}

		} else {
			/* Does the incoming packet has route info */
			List<Integer> NextHops = this.getDevMiddleware()
					.getRoutingCtrlBlock().Route(aPacket);

			/*
			 * if after routing, we got a discovered path 1. use the discovered
			 * path for the routing 2. Get the hop from the route 3. Remove the
			 * hop from the Route 4.send the packet to the determined hop else
			 * Flood the network with the list of hops
			 */

			if (aPacket.isRouteAvail() && !aPacket.getRoute().isEmpty()) {
				/*
				 * Get the next hope from the packet Remove the hop from the
				 * Route send the packet to the determined hop
				 */
				if (this.devEnergyChar.isDeviceAlive() || aPacket.getType()==2) {
					int hop = aPacket.getRoute().get(0);
					aPacket.getRoute().remove(0);

					this.transmit(aPacket, hop);
					this.getDevEnergyChar().decrementEnergyLevel();
					//System.out.printf("\n [tx_event]: Time = %f,EnergyChar %s, DeviceId %d, DeviceInitEnergy=%f, DeviceCurrEnergy=%f, isDevAlive=%b, isdevCritical=%b, DevPacketsTx =%d \n",
					//Simulator.now(),
					// this.getDevEnergyChar().getEnergySourceType(),this.getDevUniqueId(),
					//   this.getDevEnergyChar().getInitEnergyLevel(),this.getDevEnergyChar().getCurrEnergyLevel(),
					//   devEnergyChar.isDeviceAlive(), this.isDeviceCritical(), this.getDevMiddleware().getRoutingCtrlBlock().getPacketsRecived());
				    BleMeshSimulator.simLog.add(
							new BleMeshDeviceLog(
									Simulator.now(), 
									this.getDevUniqueId(), 
									this.getDevEnergyChar().getEnergySourceType(), 
									this.getDevEnergyChar().getInitEnergyLevel(), 
									this.getDevEnergyChar().getCurrEnergyLevel(), 
									this.isDeviceCritical(), devEnergyChar.isDeviceAlive(), false));
				}
			} else /* Flood the network */
			{
				
				for (Integer aHop : NextHops) {
					if (this.devEnergyChar.isDeviceAlive() || aPacket.getType() == 2) {
						this.transmit(aPacket, aHop);
						this.getDevEnergyChar().decrementEnergyLevel();
						//System.out.printf("\n [tx_event]: Time = %f, EnergyChar %s, DeviceId %d, DeviceInitEnergy=%f, DeviceCurrEnergy=%f, isDevAlive=%b, isdevCritical=%b, DevPacketTRx =%d \n",
						// Simulator.now(),
						// this.getDevEnergyChar().getEnergySourceType(),this.getDevUniqueId(),
						//   this.getDevEnergyChar().getInitEnergyLevel(),this.getDevEnergyChar().getCurrEnergyLevel(),
						//    devEnergyChar.isDeviceAlive(), this.isDeviceCritical(), this.getDevMiddleware().getRoutingCtrlBlock().getPacketsRecived());
						BleMeshSimulator.simLog.add(
								new BleMeshDeviceLog(
										Simulator.now(), 
										this.getDevUniqueId(), 
										this.getDevEnergyChar().getEnergySourceType(), 
										this.getDevEnergyChar().getInitEnergyLevel(), 
										this.getDevEnergyChar().getCurrEnergyLevel(), 
										this.isDeviceCritical(), devEnergyChar.isDeviceAlive(), false));
					}
				}
			}
		}
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aPacket
	 * @param nextHop
	 */
	public void transmit(BleMeshPacket aPacket, final int nextHop) {
		final BleMeshPacket packet = new BleMeshPacket(aPacket);
		final int TxChannel = getTxChannel(aPacket, nextHop);
		packetTxCount++;

		if (this.isDeviceCritical() == true) {
			// RouteLog.log(this, getTime(),packetTxCount,packetRxCount);
		}

		txPackets.add(packet.getId());

		/** HOOK Point: Add event into the Queue */
		// add processing time and random offset

		Event e = new Event(Simulator.now() + devRadioChar.getMinOffset()
				+ Simulator.getRandom()
				* (devRadioChar.getMaxOffset() - devRadioChar.getMinOffset())
				+ processingTime, new Runnable() {
			@Override
			public void run() {
				txManager.transmit(packet, TxChannel, nextHop);
			}
		});
		Simulator.insert(e);

	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aPacket
	 * @param nextHop
	 * @return
	 */
	public int getTxChannel(BleMeshPacket aPacket, final int nextHop) {
		return 37;
	}

	/**
	 * TODO This function returns the unique id of the device to be used by the
	 * algorithm.
	 *
	 * @return
	 */
	public int getDevUniqueId() {
		return this.getDevTrafficChar().getAddress();
		// return devUniqId;
	}

	int GetRandomId() {

		return this.getDevTrafficChar().getAddress();
	}

	boolean isDeviceCritical() {
		int picoNum = this.getDevMiddleware().getTopologyCtrlBlock()
				.getDevPiconet().size();
		boolean isMaster = false;
		if (picoNum == 1) {
			for (BleMeshDevicePiconet aPiconet : this.getDevMiddleware()
					.getTopologyCtrlBlock().getDevPiconet()) {
				if (aPiconet.getDevRole() == 1)
					isMaster = true;
			}
		}

		return ((picoNum > 1) || (isMaster == true)) ? true : false;
	}

	/**
	 * Returns the gatewaySelRule.
	 *
	 * @return the gatewaySelRule
	 */
	public int getGatewaySelRule() {
		return gatewaySelRule;
	}

	/**
	 * Sets the gatewaySelRule.
	 *
	 * @param aGatewaySelRule
	 *            the gatewaySelRule
	 */
	public void setGatewaySelRule(int aGatewaySelRule) {
		gatewaySelRule = aGatewaySelRule;
	}

	private double getEnergyProfile(BleMeshDevice aDevice) {
		double localErP = 0.0;

		if (aDevice.getDevEnergyChar().getEnergySourceType() == EnergySource.BLE_DEV_CONNECTED_SUPPLY) {
			localErP += 200;
		} else if (aDevice.getDevEnergyChar().getEnergySourceType() == EnergySource.BLE_DEV_BATTRERY_RECHARGEABLE) {
			localErP += aDevice.getDevEnergyChar().getInitEnergyLevel();
		} else {
			localErP += Math.ceil(aDevice.getDevEnergyChar()
					.getInitEnergyLevel() * 0.1);
		}

		return localErP;

	}

	/**
	 * @return the trafficModel
	 */
	public BleMeshTrafficModel getTrafficModel() {
		return trafficModel;
	}

	/**
	 * @param trafficModel
	 *            the trafficModel to set
	 */
	public void setTrafficModel(BleMeshTrafficModel trafficModel) {
		this.trafficModel = trafficModel;
	}
}
