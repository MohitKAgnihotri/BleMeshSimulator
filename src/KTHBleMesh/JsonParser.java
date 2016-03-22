package KTHBleMesh;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class JsonParser {
	static int numBridges = 2;
	static String jsonLogFile = "C:\\EventSimulator\\TestCaseExecution\\Logs\\11.json";
	static int cntEnergyConsumed;
	static List criticalTimes;
	static List criticalFailures;
	static List criticalEnergy;
	
	
	public JsonParser(int numBridges, String logFileName) {
		this.numBridges = numBridges;
		this.jsonLogFile = logFileName;
		
		cntEnergyConsumed = 0;
		criticalTimes = new ArrayList();
		criticalFailures = new ArrayList();
		criticalEnergy = new ArrayList();
	}

	public int parseResults() {
		try {
			JsonArray jsonArray = JsonArray.readFrom(new FileReader(jsonLogFile));
			int numItems = jsonArray.size();
			for (int i=0; i<numItems; i++) {
				//System.out.println(i+1 + ".");
				JsonObject jsonObject = jsonArray.get(i).asObject();
				
				double currEnergy =  jsonObject.get("currEnergy").asDouble();
				//System.out.println("currEnergy = " + currEnergy);
				boolean isDeviceCritical = jsonObject.get("isDeviceCritical").asBoolean();
				//System.out.println("isDeviceCritical = " + isDeviceCritical);
				float time = jsonObject.get("time").asFloat();
				//System.out.println("time = " + time);
				boolean isSecondFailure = false;
				if (numBridges==2) {
					isSecondFailure = jsonObject.get("isSecondFailure").asBoolean();
					//System.out.println("isSecondFailure = " + isSecondFailure);
				}
				//System.out.println(" ");
				
				if (currEnergy<=0) {
					cntEnergyConsumed++;
					if (isDeviceCritical) {
						criticalTimes.add(time);
						criticalFailures.add(isSecondFailure);
						criticalEnergy.add(currEnergy);
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cntEnergyConsumed;
	}
	
	public static void main (String args[]) {
		JsonParser parser = new JsonParser(numBridges, jsonLogFile);
		parser.parseResults();
		float minimumTime = 1000;
		for (int i=0; i<cntEnergyConsumed;i++) {
			float time = (float)criticalTimes.get(i);
			boolean criticalFailure = (boolean)criticalFailures.get(i);
			double currEnergy = (double)criticalEnergy.get(i);
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
