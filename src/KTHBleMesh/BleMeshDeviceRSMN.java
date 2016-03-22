

package KTHBleMesh;

import KTHBleMesh.BleMeshDeviceComputationProp.ComputeClass;
import KTHBleMesh.BleMeshDeviceComputationProp.MemoryClass;
import KTHBleMesh.BleMeshDeviceEnergyProp.EnergySource;

/**
 * TODO Update this auto-generated comment!
 *
 * @author TODO insert full name of main author(s)
 */
public class BleMeshDeviceRSMN extends BleMeshDevice {


	private int initRSMValue;

	/**
	 * TODO This function returns the unique id of the device to be used by the
	 * algorithm.
	 *
	 * @return
	 */
	@Override
	public int getDevUniqueId() {
		return this.initRSMValue;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 */
	public void ComputeInitalRSM() {
		initRSMValue = 0;


		EnergySource powClass = this.getDevEnergyChar().getEnergySourceType();

		

		if (powClass == EnergySource.BLE_DEV_CONNECTED_SUPPLY) {
			initRSMValue += 200;

			/* if the device has higher neighbors, it is preffered */
			initRSMValue += (3 * this.getDevMiddleware().getTopologyCtrlBlock()
					.getLowerIdNeighbourDevice(this).size());
		} else if (powClass == EnergySource.BLE_DEV_BATTRERY_RECHARGEABLE) {

			initRSMValue += this.getDevEnergyChar().getInitEnergyLevel();
		}

		else if (powClass == EnergySource.BLE_DEV_BATTRERY_NON_RECHARGEABLE) {
			initRSMValue += Math.ceil(this.getDevEnergyChar()
					.getInitEnergyLevel() * 0.1);
		}

		// currESMValue = initRSMValue;
	}

	
	/* This should be enabled to use optimized inter-connect rules.*/
	// /** {@inheritDoc} */
	// @Override
	// public void genPiconetInterconnectRules(BleMeshDevice aMaster) {
	//
	// List<BleMeshDevGatewayTable> gatewayTable
	// =
	// aMaster.getDevMiddleware().getTopologyCtrlBlock().getDevPiconet().get(0).getGatewaytable();
	//
	// if(gatewayTable.size() > 0)
	// {
	// List<BleMeshDevGatewayTable> toRemove = new
	// ArrayList<BleMeshDevGatewayTable>();
	// for(BleMeshDevGatewayTable aEntry: gatewayTable)
	// {
	// if ((aEntry.getPicLocalGatewayCapacity() == 0)
	// || (aEntry.getPicRemoteGatewayCapacity() == 0))
	// {
	// toRemove.add(aEntry);
	// continue;
	// }
	//
	// if(aMaster.getDevUniqueId() >
	// aEntry.getPicRemoteMaster().getDevUniqueId())
	// {
	// /*Check for Rule 1 : Three-hop interconnection
	// * Two piconet q(u) and q(v) may be interconnected through an edge
	// * e between two slaves localGateway and RemoteGateway, where
	// * c(e) = green. (Operation: s_u captures s_v)
	// *
	// * u ---> s_u --g-- s_v <--- v => u ---> s_u ---> s_v <--- v
	// * u & v are the masters of respective piconets and
	// * s_u & s_v are slaves of master u & v
	// */
	// if(aEntry.getPicLinkColor() == 3
	// && !(aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))
	// && !(aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())))
	// {
	// aEntry.setPicRole(1);
	// aEntry.setPicInterconnectionRule(2);
	// }
	//
	//
	//
	// /*Check for Rule 2-a : Two-hop interconnection c(localGateway,
	// RemoteGateway) = green,
	// * localGateway is a slave of u and RemoteGateway is a master
	// * of piconet v. (Operation: localGateway captures RemoteGateway.)
	// * u ---> s_u --g-- v => u---> s_u ---> v
	// * u & v are the masters of respective piconets and s_u is slave of master
	// u
	// * (Operation: s_u captures v.)*/
	//
	// else if(aEntry.getPicLinkColor() == 3
	// && (aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))
	// && !(aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())))
	// {
	// aEntry.setPicRole(2);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	// else if (aEntry.getPicLinkColor() == 3
	// && !(aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))
	// && (aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())))
	// {
	// aEntry.setPicRole(1);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	// /*
	// I-Rule 2-b: Through the edge (v, s_x) or (u, s_x) where
	// s_x is slave of u or v, and c((v, s_x)) = silver or
	// c((u, s_x)) = silver (that is, s_x is smaller than both
	// u and v, and it belong to either q(u) or q(v) but not
	// both. Both piconets attempted to slave s_x but only one
	// of them was successful). (Operation: v captures s_x or
	// u captures s_x.)
	//
	// u ---> s_u --g-- v => u--->s_u<--- v
	// or
	// u --g-- s_v <--- v => u ---> s_v <--- v
	// */
	// else if (aEntry.getPicLinkColor() == 2
	// && ((aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())
	// &&
	// !(aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster())))))
	// {
	// aEntry.setPicRole(1);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	//
	// else if (aEntry.getPicLinkColor() == 2
	// && (!(aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster()))
	// &&
	// (aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))))
	// {
	// aEntry.setPicRole(2);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	//
	// /*
	// * I-Rule 2-c: Through the edge (u, s_v) where s_v is slave of v
	// and c((u, s_v)) = red (that is, s_v is smaller than both
	// u and v. s_v is slaved by v, and s_v was delegated by u to
	// v). (Operation: s_v captures u.)
	// u --r-- s_v <--- v => u <--- s_v <--- v
	// * */
	// if (aEntry.getPicLinkColor() == 4
	// && aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())
	// &&
	// !(aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster())))
	// {
	// aEntry.setPicRole(1);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	// /*
	// * I-Rule 2-c: Through the edge (s_u, v) where s_u is slave of u
	// and c((s_u, v)) = red (that is, s_u is smaller than both
	// u and v. s_u is slaved by u, and s_u was delegated by v to
	// u). (Operation: s_u captures v.)
	//
	// * */
	// else if (aEntry.getPicLinkColor() == 4
	// && !(aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster()))
	// && aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))
	// {
	// aEntry.setPicRole(2);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	// /*
	// * I-Rule 3 (One-hop interconnection): through the edge
	// e(u,v) where c((u, v)) = red. Both u and v are
	// masters of different piconets. (Operation: v captures u )
	// **/
	//
	// else if (aEntry.getPicLinkColor() == 4
	// && aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())
	// && aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))
	// {
	//
	// aEntry.setPicRole(2);
	// aEntry.setPicInterconnectionRule(3);
	//
	// }
	//
	// }
	//
	// else
	// {
	// /*Check for Rule 1 : Three-hop interconnection
	// * Two piconet q(u) and q(v) may be interconnected through an edge
	// * e between two slaves localGateway and RemoteGateway, where
	// * c(e) = green. (Operation: s_u captures s_v)
	// *
	// * u ---> s_u --g-- s_v <--- v => u ---> s_u ---> s_v <--- v
	// * u & v are the masters of respective piconets and
	// * s_u & s_v are slaves of master u & v
	// */
	// if(aEntry.getPicLinkColor() == 3
	// && !(aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))
	// && !(aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())))
	// {
	//
	// aEntry.setPicRole(2);
	// aEntry.setPicInterconnectionRule(2);
	//
	// }
	//
	// /*Check for Rule 2-a : Two-hop interconnection c(localGateway,
	// RemoteGateway) = green,
	// * localGateway is a slave of u and RemoteGateway is a master
	// * of piconet v. (Operation: localGateway captures RemoteGateway.)
	// * u ---> s_u --g-- v => u---> s_u ---> v
	// * u & v are the masters of respective piconets and s_u is slave of master
	// u
	// * (Operation: s_u captures v.)*/
	//
	// else if(aEntry.getPicLinkColor() == 3
	// && (aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))
	// && !(aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())))
	// {
	// aEntry.setPicRole(2);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	// else if (aEntry.getPicLinkColor() == 3
	// && !(aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))
	// && (aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())))
	// {
	// aEntry.setPicRole(1);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	// /*
	// I-Rule 2-b: Through the edge (v, s_x) or (u, s_x) where
	// s_x is slave of u or v, and c((v, s_x)) = silver or
	// c((u, s_x)) = silver (that is, s_x is smaller than both
	// u and v, and it belong to either q(u) or q(v) but not
	// both. Both piconets attempted to slave s_x but only one
	// of them was successful). (Operation: v captures s_x or
	// u captures s_x.)
	//
	// u ---> s_u --g-- v => u--->s_u<--- v
	// or
	// u --g-- s_v <--- v => u ---> s_v <--- v
	// */
	// else if (aEntry.getPicLinkColor() == 2
	// && ((aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())
	// &&
	// !(aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster())))))
	// {
	// aEntry.setPicRole(1);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	// else if (aEntry.getPicLinkColor() == 2
	// && ((!(aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster()))
	// &&
	// (aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster())))))
	// {
	// aEntry.setPicRole(2);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	// /*
	// * I-Rule 2-c: Through the edge (u, s_v) where s_v is slave of v
	// and c((u, s_v)) = red (that is, s_v is smaller than both
	// u and v. s_v is slaved by v, and s_v was delegated by u to
	// v). (Operation: s_v captures u.)
	// u --r-- s_v <--- v => u <--- s_v <--- v
	// * */
	// if (aEntry.getPicLinkColor() == 4
	// && aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())
	// &&
	// !(aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster())))
	// {
	// aEntry.setPicRole(1);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	// /*
	// * I-Rule 2-c: Through the edge (s_u, v) where s_u is slave of u
	// and c((s_u, v)) = red (that is, s_u is smaller than both
	// u and v. s_u is slaved by u, and s_u was delegated by v to
	// u). (Operation: s_u captures v.)
	//
	// * */
	// else if (aEntry.getPicLinkColor() == 4
	// && !(aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster()))
	// && aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))
	// {
	// aEntry.setPicRole(2);
	// aEntry.setPicInterconnectionRule(1);
	// }
	//
	// /*
	// * I-Rule 3 (One-hop interconnection): through the edge
	// e(u,v) where c((u, v)) = red. Both u and v are
	// masters of different piconets. (Operation: v captures u )
	// **/
	//
	// else if (aEntry.getPicLinkColor() == 4
	// && aEntry.getPicLocalGatewayDev().equals(aEntry.getPicLocalMaster())
	// && aEntry.getPicRemoteGatewayDev().equals(aEntry.getPicRemoteMaster()))
	// {
	//
	// aEntry.setPicRole(1);
	// aEntry.setPicInterconnectionRule(3);
	// }
	// }
	//
	// if(aEntry.getPicInterconnectionRule() == 0)
	// {
	// toRemove.add(aEntry);
	// }
	// else
	// {
	// logPicoInterconnect.log(this,
	// aEntry.getPicLocalMaster().getDevUniqueId(),
	// aEntry.getPicLocalGatewayDev().getDevUniqueId(),
	// aEntry.getPicRemoteMaster().getDevUniqueId(),
	// aEntry.getPicRemoteGatewayDev().getDevUniqueId(),
	// aEntry.getPicLinkColor(),
	// aEntry.getPicRole(),
	// aEntry.getPicLocalGatewayCapacity());
	// }
	//
	// }
	// gatewayTable.removeAll(toRemove);
	// }
	//
	// }

}
