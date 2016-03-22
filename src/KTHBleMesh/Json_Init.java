package KTHBleMesh;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Json_Init {
	
	public static void main (String args[]) {
		try {
			JsonObject jsonObject = JsonObject.readFrom(new FileReader("C:/HII project/Config_0.3_15/15_Deployment_0_Traf_0.json"));
			System.out.println(jsonObject);
			int deployCnt = jsonObject.names().size();
			System.out.println("deployCnt = " + deployCnt);
			for( String name : jsonObject.names() ) {
				  JsonObject json2 = jsonObject.get( name ).asObject();
				  System.out.println(name);
				  int address = json2.get("address").asInt();
				  System.out.println("address = " + address);
				  float batterySize = json2.get("batterySize").asFloat();
				  System.out.println("batterySize = " + batterySize);
				  String computeClass = json2.get("computeClass").toString();
				  System.out.println("computeClass = " + computeClass);
				  int delay = json2.get("delay").asInt();
				  System.out.println("delay = " + delay);
				  int destination = json2.get("destination").asInt();
				  System.out.println("destination = " + destination);
				  String energyDissipation = json2.get("energyDissipitation").toString();
				  System.out.println("energyDissipation = " + energyDissipation);
				  String energySourceType = json2.get("energySourceType").toString();
				  System.out.println("energySourceType = " + energySourceType);
				  int initEnergyLevel = json2.get("initEnergyLevel").asInt();
				  System.out.println("initEnergyLevel = " + initEnergyLevel);
				  int maxDistance = json2.get("maxDistance").asInt();
				  System.out.println("maxDistance = " + maxDistance);
				  String memoryClass = json2.get("memoryClass").toString();
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
				}
			//System.out.println(((JsonObject)jsonObject.get("0")).valueOf("address"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
