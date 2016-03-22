package KTHBleMesh;


/**
 * TODO Update this auto-generated comment!
 *
 * @author Mohit Agnihotri
 */
public class BleMeshMiddleware {

	private BleMeshTopologyCtrlBlock topologyCtrlBlock;
	private BleMeshRoutingCtrlBlock routingCtrlBlock;



	public BleMeshMiddleware() {
		topologyCtrlBlock = new BleMeshTopologyCtrlBlock();
		routingCtrlBlock = new BleMeshRoutingCtrlBlock();
	}

	/**
	 * Returns the topologyCtrlBlock.
	 *
	 * @return the topologyCtrlBlock
	 */
	public BleMeshTopologyCtrlBlock getTopologyCtrlBlock() {
		return topologyCtrlBlock;
	}

	/**
	 * Sets the topologyCtrlBlock.
	 *
	 * @param aTopologyCtrlBlock
	 *            the topologyCtrlBlock
	 */
	public void setTopologyCtrlBlock(BleMeshTopologyCtrlBlock aTopologyCtrlBlock) {
		topologyCtrlBlock = aTopologyCtrlBlock;
	}

	/**
	 * Returns the routingCtrlBlock.
	 *
	 * @return the routingCtrlBlock
	 */
	public BleMeshRoutingCtrlBlock getRoutingCtrlBlock() {
		return routingCtrlBlock;
	}

	/**
	 * Sets the routingCtrlBlock.
	 *
	 * @param aRoutingCtrlBlock
	 *            the routingCtrlBlock
	 */
	public void setRoutingCtrlBlock(BleMeshRoutingCtrlBlock aRoutingCtrlBlock) {
		routingCtrlBlock = aRoutingCtrlBlock;
	}

}
