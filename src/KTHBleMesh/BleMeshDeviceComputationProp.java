package KTHBleMesh;



/**
 * TODO Update this auto-generated comment!
 *
 * @author Mohit Agnihotri
 */
public class BleMeshDeviceComputationProp {

	public enum ComputeClass {
		DEV_COMPUTE_CLASS_SENSOR, DEV_COMPUTE_CLASS_APPLIANCE, DEV_COMPUTE_CLASS_PC
	}

	public enum MemoryClass {
		DEV_MEMORY_CLASS_SENSOR, DEV_MEMORY_CLASS_APPLIANCE, DEV_MEMORY_CLASS_PC
	}

	private ComputeClass computeClass;
	private MemoryClass memoryClass;

	/**
	 * Constructs a {@code BleMeshDeviceComputationProp}. TODO Update this
	 * auto-generated comment!
	 *
	 * @param aComputeClass
	 */
	public BleMeshDeviceComputationProp() {
		computeClass = ComputeClass.DEV_COMPUTE_CLASS_SENSOR;
		memoryClass = MemoryClass.DEV_MEMORY_CLASS_SENSOR;
	}

	/**
	 * Returns the computeClass.
	 *
	 * @return the computeClass
	 */
	public ComputeClass getComputeClass() {
		return computeClass;
	}

	/**
	 * Sets the computeClass.
	 *
	 * @param aComputeClass
	 *            the computeClass
	 */
	public void setComputeClass(ComputeClass aComputeClass) {
		computeClass = aComputeClass;
	}

	/**
	 * Returns the memoryClass.
	 *
	 * @return the memoryClass
	 */
	public MemoryClass getMemoryClass() {
		return memoryClass;
	}

	/**
	 * Sets the memoryClass.
	 *
	 * @param aMemoryClass
	 *            the memoryClass
	 */
	public void setMemoryClass(MemoryClass aMemoryClass) {
		memoryClass = aMemoryClass;
	}

}
