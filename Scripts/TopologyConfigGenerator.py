#!/usr/bin/python3

import random;
import math;
import numpy;
import os;
import json;

NumberofDevice=[30]
NumberofDeploy= 1
NumberofTrafficModel=1
rangeX = NumberofDevice[0];
rangeY = NumberofDevice[0];
rangeZ = 1;
rangeTxRx = 10;
ScalingFactor = 10;
ValidFreq = [1,1,1,1]
TimeScaleFac = 1



#DeploymentChar =[0.15,0.15,0.7]
#DeploymentChar =[0.25,0.25,0.5]
DeploymentChar =[0.35,0.35,0.3]


DeviceType=["redhawk_ble_mesh.BleMeshDevice","redhawk_ble_mesh.BleMeshDeviceRSM","redhawk_ble_mesh.BleMeshDeviceRSMN"]
devProperties =[[]];

ComputeClass = ["DEV_COMPUTE_CLASS_SENSOR", "DEV_COMPUTE_CLASS_APPLIANCE", "DEV_COMPUTE_CLASS_PC"]
MemoryClass = ["DEV_MEMORY_CLASS_SENSOR", "DEV_MEMORY_CLASS_APPLIANCE", "DEV_MEMORY_CLASS_PC"]
EnergySrc = ["BLE_DEV_BATTRERY_NON_RECHARGEABLE", "BLE_DEV_BATTRERY_RECHARGEABLE", "BLE_DEV_CONNECTED_SUPPLY"]
BatterySize = [.0250, 0.2500,0] #Scaled BatterySize by factor of 10000

ConfigDump = {};



def RSD_Function(devNum,file,devProperties):
        for i in range(0,devNum):
            JsonDump={};
            JsonDump["address"]=devProperties[i][14]
            JsonDump["x"]=devProperties[i][1]
            JsonDump["y"]=devProperties[i][2]
            JsonDump["z"]=devProperties[i][3]
            JsonDump["maxDistance"]=devProperties[i][4]
            JsonDump["computeClass"]=devProperties[i][5]
            JsonDump["memoryClass"]=devProperties[i][6]
            JsonDump["energySourceType"]=devProperties[i][7]
            JsonDump["initEnergyLevel"]=devProperties[i][8]

            JsonDump["batterySize"]=devProperties[i][9]
            JsonDump["period"]=devProperties[i][10]
            JsonDump["destination"]=devProperties[i][11]
            JsonDump["nrofPackets"]=1
            JsonDump["delay"]=devProperties[i][13]
            JsonDump["energyDissipitation"]=0.1+(devProperties[i][10]*3*0.001)
            ConfigDump[i]= JsonDump;
            JsonDump ={}
            print("sim.devices[",i,"].devTrafficChar.address = ",devProperties[i][14],file=file)
            print("sim.devices[",i,"].devMobilityChar.x = ",devProperties[i][1],file=file)
            print("sim.devices[",i,"].devMobilityChar.y = ",devProperties[i][2],file=file)
            print("sim.devices[",i,"].devMobilityChar.z = ",devProperties[i][3],file=file)
            print("sim.devices[",i,"].devRadioChar.maxDistance = ",devProperties[i][4],file=file)
            print("sim.devices[",i,"].devComputationChar.computeClass = ",devProperties[i][5],file=file)
            print("sim.devices[",i,"].devComputationChar.memoryClass = ",devProperties[i][6],file=file)
            print("sim.devices[",i,"].devEnergyChar.energySourceType = ",devProperties[i][7],file=file)
            print("sim.devices[",i,"].devEnergyChar.initEnergyLevel = ",devProperties[i][8],file=file)
            print("sim.devices[",i,"].devEnergyChar.batterySize = ",devProperties[i][9],file=file)
            print("sim.devices[",i,"].trafficModel.period = ",devProperties[i][10]/TimeScaleFac,file=file)
            print("sim.devices[",i,"].trafficModel.destination = ",devProperties[i][11],file=file)
            print("sim.devices[",i,"].trafficModel.nrofPackets = ",1,file=file)
            print("sim.devices[",i,"].trafficModel.delay = ",devProperties[i][13],file=file)
            print("sim.devices[",i,"].devEnergyChar.energyDissipitation = ",0.1+(devProperties[i][10]*3*0.001),file=file)  #Assuming for every Tx we have 3 Rx

if __name__ == "__main__":
    for num in NumberofDevice:
        DirName = "../Config/Config_"+str(DeploymentChar[2])+"_"+str(num)
        TotalSenDev = math.ceil(num*DeploymentChar[2])
        TotalMainDev = math.ceil(num * ((1-DeploymentChar[2])/2))
        TotalRecDev = math.ceil(num * ((1-DeploymentChar[2])/2))


        devProperties = num * [None]
        DT = ['BLE_DEV_CONNECTED_SUPPLY'] * int(num * DeploymentChar[0]+1) + ['BLE_DEV_BATTRERY_RECHARGEABLE'] * int(num * DeploymentChar[1]+1) +['BLE_DEV_BATTRERY_NON_RECHARGEABLE'] * int(num * DeploymentChar[2]+1)
        random.shuffle(DT)

        for dep in range(0,NumberofDeploy):
            SensorLoc = numpy.random.uniform(0,30,size=(TotalSenDev,2))
            MainLoc = numpy.random.uniform(0,30,size=(TotalMainDev,2))
            RecLoc = numpy.random.uniform(0,30,size=(TotalRecDev,2))
            print("////////////////////")
            print(SensorLoc)
            print("////////////////////")
            print(MainLoc)
            print("////////////////////")
            print(RecLoc)

            AddressAssignment = [i for i in range(num)]
            random.shuffle(AddressAssignment)
            for i in range(0,num):
                if (i < TotalSenDev):
                    devProperties[i]=["Random",
                                      SensorLoc[i][0],                                      #X-Pos
                                      SensorLoc[i][1],                                      #Y-Pos
                                      random.randint(1, rangeZ),                            #Z-Pos
                                      random.randint(rangeTxRx, rangeTxRx),                 #RxTx-Range
                                      ComputeClass[random.randint(0, 2)],                   #Compute Class
                                      MemoryClass[random.randint(0, 2)],                    #Memory Class
                                      'BLE_DEV_BATTRERY_NON_RECHARGEABLE',                  #Energy Source
                                      random.randint(50, 99),                               #Battery Percentage
                                      random.randint(50, 99),                               #Battery Size
                                      ValidFreq[random.randint(0,3)],                       #Period of Packet Tx
                                      AddressAssignment[TotalSenDev + i % (num - TotalSenDev)],                             #Peer Address
                                      random.randint(1, num-1)*1000,                        #No of Packets to be Tx
                                      7,                                                    #Delay for the first packet
                                      AddressAssignment[i]];

                    devProperties[i][5] = ComputeClass[random.randint(0, 1)] if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else ComputeClass[random.randint(0, 2)]
                    devProperties[i][6] = MemoryClass[random.randint(0, 1)] if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else MemoryClass[random.randint(0, 2)]
                    devProperties[i][8] = 99 if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else random.randint(50, 99)
                    devProperties[i][9] = BatterySize[ 0 if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else 1]
                    devProperties[i][13] = 7
                elif (i >= TotalSenDev and (i - TotalSenDev) < TotalMainDev):
                    devProperties[i]=["Random",
                                      MainLoc[i-TotalSenDev][0],                            #X-Pos
                                      MainLoc[i-TotalSenDev][1],                            #Y-Pos
                                      random.randint(1, rangeZ),                            #Z-Pos
                                      random.randint(rangeTxRx, rangeTxRx),                 #RxTx-Range
                                      ComputeClass[random.randint(0, 2)],                   #Compute Class
                                      MemoryClass[random.randint(0, 2)],                    #Memory Class
                                      'BLE_DEV_CONNECTED_SUPPLY',                           #Energy Source
                                      random.randint(50, 99),                               #Battery Percentage
                                      random.randint(50, 99),                               #Battery Size
                                      ValidFreq[random.randint(0,3)],                       #Period of Packet Tx
                                      AddressAssignment[TotalSenDev + i % (num - TotalSenDev)],       #Peer Address
                                      random.randint(1, num-1)*1000,                        #No of Packets to be Tx
                                      7,                                                    #Delay for the first packet
                                      AddressAssignment[i]];

                    devProperties[i][5] = ComputeClass[random.randint(0, 1)] if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else ComputeClass[random.randint(0, 2)]
                    devProperties[i][6] = MemoryClass[random.randint(0, 1)] if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else MemoryClass[random.randint(0, 2)]
                    devProperties[i][8] = 99 if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else random.randint(50, 99)
                    devProperties[i][9] = BatterySize[ 0 if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else 1]
                    devProperties[i][13] = 7
                elif (i >= (TotalSenDev + TotalMainDev) and (i - TotalSenDev - TotalMainDev) < TotalRecDev):
                    devProperties[i]=["Random",
                                      RecLoc[i-(TotalSenDev + TotalMainDev)][0],            #X-Pos
                                      RecLoc[i-(TotalSenDev + TotalMainDev)][1],            #Y-Pos
                                      random.randint(1, rangeZ),                            #Z-Pos
                                      random.randint(rangeTxRx, rangeTxRx),                 #RxTx-Range
                                      ComputeClass[random.randint(0, 2)],                   #Compute Class
                                      MemoryClass[random.randint(0, 2)],                    #Memory Class
                                      'BLE_DEV_BATTRERY_RECHARGEABLE',                      #Energy Source
                                      random.randint(50, 99),                               #Battery Percentage
                                      random.randint(50, 99),                               #Battery Size
                                      ValidFreq[random.randint(0,3)],                       #Period of Packet Tx
                                      AddressAssignment[TotalSenDev + i % (num - TotalSenDev)],       #Peer Address
                                      random.randint(1, num-1)*1000,                        #No of Packets to be Tx
                                      7,                                                    #Delay for the first packet
                                      AddressAssignment[i]];

                    devProperties[i][5] = ComputeClass[random.randint(0, 1)] if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else ComputeClass[random.randint(0, 2)]
                    devProperties[i][6] = MemoryClass[random.randint(0, 1)] if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else MemoryClass[random.randint(0, 2)]
                    devProperties[i][8] = 99 if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else random.randint(50, 99)
                    devProperties[i][9] = BatterySize[ 0 if devProperties[i][7] == "BLE_DEV_BATTRERY_NON_RECHARGEABLE" else 1]
                    devProperties[i][13] = 7




            for traf in range(0,NumberofTrafficModel):
                if not os.path.exists(DirName):
                    os.makedirs(DirName)

                fileName = DirName+"/"+str(num)+"_Deployment_"+str(dep)+"_Traf_"+str(traf)+".rsd"
                file = open(fileName,'w+')
                print("sim.depId = ",dep,file=file)
                RSD_Function(num,file,devProperties)
                fileJSON=open(DirName+"/"+str(num)+"_Deployment_"+str(dep)+"_Traf_"+str(traf)+".json",'w+')
                json.dump(ConfigDump,fileJSON,sort_keys=True, indent=4, separators=(',', ': '))
                fileJSON.close()
                file.close()

    for device in DeviceType:
        for num in NumberofDevice:
            DirName = "../Config/Config_"+str(DeploymentChar[2])+"_"+str(num)
            for dep in range(0,NumberofDeploy):
                for traf in range(0,NumberofTrafficModel):
                    filename = device.split('.')[1]+"_" + str(num)+"_" + str(dep)+ str(traf)+".rsd"
                    filename1 = device.split('.')[1]+"_" + str(num)+"_" + str(dep)+ str(traf)+".cfg"

                    #file = open(DirName+"\\"+filename,'w+')

                    #print("redhawkFileVersion=2",file=file)
                    #print("# A test scenario for BLE Mesh simulator",file=file)
                    #print("sim: redhawk_ble_mesh.BleMeshSimulator",file=file)

                    #print("# create devices",file=file)
                    #print("sim.devices[]: ",device,file=file)
                    #print("# Load Deployment model with Device characteristic",file=file)
                    #print(">",str(num)+"_Deployment_"+str(dep)+"_Traf_"+str(traf)+ ".rsd",file=file)
                    #print("sim.txManager.mode = simple",file=file)
                    #print("sim.endtime = 100.0",file=file)

                    #file1 = open(DirName+"\\"+filename1,'w+')
                    #print("-parfile",filename,file=file1)
                    #print("-iterate sim.devices[].gatewaySelRule={1;2;3;4}",file=file1)
                    #print("-seeds {1}",file=file1)
                    #print("-logConfig BleMesh.lcf",file=file1)
                    #print("-logFormat MAT2",file=file1)

                    #file1.close()
                    #file.close()
    file.close()
















