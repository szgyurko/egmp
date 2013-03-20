package org.gyurko.egmp.core;

import junit.framework.Assert;
import org.gyurko.egmp.core.impl.EgmpMulticast;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.MulticastSocket;

/**
 * Created by IntelliJ IDEA.
 * User: Szabolcs Gyurko
 * Date: 3/20/13
 * Time: 8:09 PM
 *
 * @author szabolcs
 */
public class EgmpFactoryTest {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpFactoryTest.class);
    /** EGMP config object */
    private EgmpConfig config;

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
        config.setIP(Inet4Address.getByName("239.190.1.1"));
        config.setPort(5328);

        Egmp instance = EgmpFactory.getInstance(config);
        try {
            instance.initEgmpNode();
        } catch (EgmpException eg) {
            LOGGER.error("Error creating EGMP object", eg);
        }

        Assert.assertTrue(true);
    }

    @Test
    public void testIPV6Multicast() throws Exception {
        config.setIP(Inet6Address.getByName("ff08::be:0101"));
        config.setPort(5328);

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();

        Assert.assertTrue(true);
    }

    @Test
    public void testHeartBeatThread() throws Exception {
        config.setIP(Inet4Address.getByName("239.190.1.1"));
        config.setPort(5328);
        config.setHeartBeatSchedulerEnabled(true);

        /* Set up a listener */
        MulticastSocket socket = new MulticastSocket(5328);
        socket.joinGroup(Inet4Address.getByName("239.190.1.1"));

        byte[] recvbuf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(recvbuf, recvbuf.length);

        /* Sleep a little bit for the IGMP to go out */
        Thread.sleep(1000);

        Egmp instance = EgmpFactory.getInstance(config);
        instance.initEgmpNode();

        int i;

        for (i = 0; i < 5; i++) {
            socket.receive(packet);
            String data = new String(packet.getData()).substring(0, packet.getLength());

            LOGGER.debug("Received data: |{}|", data);

            if ("NODE".equals(data))
                break;
        }

        socket.leaveGroup(Inet4Address.getByName("239.190.1.1"));
        socket.close();

        instance.shutdownEgpmNode();

        Assert.assertTrue(i < 5);
    }
}
