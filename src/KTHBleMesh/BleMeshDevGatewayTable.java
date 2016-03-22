package KTHBleMesh;

import java.util.Comparator;

/**
 * This class is used to hold the piconet information used during piconet
 * interconnection.
 *
 * @author Mohit Agnihotri
 */
public class BleMeshDevGatewayTable {

	/*
	 * Each master u of a piconet constructs a gateway table. The entries of a
	 * gateway table represent all the rules that can be applied o merge with
	 * neighbor piconets. Each entry consists of the following elements:
	 */

	/* picRemoteMaster: The Master of the neighbor piconet */
	private BleMeshDevice picLocalMaster;

	/* The appointed gateway of local piconet (can be same a piconet master). */
	private BleMeshDevice picLocalGatewayDev;

	/* picRemoteMaster: The Master of the neighbor piconet */
	private BleMeshDevice picRemoteMaster;

	/* The appointed gateway of remote piconet (can be same a piconet master). */
	private BleMeshDevice picRemoteGatewayDev;

	/* The piconet capacity of picLocalGatewayDev. */
	private int picLocalGatewayCapacity;

	/* The piconet capacity of picRemoteGatewayCapacity. */
	private int picRemoteGatewayCapacity;

	/* Interconnection rule */
	private int picInterconnectionRule;

	/*
	 * the role to be played in the new relation (either M or S). If role is M,
	 * LocalGateway becomes master to RemoteGateway according to interconnection
	 * rule I. If role is S, LocalGateway becomes slave to RemoteGateway.
	 */
	private int picRole;

	/* Color of the link between the Local and Remote Gateway */
	private int picLinkColor;

	/*
	 * This stores the energy distribution of the bridge. Currently it is
	 * assigned to min(devA.energy,devB.energy). A relay with higher value of
	 * energyProf is preferred among same rule relays.
	 */
	private double relayEnergyProf;

	/**
	 * Constructs a {@code BleMeshDevGatewayTable}. TODO Update this
	 * auto-generated comment!
	 *
	 * @param aPicLocalMaster
	 * @param aPicLocalGatewayDev
	 * @param aPicRemoteMaster
	 * @param aPicRemoteGatewayDev
	 * @param aPicLocalGatewayCapacity
	 * @param aPicRemoteGatewayCapacity
	 * @param aPicLinkColor
	 * @param aRelayEnergyProf
	 */
	public BleMeshDevGatewayTable(BleMeshDevice aPicLocalMaster,
			BleMeshDevice aPicLocalGatewayDev, BleMeshDevice aPicRemoteMaster,
			BleMeshDevice aPicRemoteGatewayDev, int aPicLocalGatewayCapacity,
			int aPicRemoteGatewayCapacity, int aPicLinkColor,
			double aRelayEnergyProf) {
		picLocalMaster = aPicLocalMaster;
		picLocalGatewayDev = aPicLocalGatewayDev;
		picRemoteMaster = aPicRemoteMaster;
		picRemoteGatewayDev = aPicRemoteGatewayDev;
		picLocalGatewayCapacity = aPicLocalGatewayCapacity;
		picRemoteGatewayCapacity = aPicRemoteGatewayCapacity;
		picInterconnectionRule = 0;
		picRole = 0;
		picLinkColor = aPicLinkColor;
		relayEnergyProf = aRelayEnergyProf;
	}

	/**
	 * Returns the picRemoteMaster.
	 *
	 * @return the picRemoteMaster
	 */
	public BleMeshDevice getPicRemoteMaster() {
		return picRemoteMaster;
	}

	/**
	 * Sets the picRemoteMaster.
	 *
	 * @param aPicRemoteMaster
	 *            the picRemoteMaster
	 */
	public void setPicRemoteMaster(BleMeshDevice aPicRemoteMaster) {
		picRemoteMaster = aPicRemoteMaster;
	}

	/**
	 * Returns the picLocalGatewayDev.
	 *
	 * @return the picLocalGatewayDev
	 */
	public BleMeshDevice getPicLocalGatewayDev() {
		return picLocalGatewayDev;
	}

	/**
	 * Sets the picLocalGatewayDev.
	 *
	 * @param aPicLocalGatewayDev
	 *            the picLocalGatewayDev
	 */
	public void setPicLocalGatewayDev(BleMeshDevice aPicLocalGatewayDev) {
		picLocalGatewayDev = aPicLocalGatewayDev;
	}

	/**
	 * Returns the picRemoteGatewayDev.
	 *
	 * @return the picRemoteGatewayDev
	 */
	public BleMeshDevice getPicRemoteGatewayDev() {
		return picRemoteGatewayDev;
	}

	/**
	 * Sets the picRemoteGatewayDev.
	 *
	 * @param aPicRemoteGatewayDev
	 *            the picRemoteGatewayDev
	 */
	public void setPicRemoteGatewayDev(BleMeshDevice aPicRemoteGatewayDev) {
		picRemoteGatewayDev = aPicRemoteGatewayDev;
	}

	/**
	 * Returns the picLocalGatewayCapacity.
	 *
	 * @return the picLocalGatewayCapacity
	 */
	public int getPicLocalGatewayCapacity() {
		return picLocalGatewayCapacity;
	}

	/**
	 * Sets the picLocalGatewayCapacity.
	 *
	 * @param aPicLocalGatewayCapacity
	 *            the picLocalGatewayCapacity
	 */
	public void setPicLocalGatewayCapacity(int aPicLocalGatewayCapacity) {
		picLocalGatewayCapacity = aPicLocalGatewayCapacity;
	}

	/**
	 * Returns the picInterconnectionRule.
	 *
	 * @return the picInterconnectionRule
	 */
	public int getPicInterconnectionRule() {
		return picInterconnectionRule;
	}

	/**
	 * Sets the picInterconnectionRule.
	 *
	 * @param aPicInterconnectionRule
	 *            the picInterconnectionRule
	 */
	public void setPicInterconnectionRule(int aPicInterconnectionRule) {
		picInterconnectionRule = aPicInterconnectionRule;
	}

	/**
	 * Returns the picRole.
	 *
	 * @return the picRole
	 */
	public int getPicRole() {
		return picRole;
	}

	/**
	 * Sets the picRole.
	 *
	 * @param aPicRole
	 *            the picRole
	 */
	public void setPicRole(int aPicRole) {
		picRole = aPicRole;
	}

	/**
	 * Returns the picLinkColor.
	 *
	 * @return the picLinkColor
	 */
	public int getPicLinkColor() {
		return picLinkColor;
	}

	/**
	 * Sets the picLinkColor.
	 *
	 * @param aPicLinkColor
	 *            the picLinkColor
	 */
	public void setPicLinkColor(int aPicLinkColor) {
		picLinkColor = aPicLinkColor;
	}

	/**
	 * Returns the picLocalMaster.
	 *
	 * @return the picLocalMaster
	 */
	public BleMeshDevice getPicLocalMaster() {
		return picLocalMaster;
	}

	/**
	 * Sets the picLocalMaster.
	 *
	 * @param aPicLocalMaster
	 *            the picLocalMaster
	 */
	public void setPicLocalMaster(BleMeshDevice aPicLocalMaster) {
		picLocalMaster = aPicLocalMaster;
	}

	/**
	 * Returns the picRemoteGatewayCapacity.
	 *
	 * @return the picRemoteGatewayCapacity
	 */
	public int getPicRemoteGatewayCapacity() {
		return picRemoteGatewayCapacity;
	}

	/**
	 * Sets the picRemoteGatewayCapacity.
	 *
	 * @param aPicRemoteGatewayCapacity
	 *            the picRemoteGatewayCapacity
	 */
	public void setPicRemoteGatewayCapacity(int aPicRemoteGatewayCapacity) {
		picRemoteGatewayCapacity = aPicRemoteGatewayCapacity;
	}

	class BleMeshDevGatewayComparator implements
			Comparator<BleMeshDevGatewayTable> {
		public int compare(BleMeshDevGatewayTable gw1,
				BleMeshDevGatewayTable gw2) {
			int inteconnectRule = ((Integer) gw1.getPicInterconnectionRule())
					.compareTo(gw1.getPicInterconnectionRule());
			return ((inteconnectRule == 0) ? ((Integer) (-1 * gw1
					.getPicLocalGatewayCapacity())).compareTo(-1
					* gw2.getPicLocalGatewayCapacity()) : inteconnectRule);
		}
	}

	/**
	 * {@code BleMeshGatewayRuleDef} The bridge with the highest priority
	 * interconnect rule is picked from the possible relays. Rule of priority
	 * are : Pri 1 : 3 Hop bridge Pri 2 : Various configuration of 2 hop bridge
	 * (keeping piconet size =< 7) Pri 3 : 1 hop bridge
	 */
	public static Comparator<BleMeshDevGatewayTable> BleMeshGatewayRuleDef = new Comparator<BleMeshDevGatewayTable>() {

		@Override
		public int compare(BleMeshDevGatewayTable gw1,
				BleMeshDevGatewayTable gw2) {
			if ((gw1.getPicInterconnectionRule() - gw2
					.getPicInterconnectionRule()) == 0) {
				return (-1 * gw1.getPicLocalGatewayCapacity())
						- (-1 * gw2.getPicLocalGatewayCapacity());
			} else {
				return gw1.getPicInterconnectionRule()
						- gw2.getPicInterconnectionRule();
			}
		}
	};

	/**
	 * {@code BleMeshGatewayRuleDefEnergy} The bridge with the highest priority
	 * interconnect rule is picked from the possible relays. Rule of priority
	 * are : Pri 1 : 3 Hop bridge Pri 2 : Various configuration of 2 hop bridge
	 * (keeping piconet size =< 7) Pri 3 : 1 hop bridge
	 *
	 * Among the chosen bridges, a bridge pair with min-max energy is chosen
	 * over a random pair.
	 */
	public static Comparator<BleMeshDevGatewayTable> BleMeshGatewayRuleDefEnergy = new Comparator<BleMeshDevGatewayTable>() {

		@Override
		public int compare(BleMeshDevGatewayTable gw1,
				BleMeshDevGatewayTable gw2) {
			if ((gw1.getPicInterconnectionRule() - gw2
					.getPicInterconnectionRule()) == 0
					&& (gw1.getPicLocalGatewayCapacity() - gw2
							.getPicLocalGatewayCapacity()) == 0) {
				return (int) ((-1 * gw1.getRelayEnergyProf()) - (-1 * gw2
						.getRelayEnergyProf()));
			} else if ((gw1.getPicInterconnectionRule() - gw2
					.getPicInterconnectionRule()) == 0) {
				return (-1 * gw1.getPicLocalGatewayCapacity())
						- (-1 * gw2.getPicLocalGatewayCapacity());
			} else {
				return gw1.getPicInterconnectionRule()
						- gw2.getPicInterconnectionRule();
			}
		}
	};

	/**
	 * TODO Update this auto-generated comment for {@code BleMeshDevAddress}
	 * Only consideration is given for the Energy. The bridges are chosen if
	 * they have the maximum energy among their peers
	 */
	public static Comparator<BleMeshDevGatewayTable> BleMeshGatewayRuleOnlyEnergy = new Comparator<BleMeshDevGatewayTable>() {

		@Override
		public int compare(BleMeshDevGatewayTable gw1,
				BleMeshDevGatewayTable gw2) {

			return (int) ((-1 * gw1.getRelayEnergyProf()) - (-1 * gw2
					.getRelayEnergyProf()));

		}
	};

	/** Only hop length is considered while selecting the bridges */
	public static Comparator<BleMeshDevGatewayTable> BleMeshGatewayRuleHop = new Comparator<BleMeshDevGatewayTable>() {

		@Override
		public int compare(BleMeshDevGatewayTable gw1,
				BleMeshDevGatewayTable gw2) {
			if ((gw1.getPicInterconnectionRule() - gw2
					.getPicInterconnectionRule()) == 0) {
				return (int) ((-1 * gw1.getRelayEnergyProf()) - (-1 * gw2
						.getRelayEnergyProf()));
			} else {
				return -1
						* (gw1.getPicInterconnectionRule() - gw2
								.getPicInterconnectionRule());
			}
		}
	};

	/**
	 * Returns the relayEnergyProf.
	 *
	 * @return the relayEnergyProf
	 */
	public double getRelayEnergyProf() {
		return relayEnergyProf;
	}

	/**
	 * Sets the relayEnergyProf.
	 *
	 * @param aRelayEnergyProf
	 *            the relayEnergyProf
	 */
	public void setRelayEnergyProf(double aRelayEnergyProf) {
		relayEnergyProf = aRelayEnergyProf;
	}
}
