package org.gyurko.egmp.test;

import org.gyurko.egmp.core.*;
import org.gyurko.egmp.elevation.IpAddressElevationStrategy;
import org.gyurko.egmp.elevation.NodeIdElevationStrategy;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
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
    @Override
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
                egmpConfig.setElevationStrategy(new NodeIdElevationStrategy());
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

        egmp.shutdownEgpmNode();
    }
}