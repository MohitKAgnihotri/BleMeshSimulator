#!/usr/bin/python
import sys, getopt
from graph_tool.all import *
from itertools import izip
from numpy.random import randint
from sys import stdin, stdout
import random;
import math;
from gi.repository import Gtk, Gdk, GdkPixbuf, GObject
import pprint
import json

import matplotlib.pyplot as plt; plt.rcdefaults()
import numpy as np
import matplotlib.pyplot as plt

#Init Global Variables
jsonData = []
IndextoUniqMap = {}
logfile = ""
path = ""

Triangle = 6
Circle = 7
Square = 8

Red = [255,0,0]
Green = [0,255,0]
Blue = [0,0,255]
Black = [0,0,0]
Silver = [255,255,0]
White = [0,0,0]

#Red = [255,255,255]
#Green = [255,255,255]
#Blue = [255,255,255]
#Black = [255,255,255]
#Silver = [255,255,255]
#White = [255,255,255]


def CreateGraph(X,Y,isDevCri,devAdd):
    plt.plot(Y,X)
    plt.xlabel('Time')
    plt.ylabel('Energy')
    plt.ylim([-10,100])
    plt.title('Energy Decay for a device: IsDeviceCritical=' + str(isDevCri) + " , DeviceAddress"+str(devAdd))
    plt.show()

def CreateNGraph(jsonData,filename,path=""):
    size = jsonData['size']
    g = Graph(directed=False)
    g.add_vertex(size);
    vprop_id = g.new_vertex_property("vector<int>")
    vprop_pos = g.new_vertex_property("vector<float>")
    eprop_dis = g.new_edge_property("float")
    eprop_clr = g.new_edge_property("vector<double>")
    vprop_shape = g.new_vertex_property("int")
    vprop_color = g.new_vertex_property("vector<float>")

    devList = jsonData['Graph']

    i = 0
    for device in devList:
        vprop_id[g.vertex(i)] = [device['devUniqueId'],device['devAdd']]
        vprop_pos[g.vertex(i)] = [device['devLocX'],device['devLocY']]
        IndextoUniqMap[(device['devUniqueId'],device['devAdd'])] = i
        if (i < 18):
            vprop_shape[g.vertex(i)]=Square;
            vprop_color[g.vertex(i)]=[255,0,0, 0.5];
        elif (i > 18 and i < 27):
            vprop_shape[g.vertex(i)]=Triangle;
            vprop_color[g.vertex(i)]=[0.640625, 1, 1, 0.9]
        else:
            vprop_shape[g.vertex(i)]=Circle;
            vprop_color[g.vertex(i)]=[255,255,0, 0.5];

        i = i + 1

    #Create Neighbour Graph
    for device in devList:
        for neighbour in device['neighbours']:
            srcDevKey = (device['devUniqueId'],device['devAdd'])
            desDevKey = (neighbour['devUniqueId'],neighbour['devAdd'])
            if (g.edge(IndextoUniqMap[srcDevKey],IndextoUniqMap[desDevKey]) is None):
                e = g.add_edge(IndextoUniqMap[srcDevKey],IndextoUniqMap[desDevKey])
                dis = neighbour['distance']
                dis = float("{0:.2f}".format(dis))
                eprop_dis[e] = dis
                if (neighbour['linkColor'] == 1):
                    eprop_clr[e] = Black
                elif (neighbour['linkColor'] == 2):
                    eprop_clr[e] = Silver
                elif (neighbour['linkColor'] == 3):
                    eprop_clr[e] = Green
                elif (neighbour['linkColor'] == 4):
                    eprop_clr[e] = Red
                elif (neighbour['linkColor'] == 5):
                    eprop_clr[e] = Blue

    graph_draw(g,pos=vprop_pos,vertex_size=8, vertex_fill_color=vprop_color, vertex_text=vprop_id, vertex_shape=vprop_shape, vertex_font_size=8, edge_color=eprop_clr, output_size=(1024, 1024), fit_view=True, output=path+"GraphEdgeClr_"+filename+".pdf")
    #graph_draw(g,pos=vprop_pos,vertex_size=18, vertex_fill_color=vprop_color, vertex_shape=vprop_shape, vertex_font_size=8, edge_color=eprop_clr, output_size=(1024, 1024), fit_view=True, output=path+"GraphEdgeClr_"+filename+".pdf")
    graph_draw(g,pos=vprop_pos,vertex_size=8, vertex_fill_color=vprop_color, vertex_text=vprop_id, vertex_shape=vprop_shape, vertex_font_size=8, edge_color=eprop_clr, output_size=(1024, 1024), fit_view=True, output=path+"GraphEdgeClr_"+filename+".eps")
    #graph_draw(g,pos=vprop_pos,vertex_size=18, vertex_fill_color=vprop_color,  vertex_shape=vprop_shape, vertex_font_size=8, edge_color=eprop_clr, output_size=(1024, 1024), fit_view=True, output=path+"GraphEdgeClr_"+filename+".eps")


def CreateCGraph(jsonData,filename,path=""):
    size = jsonData['size']
    g = Graph(directed=True)
    g.add_vertex(size);
    vprop_id = g.new_vertex_property("vector<int>")
    vprop_pos = g.new_vertex_property("vector<float>")
    vprop_shape = g.new_vertex_property("int")
    vprop_color = g.new_vertex_property("vector<float>")

    devList = jsonData['Graph']

    i = 0
    for device in devList:
        vprop_id[g.vertex(i)] = [device['devUniqueId'],device['devAdd']]
        vprop_pos[g.vertex(i)] = [device['devLocX'],device['devLocY']]
        vprop_color[g.vertex(i)]=[0.640625, 1, 1, 0.9];
        if (i < 18):
            vprop_shape[g.vertex(i)]=Square;
            vprop_color[g.vertex(i)]=[255,0,0, 0.5];
        elif (i > 18 and i < 27):
            vprop_shape[g.vertex(i)]=Triangle;
            vprop_color[g.vertex(i)]=[0.640625, 1, 1, 0.9]
        else:
            vprop_shape[g.vertex(i)]=Circle;
            vprop_color[g.vertex(i)]=[255,255,0, 0.5];

        IndextoUniqMap[(device['devUniqueId'],device['devAdd'])] = i
        i = i + 1

    #Print the IndextoUniqMap
    #print(IndextoUniqMap)

    #Create Neighbour Graph
    for device in devList:
        X = device['packStat']['Energy']
        Y = device['packStat']['Time']
        #CreateGraph(X,Y,device['isDeviceCritical'],device['devAdd'])
        MasterKey = (device['devUniqueId'],device['devAdd'])
        for piconet in device['piconet']:
            srcDevKey = (piconet['picMasterId'],piconet['picMasterAdd'])
            for slave in piconet['slaves']:
                desDevKey = (slave['slaveUniqId'],slave['slaveAdd'])
                if (g.edge(IndextoUniqMap[srcDevKey],IndextoUniqMap[desDevKey]) is None):
                    e = g.add_edge(IndextoUniqMap[srcDevKey],IndextoUniqMap[desDevKey])
                    vprop_shape[e.source()]=Triangle;
                    vprop_shape[e.target()]=Circle;
                    vprop_color[e.source()]=[255,0,0, 0.5];
                    vprop_color[e.target()]=[0.640625, 1, 1, 0.9];

                    if(e.target().in_degree()>1 or e.target().out_degree == 0):
                        vprop_shape[e.target()]=Square;
                        vprop_color[e.target()]=[255,255,0, 0.5];

                    if(e.target().in_degree()>=1 and e.target().out_degree()>=1):
                        if(vprop_shape[e.target()] == Triangle):
                            vprop_color[e.target()]=[255,255,0, 0.5];
                        else:
                            vprop_shape[e.target()]=Square;
                            vprop_color[e.target()]=[255,255,0, 0.5];

                    if(e.source().in_degree()>=1 and e.source().out_degree()>=1):
                        if(vprop_shape[e.source()] == Triangle):
                            vprop_color[e.source()]=[255,255,0, 0.5];
                        else:
                            vprop_shape[e.source()]=Square;
                            vprop_color[e.source()]=[255,255,0, 0.5];

                    if(MasterKey == srcDevKey):
                            vprop_shape[e.source()]=Triangle;


    graph_draw(g,pos=vprop_pos, vertex_size=8, vertex_font_size=8, vertex_fill_color=vprop_color, vertex_text=vprop_id, vertex_shape=vprop_shape,  output_size=(1024, 1024), fit_view=True, output=path+"GraphDia_"+filename+".pdf")



    g.set_directed(False)
    RouteLen = 0
    Routes = 0
    for v in g.vertices():
        for u in g.vertices():
            vlist, elist = graph_tool.topology.shortest_path(g, v, u, weights=None, pred_map=None)
            if vlist:
                Routes = Routes +1
                RouteLen = RouteLen + len(vlist)

                #print("Shortest Path from %d to %d ",g.vertex_index[v],g.vertex_index[u])
                #print([str(m) for m in vlist])

    #print("AverageRouteLen = ",float(RouteLen/Routes))


def parseArguments(argv):
    try:
        opts, args = getopt.getopt(argv,"i:p:",["ifile=,path="])
        print(args)
    except getopt.GetoptError:
        print "1 UpGraph.py -i <inputfile>"
        sys.exit(2)

    if(len(opts) == 0):
        print "2 UpGraph.py -i <inputfile>"
        sys.exit(2)

    for opt, arg in opts:
        if opt == '-h':
            print "3 UpGraph.py -i <inputfile>"
            sys.exit()
        elif opt in ("-i", "--ifile"):
            global logfile
            logfile = arg
            print "4 Input file is ", logfile
	elif opt in ("-p", "--path"):
            global path
            path = arg
            print "4 Input file is ", path


if __name__ == "__main__":
    parseArguments(sys.argv[1:])
    with open(logfile) as data_file:
        jsonData = json.load(data_file)
        #pprint.pprint(jsonData)


    if ("RSM" in logfile and not("RSMN" in logfile)):
        filename = "RSM"
    elif ("RSMN" in logfile):
        filename = "RSMN"
    else:
        filename = "Dflt"

    CreateNGraph(jsonData,filename,path)
    CreateCGraph(jsonData,filename,path)
