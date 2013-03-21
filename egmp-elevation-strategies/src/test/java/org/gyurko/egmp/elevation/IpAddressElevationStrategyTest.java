package org.gyurko.egmp.elevation;

import org.gyurko.egmp.core.Egmp;
import org.gyurko.egmp.core.EgmpConfig;
import org.gyurko.egmp.core.EgmpFactory;
import org.gyurko.egmp.core.EgmpImplementation;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.MulticastSocket;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
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
    public void testIPElevationStrategy() throws Exception {
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

            Thread.sleep(10000);
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
            DatagramPacket packet = new DatagramPacket(data, data.length, Inet4Address.getByName("255.255.255.255"), PORT);

            LOGGER.debug("Sending some max elevation data for testing");
            socket.send(packet);

            Thread.sleep(10000);
        }

        instance.shutdownEgpmNode();
    }
}
