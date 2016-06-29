package org.gyurko.egmp.core;

import org.gyurko.egmp.core.impl.EgmpMulticast;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

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
public class EgmpFactoryTest {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpFactoryTest.class);
    /** Unicast broadcast for IPv6 */
    private static final String UNICAST_V6_BROADCAST = "ff02::1";
    /** EGMP config object */
    private EgmpConfig config;
    /** Multicast group to use */
    private static final String MULTICAST_V4_GROUP = "239.190.1.1";
    /** Multicast V6 group to use */
    private static final String MULTICAST_V6_GROUP = "ff08::be:0101";
    /** Multicast port to use */
    private static final int PORT = 5328;

    @Before
    public void setUpConfig() {
        config = new EgmpConfig();
    }

    @Test
    public void testGetInstance() throws Exception {
        Egmp instance = EgmpFactory.getInstance(config);

        Assert.assertEquals(EgmpMulticast.class.getCanonicalName(), instance.getClass().getCanonicalName());
        LOGGER.debug(instance.getClass().getCanonicalName());
    }

    @Test
    public void testIPV4Multicast() throws Exception {
        config.setIp(Inet4Address.getByName(MULTICAST_V4_GROUP));
        config.setPort(PORT);

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();
        instance.shutdownEgmpNode();
    }

    @Test
    public void testIPV6Multicast() throws Exception {
        config.setIp(Inet6Address.getByName(MULTICAST_V6_GROUP));
        config.setPort(PORT);

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();
        instance.shutdownEgmpNode();
    }

    @Test
    public void testHeartBeatThreadMulticast() throws Exception {
        config.setIp(Inet4Address.getByName(MULTICAST_V4_GROUP));
        config.setPort(PORT);
        config.setHeartBeatSchedulerEnabled(true);

        /* Set up a listener */
        MulticastSocket socket = new MulticastSocket(PORT);
        socket.joinGroup(Inet4Address.getByName(MULTICAST_V4_GROUP));
        socket.setSoTimeout(2000);

        byte[] recvbuf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(recvbuf, recvbuf.length);

        /* Sleep a little bit for the IGMP to go out and register with the L2 infrastructure */
        Thread.sleep(1000);

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();

        int i;

        for (i = 0; i < 5; i++) {
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException se) {
                continue;
            }
            String data = new String(packet.getData()).substring(0, packet.getLength());

            LOGGER.debug("Received data: |{}|", data);

            if ("0*".equals(data))
                break;
        }

        socket.leaveGroup(Inet4Address.getByName(MULTICAST_V4_GROUP));
        socket.close();

        instance.shutdownEgmpNode();

        Assert.assertTrue(i < 5);
    }

    @Test
    public void testHeartBeatThreadWithV6Multicast() throws Exception {
        config.setIp(Inet4Address.getByName(MULTICAST_V6_GROUP));
        config.setPort(PORT);
        config.setHeartBeatSchedulerEnabled(true);

        /* Set up a listener */
        MulticastSocket socket = new MulticastSocket(PORT);
        socket.joinGroup(Inet4Address.getByName(MULTICAST_V6_GROUP));
        socket.setSoTimeout(2000);

        byte[] recvbuf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(recvbuf, recvbuf.length);

        /* Sleep a little bit for the IGMP to go out and register with the L2 infrastructure */
        Thread.sleep(1000);

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();

        int i;

        for (i = 0; i < 5; i++) {
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException se) {
                continue;
            }
            String data = new String(packet.getData()).substring(0, packet.getLength());

            LOGGER.debug("Received data: |{}|", data);

            if ("0*".equals(data))
                break;
        }

        socket.leaveGroup(Inet4Address.getByName(MULTICAST_V6_GROUP));
        socket.close();

        instance.shutdownEgmpNode();

        Assert.assertTrue(i < 5);
    }

    @Test
    public void testIPV4Unicast() throws Exception {
        config.setIp(Inet4Address.getLocalHost());
        config.setPort(PORT);
        config.setImplementation(EgmpImplementation.UNICAST);

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();

        instance.shutdownEgmpNode();
    }

    @Test(expected = EgmpException.class)
    public void testIPV6Unicast() throws Exception {
        config.setIp(Inet6Address.getByName(UNICAST_V6_BROADCAST));
        config.setPort(PORT);
        config.setImplementation(EgmpImplementation.UNICAST);

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();
    }

    @Test(expected = EgmpException.class)
    public void testHeartBeatThreadUnicastIfPortIsAlreadyInUse() throws Exception {
        config.setIp(Inet4Address.getLocalHost());
        config.setPort(PORT);
        config.setHeartBeatSchedulerEnabled(true);
        config.setImplementation(EgmpImplementation.UNICAST);

        /* Set up a listener */
        DatagramSocket socket = new DatagramSocket(PORT, config.getIp());
        socket.setSoTimeout(2000);

        byte[] recvbuf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(recvbuf, recvbuf.length);

        Egmp instance = EgmpFactory.getInstance(config);
        try {
            instance.initEgmpNode();
        } finally {
            socket.close();
        }
    }
}
