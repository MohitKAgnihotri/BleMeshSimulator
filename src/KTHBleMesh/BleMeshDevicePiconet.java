package KTHBleMesh;



import java.util.ArrayList;
import java.util.List;

/**
 * TODO Update this auto-generated comment!
 *
 * @author Mohit Agnihotri
 */
public class BleMeshDevicePiconet {

	/* Role of the device */
	private int devRole;
	private BleMeshDevice devMaster;
	private List<BleMeshDevice> devSlaves;
	private List<BleMeshDevice> devSecNeighbours;
	private List<BleMeshDevGatewayTable> Gatewaytable;
	private List<BleMeshDevice> Relays;

	/**
	 * Constructs a {@code BleMeshDevicePiconet}. TODO Update this
	 * auto-generated comment!
	 *
	 * @param aDevRole
	 * @param aDevMaster
	 */
	public BleMeshDevicePiconet(int aDevRole, BleMeshDevice aDevMaster) {
		devRole = aDevRole;
		devMaster = aDevMaster;
		devSlaves = new ArrayList<BleMeshDevice>();
		devSecNeighbours = new ArrayList<BleMeshDevice>();
		Gatewaytable = new ArrayList<BleMeshDevGatewayTable>();
		Relays = new ArrayList<BleMeshDevice>();
	}

	/**
	 * Returns the devRole.
	 *
	 * @return the devRole
	 */
	public int getDevRole() {
		return devRole;
	}

	/**
	 * Sets the devRole.
	 *
	 * @param aDevRole
	 *            the devRole
	 */
	public void setDevRole(int aDevRole) {
		devRole = aDevRole;
	}

	/**
	 * Returns the devMaster.
	 *
	 * @return the devMaster
	 */
	public BleMeshDevice getDevMaster() {
		return devMaster;
	}

	/**
	 * Sets the devMaster.
	 *
	 * @param aDevMaster
	 *            the devMaster
	 */
	public void setDevMaster(BleMeshDevice aDevMaster) {
		devMaster = aDevMaster;
	}

	/**
	 * Returns the devSlaves.
	 *
	 * @return the devSlaves
	 */
	public List<BleMeshDevice> getDevSlaves() {
		return devSlaves;
	}

	/**
	 * Sets the devSlaves.
	 *
	 * @param aDevSlaves
	 *            the devSlaves
	 */
	public void setDevSlaves(List<BleMeshDevice> aDevSlaves) {
		devSlaves = aDevSlaves;
	}

	/**
	 * Returns the devSecNeighbours.
	 *
	 * @return the devSecNeighbours
	 */
	public List<BleMeshDevice> getDevSecNeighbours() {
		return devSecNeighbours;
	}

	/**
	 * Sets the devSecNeighbours.
	 *
	 * @param aDevSecNeighbours
	 *            the devSecNeighbours
	 */
	public void setDevSecNeighbours(List<BleMeshDevice> aDevSecNeighbours) {
		devSecNeighbours = aDevSecNeighbours;
	}

	/**
	 * Returns the gatewaytable.
	 *
	 * @return the gatewaytable
	 */
	public List<BleMeshDevGatewayTable> getGatewaytable() {
		return Gatewaytable;
	}

	/**
	 * Sets the gatewaytable.
	 *
	 * @param aGatewaytable
	 *            the gatewaytable
	 */
	public void setGatewaytable(List<BleMeshDevGatewayTable> aGatewaytable) {
		Gatewaytable = aGatewaytable;
	}

	public void decrementGatewayDeviceCap(BleMeshDevice aGWDevice) {
		for (BleMeshDevGatewayTable aRow : Gatewaytable) {
			if (aRow.getPicLocalGatewayDev().equals(aGWDevice))
				aRow.setPicLocalGatewayCapacity(aRow
						.getPicLocalGatewayCapacity() - 1);
		}

	}

	/**
	 * Returns the relays.
	 *
	 * @return the relays
	 */
	public List<BleMeshDevice> getRelays() {
		return Relays;
	}

	/**
	 * Sets the relays.
	 *
	 * @param aRelays
	 *            the relays
	 */
	public void setRelays(List<BleMeshDevice> aRelays) {
		Relays = aRelays;
	}

}
