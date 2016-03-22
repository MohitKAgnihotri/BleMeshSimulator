// 

package KTHBleMesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.javatuples.Pair;

/**
 * TODO Update this auto-generated comment!
 *
 * @author Mohit Agnihotri
 */
public class BleMeshRoutingCtrlBlock {

	int packetsRecived;

	/*
	 * This is used to store the discovered routes in the network
	 */
	HashMap<Pair, List<List<Integer>>> Routes;

	/**
	 * Returns the packetsRecived.
	 *
	 * @return the packetsRecived
	 */
	public int getPacketsRecived() {
		return packetsRecived;
	}

	/**
	 * Sets the packetsRecived.
	 *
	 * @param aPacketsRecived
	 *            the packetsRecived
	 */
	public void setPacketsRecived(int aPacketsRecived) {
		packetsRecived = aPacketsRecived;
	}

	List<List<Integer>> directNodes;
	List<Integer> relayNodes;



	public BleMeshRoutingCtrlBlock() {
		directNodes = new ArrayList<List<Integer>>();
		relayNodes = new ArrayList<Integer>();
		packetsRecived = 0;
		Routes = new HashMap<Pair, List<List<Integer>>>();
	}

	/**
	 * Returns the routes.
	 *
	 * @return the routes
	 */
	public List<List<Integer>> getdirectNodes() {
		return directNodes;
	}

	/**
	 * Sets the routes.
	 *
	 * @param aRoutes
	 *            the routes
	 */
	public void setdirectNodes(List<List<Integer>> aRoutes) {
		directNodes = aRoutes;
	}

	/**
	 * Returns the relayNodes.
	 *
	 * @return the relayNodes
	 */
	public List<Integer> getRelayNodes() {
		return relayNodes;
	}

	/**
	 * Sets the relayNodes.
	 *
	 * @param aRelayNodes
	 *            the relayNodes
	 */
	public void setRelayNodes(List<Integer> aRelayNodes) {
		relayNodes = aRelayNodes;
	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aPacket
	 * @return
	 */
	public List<Integer> floodBasedRouting(BleMeshPacket aPacket) {

		/*
		 * First check if the messages originated from a valid source. The
		 * message source must be one of the direct destinations
		 */
		boolean validSrcFlag = false;
		int piconetId = 255;
		// int srcAdd = aPacket.getFromAddress();
		int dstAdd = aPacket.getToAddress();
		int viaAdd = aPacket.getViaAddress();

		List<Integer> nextHopDevices = new ArrayList<Integer>();

		for (List<Integer> aDirectDestList : directNodes) {
			if (aDirectDestList.contains(viaAdd)) {
				validSrcFlag = true;
				piconetId = directNodes.indexOf(aDirectDestList);
				break;
			}
		}

		if (validSrcFlag == true) {
			/*
			 * Check if destination is directly reachable in any of the piconets
			 * example: Packet originating at Master for a slave
			 */
			try {

				for (List<Integer> aDirectDestList : directNodes) {
					if (aDirectDestList.contains(dstAdd)) {
						// System.out.printf("Packet must be sent to directly reachable destination to %d \n",
						// dstAdd);
						nextHopDevices.add(dstAdd);
						return nextHopDevices;
					}
				}

				if (relayNodes.size() > 0) {
					for (int aHop : relayNodes) {
						if (aHop != viaAdd && !aPacket.getHops().contains(aHop)
								&& !nextHopDevices.contains(aHop)) {
							// System.out.printf("Packet must be forwarded from %d to %d \n",
							// viaAdd,aHop);
							nextHopDevices.add(aHop);
						}
					}

				}

				for (List<Integer> aDirectDestList : directNodes) {
					if (!(piconetId == directNodes.indexOf(aDirectDestList))) {
						for (int aHop : aDirectDestList) {
							if (aHop != viaAdd
									&& !aPacket.getHops().contains(aHop)
									&& !nextHopDevices.contains(aHop)) {
								// System.out.printf("Packet must be forwarded from %d to %d \n",
								// viaAdd,aHop);
								nextHopDevices.add(aHop);
							}
						}

					}
				}

			} catch (Exception e) {
				// System.out.println("Incorrect Piconet Id \n");
			}

		} else {
			// System.out.printf("Dropping packet intended to  %d -> %d \n",srcAdd,
			// dstAdd);
		}

		return nextHopDevices;

	}

	/**
	 * TODO Update this auto-generated comment!
	 *
	 * @param aPacket
	 * @return
	 */
	public List<Integer> Route(BleMeshPacket aPacket) {

		// int srcAdd = aPacket.getFromAddress();
		int dstAdd = aPacket.getToAddress();
		int viaAdd = aPacket.getViaAddress();
		int min = 100;
		List<Integer> route = null;

		if (isRouteAvailable(viaAdd, dstAdd)) {
			/* If it is, use one of the discovered routes for the packet */
			List<List<Integer>> routeList;
			routeList = getDiscoveredRoute(viaAdd, dstAdd);

			/* Mark the packet to use pre-configured route */
			for (List<Integer> aRoute : routeList) {
				if (aRoute.size() < min) {
					aPacket.setRouteAvail(true);
					aPacket.getRoute().addAll(aRoute);
					min = aRoute.size();
				}
			}
		} else {
			/* Flood the network */
			route = floodBasedRouting(aPacket);
		}

		return route;

	}

	boolean isRouteAvailable(int src, int dst) {
		if (Routes != null) {
			if (Routes.containsKey(Pair.with(src, dst))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	List<List<Integer>> getDiscoveredRoute(int src, int dst) {

		Pair<Integer, Integer> pair = Pair.with(src, dst);
		List<List<Integer>> routes = Routes.get(pair);
		return routes;
	}

	BleMeshPacket ProcessPacket(BleMeshPacket aPacket, BleMeshDevice device) {

		BleMeshPacket packet = null;

		if ((aPacket.getType() != 0) && (aPacket.getType() != 10) && (aPacket.getType() != 2) ) {
			return packet;
		} 
		
		if (aPacket.getType() == 2) {
			
			//System.out.println("Received failed packet received on " + device.getDevUniqueId() + " from " + aPacket.getFromAddress());
			System.out.printf("Received failed packet from =[%d] to Destinatoin=[%d] via Hops=%s\n" ,
				     aPacket.getFromAddress(), aPacket.getToAddress(),
				     aPacket.getHops().toString());
			
			// if there is only 1 bridge, here is the end of network lifetime!!!! Perform the logging!
			
			int failedDevice = aPacket.getFromAddress();
			// update the routing table, remove failed device from list of direct and relay nodes
			List<List<Integer>> directNodes = this.getdirectNodes();
			for (List<Integer> directNodesList:directNodes) {
				if (directNodesList.contains(failedDevice)) {
					int index = directNodesList.indexOf(failedDevice);
					directNodesList.remove(index);
				}
			}
			
			
			if (this.getRelayNodes().contains(failedDevice)) {
				int index = this.getRelayNodes().indexOf(failedDevice);
				this.getRelayNodes().remove(index);
			}
				
			// find the route(s) containing failed node, and delete that instance from discovered routes
			List<List<Integer>> routesList = this.getDiscoveredRoute(aPacket.getToAddress(), aPacket.getPreviousDestination());
			List<Integer> selectedRoute = null;
			int min = 10000;
			if (routesList!=null) {
				Iterator<List<Integer>> it = routesList.iterator();
				while (it.hasNext()) {
					List<Integer> aRoute=it.next();
					if (aRoute.contains(failedDevice)) {
						System.out.println("Removing route: " + aRoute.toString());
						it.remove();
					} 
				}
			}
			
			if (routesList!=null) {
				Iterator<List<Integer>> it = routesList.iterator();
				while (it.hasNext()) {
					List<Integer> aRoute=it.next();
					// select the second shortest route
					if (aRoute.size() < min) {
						selectedRoute = aRoute;
						min = aRoute.size();
						System.out.println("Selected route: " + selectedRoute.toString());
					}
				}
			}
			
			// create a new packet and send it to the destination via selected route
			if (selectedRoute!=null) {
				packet = new BleMeshPacket(aPacket.getId()+1, aPacket.getToAddress(), aPacket.getPreviousDestination(), 0);	
				packet.setType(1);
				packet.setTtl(1);
				packet.setRoute(selectedRoute);
				packet.setRouteAvail(true);
				System.out.println("New route: " + selectedRoute.toString());
			} else {
				System.out.println("No new routes, network parted!");
				boolean isSecondFailure = false;
				boolean isDeviceCritical = device.isDeviceCritical();
			    if (BleMeshSimulator.numBridges==2) {
					// end of network lifetime
			    	isSecondFailure = true;
			    	isDeviceCritical = true;
			    }
			    BleMeshSimulator.simLog.add(
						new BleMeshDeviceLog(
								Simulator.now(), 
								device.getDevUniqueId(), 
								device.getDevEnergyChar().getEnergySourceType(), 
								device.getDevEnergyChar().getInitEnergyLevel(), 
								device.getDevEnergyChar().getCurrEnergyLevel(), 
								isDeviceCritical, device.getDevEnergyChar().isDeviceAlive(), isSecondFailure));
			    
			    if (isSecondFailure && isDeviceCritical)
			    	System.exit(-1);
			}
			return packet;
		}
		
		System.out.printf("Adding\tnew\tRoute\tfrom\tSrc=[\t%d\t]\tDest=[\t%d\t]\tRoute => \t%s\t\n",
						aPacket.getFromAddress(), aPacket.getToAddress(),
						aPacket.getHops().toString());

		int src = aPacket.getToAddress();
		List<Integer> traversedPath = new ArrayList<Integer>();

		traversedPath.addAll(aPacket.getHops());
		int index = traversedPath.indexOf(src);
		traversedPath.remove(index);
		Collections.reverse(traversedPath);

		while (traversedPath.size() > 0) {
			Pair<Integer, Integer> pair = Pair.with(aPacket.getToAddress(),
					traversedPath.get(traversedPath.size() - 1));
			if (Routes.containsKey(pair)) {
				List<List<Integer>> temp = Routes.get(pair);
				if (!temp.contains(traversedPath)) {
					List<Integer> route = new ArrayList<Integer>();
					route.addAll(traversedPath);
					temp.add(route);
					Routes.replace(pair, temp);
					System.out.println(pair + " " + route);
				}
			} else {
				List<List<Integer>> temp = new ArrayList<List<Integer>>();
				List<Integer> route = new ArrayList<Integer>();
				route.addAll(traversedPath);
				temp.add(route);
				Routes.put(pair, temp);
				System.out.println(pair + " " + route);
			}

			traversedPath.remove(traversedPath.size() - 1);
		}

		/*
		 * Reply with a reverse routing packets enabling mutual route
		 * determination
		 */
		if (aPacket.getType() == 0) {
			double packetId = Simulator.getRandom();

			packet = new BleMeshPacket(packetId, src, aPacket.getFromAddress(), 100);
			packet.setType(10);

			traversedPath.addAll(aPacket.getHops());
			Collections.reverse(traversedPath);
			traversedPath.remove(traversedPath.indexOf(src));

			packet.setRoute(traversedPath);
			packet.setRouteAvail(true);
		} 

		return packet;
	}
}
