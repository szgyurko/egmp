package org.gyurko.egmp.test;

import org.gyurko.egmp.core.*;
import org.gyurko.egmp.elevation.IpAddressElevationStrategy;
import org.gyurko.egmp.elevation.NodeIdElevationStrategy;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 EGMP - Extensible Group Management Protocol

 Copyright (C) 2013-2015  Szabolcs Gyurko <szabolcs@gyurko.org>

 This file is part of EGMP project.

 EGMP is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, version 2 of the License, but not
 any later version.

 EGMP is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with EGMP.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Main implements Runnable {
    /** Multicast group to use */
    private static final String MULTICAST_V4_GROUP = "239.190.1.1";
    /** Arguments */
    private String[] args;


    /**
     * Constructor
     *
     * @param args Program arguments
     */
    public Main(final String[] args) {
        this.args = args;
    }

    /**
     * Main class
     *
     * @param args Program arguments
     */
    public static void main(final String[] args) {
        new Thread(new Main(args)).run();
    }

    /**
     * Main cycle
     */
    public void run() {
        EgmpConfig egmpConfig = new EgmpConfig();

        egmpConfig.setHeartBeatSchedulerEnabled(true);
        try {
            egmpConfig.setIp(Inet4Address.getByName(MULTICAST_V4_GROUP));
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }

        if (args.length > 0) {
            try {
                int nodeId = Integer.parseInt(args[0]);
                System.setProperty("EGMP_NODE_ID", Integer.toString(nodeId));
                egmpConfig.setElevationStrategy(new NodeIdElevationStrategy());
            } catch (NumberFormatException nfe) {
                egmpConfig.setElevationStrategy(new IpAddressElevationStrategy());
            }

            if (args.length > 1 && "U".equals(args[1])) {
                egmpConfig.setImplementation(EgmpImplementation.UNICAST);
                try {
                    egmpConfig.setIp(Inet4Address.getLocalHost());
                } catch (UnknownHostException uhe) {
                    uhe.printStackTrace();
                }
            }
        } else {
            egmpConfig.setElevationStrategy(new IpAddressElevationStrategy());
        }


        Egmp egmp = EgmpFactory.getInstance(egmpConfig);
        try {
            egmp.initEgmpNode();
        } catch (EgmpException e) {
            e.printStackTrace();
        }

        try {
            while (System.in.available() == 0) {
                System.out.println("Current status: " + egmp.isElevated());
                Thread.sleep(2000);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException ie) {
        }

        egmp.shutdownEgmpNode();
    }
}