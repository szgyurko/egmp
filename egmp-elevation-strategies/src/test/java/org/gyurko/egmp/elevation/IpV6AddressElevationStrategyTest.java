package org.gyurko.egmp.elevation;

import org.gyurko.egmp.core.Egmp;
import org.gyurko.egmp.core.EgmpConfig;
import org.gyurko.egmp.core.EgmpFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class IpV6AddressElevationStrategyTest {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(IpV6AddressElevationStrategyTest.class);
    /** EGMP config object */
    private EgmpConfig config;
    /** Multicast V6 group to use */
    private static final String MULTICAST_V6_GROUP = "ff08::be:0101";
    /** Multicast port to use */
    private static final int MULTICAST_PORT = 5328;

    @Before
    public void setUpConfig() {
        config = new EgmpConfig();
    }
    @Test
    public void testIPElevationStrategy() throws Exception {
        config.setIp(Inet6Address.getByName(MULTICAST_V6_GROUP));
        config.setPort(MULTICAST_PORT);
        config.setHeartBeatSchedulerEnabled(true);
        config.setElevationStrategy(new IpV6AddressElevationStrategy());

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();
        MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);

        for (int i = 0; i < 3; i++) {
            byte[] data = Long.toString(Long.MAX_VALUE).getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, Inet6Address.getByName(MULTICAST_V6_GROUP), MULTICAST_PORT);

            LOGGER.debug("Sending some max elevation data for testing");
            socket.send(packet);

            Thread.sleep(10000);
        }

        instance.shutdownEgpmNode();
    }
}
