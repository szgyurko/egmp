package org.gyurko.egmp.elevation;

import org.gyurko.egmp.core.Egmp;
import org.gyurko.egmp.core.EgmpConfig;
import org.gyurko.egmp.core.EgmpFactory;
import org.gyurko.egmp.core.EgmpImplementation;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;

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
public class IpAddressElevationStrategyTest {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(IpAddressElevationStrategyTest.class);
    /** EGMP config object */
    private EgmpConfig config;
    /** Multicast group to use */
    private static final String MULTICAST_V4_GROUP = "239.190.1.1";
    /** Multicast port to use */
    private static final int PORT = 5328;

    @Before
    public void setUpConfig() {
        config = new EgmpConfig();
    }

    @Test
    public void testIPElevationStrategyMulticast() throws Exception {
        config.setIp(Inet4Address.getByName(MULTICAST_V4_GROUP));
        config.setPort(PORT);
        config.setHeartBeatSchedulerEnabled(true);
        config.setElevationStrategy(new IpAddressElevationStrategy());

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();
        MulticastSocket socket = new MulticastSocket(PORT);

        for (int i = 0; i < 3; i++) {
            byte[] data = Long.toString(Long.MAX_VALUE).getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, Inet4Address.getByName(MULTICAST_V4_GROUP), PORT);

            LOGGER.debug("Sending some max elevation data for testing");
            socket.send(packet);

            Thread.sleep(5000);
        }

        instance.shutdownEgpmNode();
    }

    @Test
    public void testIPElevationStrategyUnicast() throws Exception {
        config.setIp(Inet4Address.getLocalHost());
        config.setPort(PORT);
        config.setHeartBeatSchedulerEnabled(true);
        config.setElevationStrategy(new IpAddressElevationStrategy());
        config.setImplementation(EgmpImplementation.UNICAST);

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();

        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        for (int i = 0; i < 3; i++) {
            byte[] data = Long.toString(Long.MAX_VALUE).getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, getBroadcastAddress(), PORT);

            LOGGER.debug("Sending some max elevation data for testing");
            socket.send(packet);

            Thread.sleep(5000);
        }

        instance.shutdownEgpmNode();
    }

    /**
     * Tries to determine the lan local Broadcast address
     *
     * @returns The LAN local broadcast address
     */
    private InetAddress getBroadcastAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface current = interfaces.nextElement();
                LOGGER.trace("Checking: {} P2P: {}, LOOP: {}, Virtual: {}, Up: {}", current, current.isPointToPoint(), current.isLoopback(), current.isVirtual(), current.isUp());
                if (!current.isUp() || current.isLoopback() || current.isVirtual() || current.isPointToPoint()) continue;
                LOGGER.trace("Matching interface: {}", current);
                for (InterfaceAddress currentAddress : current.getInterfaceAddresses()) {
                    LOGGER.trace("Checking address: {}", currentAddress);
                    if (currentAddress.getAddress().isLoopbackAddress() || !(currentAddress.getAddress() instanceof Inet4Address)) continue;
                    return currentAddress.getBroadcast();
                }
            }
        } catch (SocketException e) {
            LOGGER.warn("Cannot get local host IP");
        }

        InetAddress broadcastAddress = null;

        try {
            broadcastAddress = Inet4Address.getByName("255.255.255.255");
        } catch (UnknownHostException uhe) {
        }

        return broadcastAddress;
    }

}
