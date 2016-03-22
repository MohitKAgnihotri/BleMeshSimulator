// Copyright (c) 2015 ERICSSON AB

package KTHBleMesh;

import java.util.ArrayList;
import java.util.List;

/**
 * BLE mesh transmission manager
 *
 * @author Pontus Arvidson, Wei Shen, Roman Chirikov
 */
public class BleMeshTxManager {


	private String mode;
	
	private double range;
	
	private double txPower;
	
	private double sensitivity;
	
	private int overhead;

	private ArrayList<BleMeshDevice> listeners; // list of devices, not just
												// listeners
	private ArrayList<ArrayList<BleMeshTransmission>> transmissions;

	private int nrofTransmissions = 0;
	private int nrofCollision = 0;

	
	/**
	 * Constructs a {@code BleTransmissionManager}.
	 *
	 * @param aParent
	 * @param aName
	 */
	public BleMeshTxManager() {
		listeners = new ArrayList<>();
		transmissions = new ArrayList<ArrayList<BleMeshTransmission>>(40);
		for (int i = 0; i < 40; i++) {
			transmissions.add(i, new ArrayList<BleMeshTransmission>());
		}
	}



	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param device1
	 * @param device2
	 * @return
	 */
	public double distance(BleMeshDevice device1, BleMeshDevice device2) {
		return Math.hypot(Math.hypot(device1.getDevMobilityChar().getX()
				- device2.getDevMobilityChar().getX(), device1
				.getDevMobilityChar().getY()
				- device2.getDevMobilityChar().getY()), device1
				.getDevMobilityChar().getZ()
				- device2.getDevMobilityChar().getZ());
	}

	private double pathLoss(double distance) {
		return Math.max(0, 40 + 25 * Math.log10(distance));
	}

	private double BER(double sinr) {
		// taken from
		// http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=6068586
		return 0.5 * (1 - 1 / Math.sqrt(1 + 1 / sinr));
	}

	private double BLEP(double ber, int bitLength) {
		return 1 - Math.pow(1 - ber, bitLength);
	}

	private double dBm2W(double dBm) {
		return Math.pow(10, (dBm - 30) / 10);
	}

	private double W2dBm(double W) {
		return 30 + 10 * Math.log10(W);
	}

	/**
	 * Registers a listener
	 *
	 * @param aDevice
	 */
	public void registerListener(BleMeshDevice aDevice) {
		// listeners.add(aDevice.getDevTrafficChar().getAddress(), aDevice);
		listeners.add(aDevice);
	}

	/**
	 *
	 * This method is called when a collision occurs.
	 *
	 * @param aTransmission
	 */
	private void onCollision(BleMeshTransmission aTransmission) {
		nrofCollision++;
		// logCollision.log(this, aTransmission.getChannelNumber());
	}

	/**
	 *
	 * This method is called when a transmission is added.
	 *
	 * @param aTransmission
	 */
	private void onTransmission(
			@SuppressWarnings("unused") BleMeshTransmission aTransmission) {
		nrofTransmissions++;
	}

	/**
	 * Returns the nrofTransmissions.
	 *
	 * @return the nrofTransmissions
	 */
	public int getNrofTransmissions() {
		return nrofTransmissions;
	}

	/**
	 * Returns the nrofCollision.
	 *
	 * @return the nrofCollision
	 */
	public int getNrofCollision() {
		return nrofCollision;
	}

	/**
	 * This function is used by the BSF Algorithms for simulating the scanning
	 * process. It returns the list of devices in the transmission range of the
	 * requesting device
	 *
	 * @param reqDevice
	 * @return
	 */
	public List<BleMeshDevice> getNeighbouringDevices(BleMeshDevice reqDevice) {
		List<BleMeshDevice> neighbouringDevices = new ArrayList<BleMeshDevice>();
		for (BleMeshDevice aDevice : listeners) {
			if (!reqDevice.equals(aDevice)) {
				/*
				 * It is possible that requested devices has different
				 * transmission and reception properties. The check ensures that
				 * devices are in each others transmission range
				 */
				double distance = distance(reqDevice, aDevice);
				if (distance < reqDevice.getDevRadioChar().getMaxDistance()
						&& distance < aDevice.getDevRadioChar()
								.getMaxDistance()) {

					/*
					 * TODO Need to review this part of the code as there is a
					 * small conflict in the paper and current implementation
					 * Correct version of the code is available in the comments
					 * but needs to be verified before it can be enabled.
					 * if(reqDevice.getDevTrafficChar().getAddress() >
					 * aDevice.getDevTrafficChar().getAddress()) {
					 * neighbouringDevices.add(aDevice); }
					 */
					neighbouringDevices.add(aDevice);
				}
			}
		}

		return neighbouringDevices;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param reqDevice
	 * @param neighbourDev
	 * @param contact
	 * @param linkColor
	 * @param devRole
	 * @param piconetId
	 */
	public void SetRemoteLinkStatus(BleMeshDevice reqDevice,
			BleMeshDevice neighbourDev, int contact, int linkColor, int devRole) {
		neighbourDev
				.getDevMiddleware()
				.getTopologyCtrlBlock()
				.setLocalLinkStatus(neighbourDev, reqDevice, contact,
						linkColor, devRole);
		return;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param reqDevice
	 * @param neighbourDev
	 * @param contact
	 * @param linkColor
	 */
	public void SetRemoteInterconnectLinkStatus(BleMeshDevice reqDevice,
			BleMeshDevice neighbourDev, int contact, int linkColor) {
		neighbourDev.getDevMiddleware().getTopologyCtrlBlock()
				.setInterconnectLinkStatus(reqDevice, contact, linkColor);
		return;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param reqDevice
	 */
	public void incrementRemoteDevCapacity(BleMeshDevice reqDevice) {
		reqDevice
				.getDevMiddleware()
				.getTopologyCtrlBlock()
				.setCapacity(
						reqDevice.getDevMiddleware().getTopologyCtrlBlock()
								.getCapacity() + 1);
		return;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param reqDevice
	 */
	public void decrementRemoteDevCapacity(BleMeshDevice reqDevice) {
		reqDevice
				.getDevMiddleware()
				.getTopologyCtrlBlock()
				.setCapacity(
						reqDevice.getDevMiddleware().getTopologyCtrlBlock()
								.getCapacity() - 1);
		return;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param reqDevice
	 * @return
	 */
	public int getRemoteDevRole(BleMeshDevice reqDevice) {
		int isPiconetExist = reqDevice.getDevMiddleware()
				.getTopologyCtrlBlock().getDevPiconet().size();
		if (isPiconetExist > 0) {
			return reqDevice.getDevMiddleware().getTopologyCtrlBlock()
					.getDevPiconet().get(0).getDevRole();
		}
		return 0;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aDevice
	 * @return
	 */
	public BleMeshDevice getRemoteMaster(BleMeshDevice aDevice) {
		int isPiconetExist = aDevice.getDevMiddleware().getTopologyCtrlBlock()
				.getDevPiconet().size();
		if (isPiconetExist > 0) {
			/* Device is a part of the piconet; acting as a Master or Slave */
			return aDevice.getDevMiddleware().getTopologyCtrlBlock()
					.getDevPiconet().get(0).getDevMaster();
		}
		return null;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aDevice
	 * @return
	 */
	public int getRemoteCapacity(BleMeshDevice aDevice) {
		return aDevice.getDevMiddleware().getTopologyCtrlBlock().getCapacity();
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aBlePiconetNeighbour
	 * @return
	 */
	public List<BleMeshDevice> getRemoteDeviceNeighboursHId(
			BleMeshDevice aBlePiconetNeighbour) {
		return aBlePiconetNeighbour.getDevMiddleware().getTopologyCtrlBlock()
				.getHigerIdPiconetNeighbours(aBlePiconetNeighbour);
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param devPiconetMaster
	 * @param devPiconetGateway
	 */
	public void decrementRemoteGatewayDeviceCap(BleMeshDevice devPiconetMaster,
			BleMeshDevice devPiconetGateway) {
		devPiconetMaster.getDevMiddleware().getTopologyCtrlBlock()
				.getDevPiconet().get(0)
				.decrementGatewayDeviceCap(devPiconetGateway);
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param optimalGateway
	 */
	public void createInterconnect(BleMeshDevGatewayTable optimalGateway) {
		if (optimalGateway != null) {
			BleMeshDevice aLinkdevA = optimalGateway.getPicLocalGatewayDev();
			BleMeshDevice aLinkdevB = optimalGateway.getPicRemoteGatewayDev();
			int localGatewayRole = optimalGateway.getPicRole();
			if (localGatewayRole == 1) {
				aLinkdevA.getDevMiddleware().getTopologyCtrlBlock()
						.createLocalInterconnect(aLinkdevA, aLinkdevB, 1);
				aLinkdevB.getDevMiddleware().getTopologyCtrlBlock()
						.createLocalInterconnect(aLinkdevB, aLinkdevA, 2);
			} else {
				aLinkdevA.getDevMiddleware().getTopologyCtrlBlock()
						.createLocalInterconnect(aLinkdevA, aLinkdevB, 2);
				aLinkdevB.getDevMiddleware().getTopologyCtrlBlock()
						.createLocalInterconnect(aLinkdevB, aLinkdevA, 1);
			}

			optimalGateway
					.getPicLocalMaster()
					.getDevMiddleware()
					.getTopologyCtrlBlock()
					.setRelayNode(optimalGateway.getPicLocalMaster(), aLinkdevA);
			optimalGateway
					.getPicRemoteMaster()
					.getDevMiddleware()
					.getTopologyCtrlBlock()
					.setRelayNode(optimalGateway.getPicRemoteMaster(),
							aLinkdevB);

			if (optimalGateway.getPicLocalMaster().equals(aLinkdevA)) {
				optimalGateway
						.getPicLocalMaster()
						.getDevMiddleware()
						.getTopologyCtrlBlock()
						.setRelayNode(optimalGateway.getPicLocalMaster(),
								aLinkdevB);
				optimalGateway
						.getPicRemoteMaster()
						.getDevMiddleware()
						.getTopologyCtrlBlock()
						.setRelayNode(optimalGateway.getPicRemoteMaster(),
								aLinkdevB);
			} else if (optimalGateway.getPicRemoteMaster().equals(aLinkdevB)) {
				optimalGateway
						.getPicLocalMaster()
						.getDevMiddleware()
						.getTopologyCtrlBlock()
						.setRelayNode(optimalGateway.getPicLocalMaster(),
								aLinkdevA);
				optimalGateway
						.getPicRemoteMaster()
						.getDevMiddleware()
						.getTopologyCtrlBlock()
						.setRelayNode(optimalGateway.getPicRemoteMaster(),
								aLinkdevA);
			} else {
				optimalGateway
						.getPicLocalMaster()
						.getDevMiddleware()
						.getTopologyCtrlBlock()
						.setRelayNode(optimalGateway.getPicLocalMaster(),
								aLinkdevA);
				optimalGateway
						.getPicRemoteMaster()
						.getDevMiddleware()
						.getTopologyCtrlBlock()
						.setRelayNode(optimalGateway.getPicRemoteMaster(),
								aLinkdevB);
			}
		}
	}

	/**
	 * Transmits to all receivers
	 *
	 * @param aPacket
	 * @param channelNumber
	 */
	public void transmit(BleMeshPacket aPacket, final int channelNumber,
			int nextHop) {
		BleMeshPacket packet = aPacket;
		int nrofBits = aPacket.getBitLength() + overhead;

		BleMeshDevice sender = null;
		BleMeshDevice nextHopDevTemp = null;
		for (BleMeshDevice aDevice : listeners) {
			if (aDevice.getDevTrafficChar().getAddress() == packet
					.getViaAddress()) {
				sender = aDevice;
				break;
			}
		}

		for (BleMeshDevice aDevice : listeners) {
			if (aDevice.getDevTrafficChar().getAddress() == nextHop) {
				nextHopDevTemp = aDevice;
				break;
			}
		}

		final BleMeshDevice nextHopDev = nextHopDevTemp;

		// double transmissionId = getRandom().nextDouble();
		double transmissionId = Simulator.getRandom(); // double transmissionId = 1;
		double distance, pathLoss, ber, blep, desiredPowerW = 1, sinr = 1;
		double sensitivityW = dBm2W(sensitivity);

		// BleMeshDevice.logTx.log(this, getTime(), packet.getViaAddress(),
		// packet.getId(), nrofBits/1000000);

		// // fail ongoing transmissions on the same channel depending on SINR
		// for (BleMeshTransmission anotherTransmission:
		// transmissions.get(channelNumber))
		// {
		// // transmission cannot interfere with itself
		// if (anotherTransmission.getTransmissionId() == transmissionId)
		// continue;
		//
		// BleMeshDevice anotherListener = anotherTransmission.getListener();
		//
		// // if a device has 2 RF chains it will not be affected by it's own
		// interference
		// if (anotherListener.getDevTrafficChar().getAddress() ==
		// sender.getDevTrafficChar().getAddress()
		// && sender.getDevRadioChar().getNrofRfChains() == 2)
		// continue;
		//
		// // the distance between the transmitter and the receiver of another
		// transmission
		// distance = distance(sender, anotherListener);
		//
		// // New transmission interferes another one, update success flag
		// if (mode.equals("simple") && distance <= range) {
		// if (anotherTransmission.getSuccessFlag()) {
		// anotherTransmission.setSuccessFlag(false);
		// // notify that a collision has occurred
		// onCollision(anotherTransmission);
		// }
		// }
		//
		// if (mode.equals("advanced")) {
		// int anotherNrofBits =
		// anotherTransmission.getPacket().getBitLength() + overhead;
		// pathLoss = pathLoss(distance);
		// double interferingPowerW = dBm2W(txPower - pathLoss);
		// desiredPowerW = dBm2W(anotherTransmission.getRxPowerdBm());
		// sinr = anotherTransmission.getSinr(); // unitless;
		// // interference from other transmissions
		// double oldInterference = desiredPowerW / sinr - sensitivityW;
		// sinr = desiredPowerW / (sensitivityW + oldInterference +
		// interferingPowerW);
		// ber = BER(sinr); // SINR here is unitless
		// blep = BLEP(ber, anotherNrofBits);
		// boolean successFlag = getRandom().nextDouble() > blep;
		//
		// anotherTransmission.setSinr(sinr);
		// if (!successFlag && anotherTransmission.getSuccessFlag()) {
		// anotherTransmission.setSuccessFlag(successFlag);
		// // notify that a collision has occurred
		// onCollision(anotherTransmission);
		// }
		// }
		// }

		// Clear if the transmission is failed
		boolean successFlag = true;
		// Set if the transmission can reach the listener
		boolean reachable = true;
		// Set if the transmission was failed due to interference from other
		// transmissions
		boolean collision = false;

		// the distance between the transmitter and the receiver
		distance = distance(sender, nextHopDev);

		// if (mode.equals("simple")) {
		// boolean noInterference = true;
		// for (BleMeshTransmission anotherTransmission:
		// transmissions.get(channelNumber))
		// {
		// // transmission cannot interfere with itself
		// if (anotherTransmission.getTransmissionId() == transmissionId)
		// continue;
		//
		// BleMeshDevice anotherSender =
		// listeners.get(anotherTransmission.getPacket().getViaAddress());
		//
		// // if a device has 2 RF chains it will not be affected by it's own
		// // interference
		// if (anotherSender.getDevTrafficChar().getAddress() ==
		// nextHopDev.getDevTrafficChar().getAddress()
		// && nextHopDev.getDevRadioChar().getNrofRfChains() == 2)
		// continue;
		//
		// double anotherDistance = distance(anotherSender, nextHopDev);
		// if (anotherDistance <= range) {
		// noInterference = false;
		// break;
		// }
		// }
		// successFlag = noInterference;
		// collision = !noInterference;
		//
		// }
		// else if (mode.equals("advanced")) {
		// pathLoss = pathLoss(distance);
		// desiredPowerW = dBm2W(txPower - pathLoss);
		//
		// // Get total interference from other transmitters
		// double sumInterferenceW = 0;
		// for (BleMeshTransmission anotherTransmission:
		// transmissions.get(channelNumber))
		// {
		// // transmission cannot interfere with itself
		// if (anotherTransmission.getTransmissionId() == transmissionId)
		// continue;
		//
		// BleMeshDevice anotherSender =
		// listeners.get(anotherTransmission.getPacket().getViaAddress());
		//
		// // if a device has 2 RF chains it will not be affected by it's own
		// interference
		// if (anotherSender.getDevTrafficChar().getAddress() ==
		// nextHopDev.getDevTrafficChar().getAddress()
		// && nextHopDev.getDevRadioChar().getNrofRfChains() == 2)
		// continue;
		//
		// double anotherDistance = distance(anotherSender, nextHopDev);
		// double anotherPathLoss = pathLoss(anotherDistance);
		// double interferenceW =
		// dBm2W(txPower - anotherPathLoss);
		// sumInterferenceW += interferenceW;
		// }

		// // Use SINR model only in advanced case
		// sinr = desiredPowerW / (sensitivityW + sumInterferenceW);
		// ber = BER(sinr); // SINR here is unitless
		// blep = BLEP(ber, nrofBits);
		// double rand = getRandom().nextDouble();
		// successFlag = rand > blep;
		//
		// // Check if the interference was the cause of the failed transmission
		// if (!successFlag) {
		// double snr = desiredPowerW / sensitivityW;
		// double berNoInterference = BER(snr); // SNR here is unitless
		// double blepNoInterference = BLEP(berNoInterference, nrofBits);
		// if (rand > blepNoInterference) {
		// reachable = true;
		// collision = true;
		// }
		// else {
		// reachable = false;
		// collision = false;
		// }
		// } else {
		// reachable = true;
		// collision = false;
		// }
		//
		// }
		// else {
		// throw new IllegalStateException("Invalid TxManager mode");
		// }

		final BleMeshTransmission transmission = new BleMeshTransmission(
				transmissionId, packet, nrofBits, channelNumber, nextHopDev,
				W2dBm(desiredPowerW), sinr);
		transmission.setSuccessFlag(successFlag);

		// Notify the transmission and collision
		if (reachable) {
			onTransmission(transmission);
		}
		if (collision) {
			onCollision(transmission);
		}

		transmissions.get(channelNumber).add(transmission);

		// Calculate transmission time
		double tt = transmission.getDuration();

		// runAt(getTime() + tt, new Runnable() {
		// @Override
		// public void run() {
		// // Remove transmission from channel
		// transmissions.get(channelNumber).remove(transmission);
		// nextHopDev.receive(transmission);
		// }
		// });
		
		Event e = new Event(Simulator.now() + tt, new Runnable() {
			@Override
			public void run() {
				transmissions.get(channelNumber).remove(transmission);
				nextHopDev.receive(transmission);
			}
		});

		Simulator.insert(e);
	}

	void UpdateRemoteGatewayCapacity(BleMeshDevice aMaster,
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

	void UpdateRemoteGateway(BleMeshDevice aMaster, BleMeshDevice aGatewayDev) {
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

	void updateRemoteGatewayEnergy(BleMeshDevice aMaster,
			BleMeshDevice aGatewayDev) {
		if ((aMaster == null) || (aGatewayDev == null))
			return;
		for (BleMeshDevice aDevice : listeners) {
			BleMeshTopologyCtrlBlock currTopologyCtr = aDevice
					.getDevMiddleware().getTopologyCtrlBlock();
			for (BleMeshDevGatewayTable aRow : currTopologyCtr.getDevPiconet()
					.get(0).getGatewaytable()) {
				if (aRow.getPicLocalMaster().equals(aMaster)
						&& aRow.getPicLocalGatewayDev().equals(aGatewayDev)
						&& aRow.getPicLocalGatewayCapacity() > 0) {
					aRow.setRelayEnergyProf(aRow.getRelayEnergyProf() * 0.66);
				}
			}
		}
	}

	public double networkResidualEnergy() {
		double resEnergy = 0.0;
		for (BleMeshDevice aDevice : listeners) {
			resEnergy = resEnergy
					+ aDevice.getDevEnergyChar().getCurrEnergyLevel();
		}
		return resEnergy;

	}

}
