// Copyright (c) 2015 ERICSSON AB

package KTHBleMesh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.eclipsesource.json.JsonObject;

import KTHBleMesh.BleMeshDeviceComputationProp.ComputeClass;
import KTHBleMesh.BleMeshDeviceComputationProp.MemoryClass;
import KTHBleMesh.BleMeshDeviceEnergyProp.EnergySource;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * BLE Mesh simulator
 *
 * @author Mohit Agnihotri
 */
public class BleMeshSimulator extends Simulator {

	static JsonObject jsonObject;
	static List<BleMeshDevice> devices;
	static BleMeshTxManager txManager;
	public static ArrayList<BleMeshDeviceLog> simLog;
	
	int depId;
	static int deviceType;
	static int gatewaySelAlgorithm;
	static int numBridges;

	BleMeshSimulator(long seed, double simDuration, int deviceType, int gatewaySelAlgorithm, String filePath, int numBridges) throws IOException{
		super(seed, simDuration);
		BleMeshSimulator.deviceType = deviceType;
		BleMeshSimulator.gatewaySelAlgorithm = gatewaySelAlgorithm;
		BleMeshSimulator.numBridges = numBridges;

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);

		jsonObject = JsonObject.readFrom(new FileReader(filePath));
		txManager = new BleMeshTxManager();
		devices = createDeviceCollection();
		simLog = new ArrayList<BleMeshDeviceLog>();

		for (int i = 0; i < devices.size(); i++) {
			devices.get(i).setTxManager(txManager);
			txManager.registerListener(devices.get(i));
		}
	}

	/**This function is required to be modified and act as the initilization function
	 * It would be wise to make an model (preferably JSON) to pass the file and use 
	 * it for initlization.*/
	private static List<BleMeshDevice> createDeviceCollection() {
		List<BleMeshDevice> devices = new ArrayList<BleMeshDevice>();
		int deployCnt = jsonObject.names().size();
		
		//System.out.println("deployCnt = " + deployCnt);
		for( String name : jsonObject.names() ) {
			 
			  JsonObject json2 = jsonObject.get( name ).asObject();
			  System.out.println(name);
			  int address = json2.get("address").asInt();
			  System.out.println("address = " + address);
			  float batterySize = json2.get("batterySize").asFloat();
			  System.out.println("batterySize = " + batterySize);
			  ComputeClass computeClass = null;
			  if (json2.get("computeClass").asString().equals(ComputeClass.DEV_COMPUTE_CLASS_SENSOR.toString()))
			  	computeClass = ComputeClass.DEV_COMPUTE_CLASS_SENSOR;
			  	else if (json2.get("computeClass").asString().equals(ComputeClass.DEV_COMPUTE_CLASS_APPLIANCE.toString()))
			  		computeClass = ComputeClass.DEV_COMPUTE_CLASS_APPLIANCE;
			  		else 
			  			computeClass = ComputeClass.DEV_COMPUTE_CLASS_PC;
			  System.out.println("computeClass = " + computeClass);
			  int delay = json2.get("delay").asInt();
			  System.out.println("delay = " + delay);
			  int destination = json2.get("destination").asInt();
			  System.out.println("destination = " + destination);
			  String energyDissipation = json2.get("energyDissipitation").toString();
			  System.out.println("energyDissipation = " + energyDissipation);
			  EnergySource energySourceType = null;
			  if (json2.get("energySourceType").asString().equals(EnergySource.BLE_DEV_BATTRERY_NON_RECHARGEABLE.toString()))
				  energySourceType = EnergySource.BLE_DEV_BATTRERY_NON_RECHARGEABLE;
			  else if (json2.get("energySourceType").asString().equals(EnergySource.BLE_DEV_BATTRERY_RECHARGEABLE.toString()))
				  energySourceType = EnergySource.BLE_DEV_BATTRERY_RECHARGEABLE;
			  else
				  energySourceType = EnergySource.BLE_DEV_CONNECTED_SUPPLY;
			  System.out.println("energySourceType = " + energySourceType);
			  int initEnergyLevel = json2.get("initEnergyLevel").asInt();
			  System.out.println("initEnergyLevel = " + initEnergyLevel);
			  int maxDistance = json2.get("maxDistance").asInt();
			  System.out.println("maxDistance = " + maxDistance);
			  MemoryClass memoryClass = null;
			  if (json2.get("memoryClass").asString().equals(MemoryClass.DEV_MEMORY_CLASS_SENSOR.toString()))
				  memoryClass = MemoryClass.DEV_MEMORY_CLASS_SENSOR;
				  	else if (json2.get("memoryClass").asString().equals(MemoryClass.DEV_MEMORY_CLASS_APPLIANCE.toString()))
				  		memoryClass = MemoryClass.DEV_MEMORY_CLASS_APPLIANCE;
				  		else 
				  			memoryClass = MemoryClass.DEV_MEMORY_CLASS_PC;
			  System.out.println("memoryClass = " + memoryClass);
			  
			  int nrofPackets = json2.get("nrofPackets").asInt();
			  System.out.println("nrofPackets = " + nrofPackets);
			  int period = json2.get("period").asInt();
			  System.out.println("period = " + period);
			  double x = json2.get("x").asDouble();
			  System.out.println("x = " + x);
			  double y = json2.get("y").asDouble();
			  System.out.println("y = " + y);
			  int z = json2.get("z").asInt();
			  System.out.println("z = " + z);
			  
			  BleMeshDevice aDevice = null;
			  switch(deviceType) {
			  case 1: aDevice = new BleMeshDevice();
			  		  break;
			  case 2: aDevice = new BleMeshDeviceRSM();
			  		  break;
			  case 3: aDevice = new BleMeshDeviceRSMN();
			  		  break;
			  }
			  
			  aDevice.getDevTrafficChar().setAddress(address);
			  aDevice.getDevMobilityChar().setX(x);
			  aDevice.getDevMobilityChar().setY(y);
			  aDevice.getDevMobilityChar().setZ(z);
			  aDevice.getDevRadioChar().setMaxDistance(maxDistance);
			  aDevice.getDevComputationChar().setComputeClass(computeClass);
			  aDevice.getDevComputationChar().setMemoryClass(memoryClass);
			  aDevice.getDevEnergyChar().setEnergySourceType(energySourceType);
			  aDevice.getDevEnergyChar().setInitEnergyLevel(initEnergyLevel);
			  aDevice.getDevEnergyChar().setBatterySize(batterySize);
			  aDevice.getTrafficModel().setPeriod(period);
			  aDevice.getTrafficModel().setDestination(destination);
			  aDevice.getTrafficModel().setNrofPackets(nrofPackets);
			  aDevice.getTrafficModel().setDelay(delay);
			  aDevice.getDevMiddleware().getTopologyCtrlBlock().setCapacity(7);
			  
			  aDevice.bleDeviceName = new String("BleDevice" + address);
			  aDevice.setGatewaySelRule(gatewaySelAlgorithm);
			  devices.add(aDevice);
			}
			return devices;
	}
	

	
	/**This function is required to be modified and act as the initilization function
	 * It would be wise to make an model (preferably JSON) to pass the file and use 
	 * it for initlization.*/
	/*
	private static List<BleMeshDevice> createDeviceCollection() {
		List<BleMeshDevice> devices = new ArrayList<BleMeshDevice>();
		for (int i = 0; i < 50; i++) {
		
			BleMeshDevice aDevice = new BleMeshDevice();
			aDevice.getDevTrafficChar().setAddress(i);
			aDevice.getDevMobilityChar().setX(Math.random() * 100);
			aDevice.getDevMobilityChar().setY(Math.random() * 100);
			aDevice.getDevMobilityChar().setZ(1);
			aDevice.getDevRadioChar().setMaxDistance(10);
			aDevice.getDevComputationChar().setComputeClass(
					ComputeClass.DEV_COMPUTE_CLASS_APPLIANCE);
			aDevice.getDevComputationChar().setMemoryClass(
					MemoryClass.DEV_MEMORY_CLASS_SENSOR);
			aDevice.getDevEnergyChar().setEnergySourceType(
					EnergySource.BLE_DEV_BATTRERY_NON_RECHARGEABLE);
			aDevice.getDevEnergyChar().setInitEnergyLevel(99);
			aDevice.getDevEnergyChar().setBatterySize(0.025);
			aDevice.getTrafficModel().setPeriod(1.0);
			aDevice.getTrafficModel().setDestination(50-i);
			aDevice.getTrafficModel().setNrofPackets(10);
			aDevice.getTrafficModel().setDelay(7);
			aDevice.getDevMiddleware().getTopologyCtrlBlock().setCapacity(7);
			aDevice.bleDeviceName = new String("BleDevice" + i);
			devices.add(aDevice);
		}
		return devices;
	}
	*/

	/**
	 * Constructs a {@code BleSimulator}.
	 * @throws IOException
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws IOException {

		Simulator sim = new BleMeshSimulator(1, 1000, 1, 3, "15_Deployment_0_Traf_0.json",1);
		
		for (final BleMeshDevice aDevice : devices) {
			Event e = new Event(0, new Runnable() {
				@Override
				public void run() {
					AlgorithmPhase1(aDevice);
				}
			});
			insert(e);
		}

		for (final BleMeshDevice aDevice : devices) {
			Event e = new Event(6, new Runnable() {
				@Override
				public void run() {
					aDevice.UpdateRoutingTable();
				}
			});
			insert(e);
		}

		for (final BleMeshDevice aDevice : devices) {
			Event e = new Event(10, new Runnable() {
				@Override
				public void run() {
					DeviceLogger();
				}
			});
			insert(e);
		}

		sim.doAllEvents();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT,SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY);
		File jsonLogFile = new File("Logs/NetworkLog.json");
		try {
			mapper.writeValue(jsonLogFile, simLog);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Done with the experiment");
		}
		System.out.println("Done with the experiment");
	}

	static void AlgorithmPhase1(final BleMeshDevice aBleDevice) {

		/* Step 1: Generate the network graph */
		aBleDevice
				.getDevMiddleware()
				.getTopologyCtrlBlock()
				.updateNeighbourList(
						txManager.getNeighbouringDevices(aBleDevice));
		
		if (aBleDevice instanceof BleMeshDeviceRSMN)
			((BleMeshDeviceRSMN) aBleDevice).ComputeInitalRSM();

		for (BleMeshDeviceLink neighDevice : aBleDevice.getDevMiddleware()
				.getTopologyCtrlBlock().getDevNeighbours()) {

		}

		Event e = new Event(1, new Runnable() {
			@Override
			public void run() {
				aBleDevice.construct();
			}
		});

		insert(e);

		return;
	}

	static void DeviceLogger() {

		class NeighbourloggingClass {
			int devUniqueId;
			int devAdd;
			double devLocX;
			double devLocY;
			int linkColor;
			double distance;

			NeighbourloggingClass() {
			}
		}

		class slavelogClass {
			int slaveUniqId;
			int slaveAdd;

			slavelogClass(int aSlaveUniqId, int aSalveAdd) {
				this.slaveUniqId = aSlaveUniqId;
				this.slaveAdd = aSalveAdd;
			}
		}

		class PiconetLoggingClass {
			int picMasterId;
			int picMasterAdd;
			ArrayList<slavelogClass> slaves;

			PiconetLoggingClass() {
				slaves = new ArrayList<slavelogClass>();
			}
		}

		class Packets {
			List<Double> Energy;
			List<Double> Time;

			Packets(List<Double> aEnergy, List<Double> aTime) {
				Energy = aEnergy;
				Time = aTime;
			}

			Packets() {
				Energy = new ArrayList<Double>();
				Time = new ArrayList<Double>();
			}
		}

		class DeviceloggingClass {
			int devUniqueId;
			int devAdd;
			double devLocX;
			double devLocY;
			int packetsRec;
			int bridgeCnt;
			Boolean isDeviceCritical;
			ArrayList<NeighbourloggingClass> neighbours;
			ArrayList<PiconetLoggingClass> piconet;
			Packets packStat;

			DeviceloggingClass(List<Double> aEnergy, List<Double> aTime) {
				neighbours = new ArrayList<NeighbourloggingClass>();
				piconet = new ArrayList<PiconetLoggingClass>();
				packStat = new Packets(aEnergy, aTime);
			}

			DeviceloggingClass() {
				neighbours = new ArrayList<NeighbourloggingClass>();
				piconet = new ArrayList<PiconetLoggingClass>();
				packStat = new Packets();
			}
		}

		class GraphLoggingClass {
			int size;
			ArrayList<DeviceloggingClass> Graph;

			GraphLoggingClass() {
				Graph = new ArrayList<DeviceloggingClass>();
			}
		}

		GraphLoggingClass Graph = new GraphLoggingClass();

		for (BleMeshDevice aDevice : devices) {

			DeviceloggingClass alog = new DeviceloggingClass();

			alog.devUniqueId = aDevice.getDevUniqueId();
			alog.devAdd = aDevice.getDevTrafficChar().getAddress();
			alog.devLocX = aDevice.getDevMobilityChar().getX();
			alog.devLocY = aDevice.getDevMobilityChar().getY();
			alog.packetsRec = aDevice.getDevMiddleware().getRoutingCtrlBlock()
					.getPacketsRecived();
			alog.isDeviceCritical = aDevice.isDeviceCritical();
			alog.bridgeCnt = aDevice.bridgeCnt;

			/* If Device part of a piconet. */
			List<BleMeshDevicePiconet> piconetList = aDevice.getDevMiddleware()
					.getTopologyCtrlBlock().getDevPiconet();

			for (BleMeshDevicePiconet aPiconet : piconetList) {
				/*
				 * Get logging information for all the slaves Link Color,
				 * Distance, Cartesian Coordinates(x,y,z) ...
				 */

				PiconetLoggingClass aPicoLog = new PiconetLoggingClass();

				aPicoLog.picMasterId = aPiconet.getDevMaster().getDevUniqueId();
				aPicoLog.picMasterAdd = aPiconet.getDevMaster()
						.getDevTrafficChar().getAddress();

				List<BleMeshDevice> slavesList = aPiconet.getDevSlaves();
				for (BleMeshDevice aSlave : slavesList) {
					slavelogClass aSlavelog = new slavelogClass(
							aSlave.getDevUniqueId(), aSlave.getDevTrafficChar()
									.getAddress());
					aPicoLog.slaves.add(aSlavelog);
				}
				alog.piconet.add(aPicoLog);
			}

			List<BleMeshDeviceLink> neighbourLinks = aDevice.getDevMiddleware()
					.getTopologyCtrlBlock().getDevNeighbours();
			for (BleMeshDeviceLink alink : neighbourLinks) {
				NeighbourloggingClass aNeighbour = new NeighbourloggingClass();
				aNeighbour.devUniqueId = alink.getLinkDevice().getDevUniqueId();
				aNeighbour.devAdd = alink.getLinkDevice().getDevTrafficChar()
						.getAddress();
				aNeighbour.devLocX = alink.getLinkDevice().getDevMobilityChar()
						.getX();
				aNeighbour.devLocY = alink.getLinkDevice().getDevMobilityChar()
						.getX();
				aNeighbour.linkColor = alink.getLinkColour();
				aNeighbour.distance = aDevice.getTxManager().distance(aDevice,
						alink.getLinkDevice());
				alog.neighbours.add(aNeighbour);
			}

			Graph.Graph.add(alog);
		}

		Graph.size = Graph.Graph.size();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);

		int configLog = devices.get(0).getGatewaySelRule();
		int deviceConf = devices.size();
		File jsonLogFile = new File(
				"Logs/Topology.json");
		try {
			mapper.writeValue(jsonLogFile, Graph);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
