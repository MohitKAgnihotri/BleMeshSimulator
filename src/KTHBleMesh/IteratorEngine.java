package KTHBleMesh;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class IteratorEngine {
	static String folder = "C:\\HII project";
	static String logFileName = "C:\\EventSimulator\\TestCaseExecution\\Logs\\11.json";

	private static final int numBridges = 2;
	private static final int[] numDevices = {25};
	// private static final int[] numDevices = {35,45,55,65,75};
	private static final int numDeployments = 1;
	private static final double[] deploymentChar = { 0.3 };
	//private static final double[] deploymentChar = { 0.3, 0.5, 0.7 };
	
	public static void main(String args[]) {
		//Scanner scanner = new Scanner(System.in);

		Simulator sim = null;

		for (int devType = 1; devType < 2; devType++) {
			for (int gwSel = 1; gwSel < 2; gwSel++) {
				for (int i = 0; i < numDevices.length; i++) {
					for (int j = 0; j < deploymentChar.length; j++) {
						String configSubfolder = "Config_" + deploymentChar[j] + "_" + numDevices[i];
						System.out.println(configSubfolder);
						for (int k = 0; k < numDeployments; k++) {
							try {
								String fileName = numDevices[i] + "_Deployment_" + k + "_traf_0.json";
								System.out.println(fileName);
								String filePath = folder + File.separator + configSubfolder + File.separator + fileName;
								System.out.println(filePath);
								sim = new BleMeshSimulator(1, 10000, devType, gwSel, filePath, numBridges);

								for (final BleMeshDevice aDevice : BleMeshSimulator.devices) {								
									Event e = new Event(0, new Runnable() {
										@Override
										public void run() {
											BleMeshSimulator.AlgorithmPhase1(aDevice);
										}
									});
									BleMeshSimulator.insert(e);	
								}

								for (final BleMeshDevice aDevice : BleMeshSimulator.devices) {
									Event e = new Event(6, new Runnable() {
										@Override
										public void run() {
											aDevice.UpdateRoutingTable();
										}
									});
									BleMeshSimulator.insert(e);
								}

								for (final BleMeshDevice aDevice : BleMeshSimulator.devices) {
									Event e = new Event(10, new Runnable() {
										@Override
										public void run() {
											BleMeshSimulator.DeviceLogger();
										}
									});
									BleMeshSimulator.insert(e);
								}

								sim.doAllEvents();
								ObjectMapper mapper = new ObjectMapper();
								mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
								mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT,SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY);
								File jsonLogFile = new File(logFileName);
								try {
									mapper.writeValue(jsonLogFile, BleMeshSimulator.simLog);
								} catch (JsonGenerationException e) {
									e.printStackTrace();
								} catch (JsonMappingException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
									System.out.println("Done with the experiment");
								}
								System.out.println("Done with the experiment");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		JsonParser parser = new JsonParser(numBridges, logFileName);
		int cntEnergyConsumed = parser.parseResults();
		float minimumTime = 10000;
		for (int i=0; i<cntEnergyConsumed;i++) {
			float time = (float)parser.criticalTimes.get(i);
			boolean criticalFailure = (boolean)parser.criticalFailures.get(i);
			double currEnergy = (double)parser.criticalEnergy.get(i);
			System.out.println("Time = " + time + ", isCriticalFailure = " + criticalFailure + ", currEnergy = " + currEnergy);
			if (numBridges==1) {
				if (time<minimumTime)
					minimumTime=time;
			} else if (numBridges==2) {
				if (criticalFailure && time<minimumTime)
					minimumTime=time;
			}
		}
		System.out.println("Network lifetime = " + minimumTime);
	}
	
}
